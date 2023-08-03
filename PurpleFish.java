import java.util.ArrayList;
import java.util.Collections;

public class PurpleFish extends Fish {


    public PurpleFish(int pos_x, int pos_y, int target_x, int target_y) {
        super(pos_x, pos_y, target_x, target_y);
        /* la vitesse des poissons mauves varie en fonction du nombre de poissons oranges dans l'aquarium */
        setSpeed(INIT_SPEED + Board.amountOfOrangeFish());
    }

    @Override
    public void move(Board board) {
        initFishBehaviour(board);

        for (MovingGameElement mvElemOther : board.fishList) {
            ArrayList<Integer> tempoX = new ArrayList<Integer>();
            ArrayList<Integer> tempoY = new ArrayList<Integer>();
            ArrayList<Double> tempoDistance = new ArrayList<Double>();

            /* Les poissons mauves se déplacent toujours dans la direction opposée au poisson rouge le plus proche.
             * Dans le cas contraire où il n'y a aucun poisson rouge autour de lui (RANGE_DISTANCE), il continue de nager en gardant le même comportement que les poissons orange */
            if (mvElemOther.getClass().getSimpleName().equals("RedFish")) {
                fishBehaviour(board, mvElemOther, tempoX, tempoY, tempoDistance, RANGE_DISTANCE);
            }
        }

        if (isModeActivated()) {
            handleModeActivation(board);
        }

        super.setPositions();
    }

    @Override
    protected void fishBehaviour(Board board, MovingGameElement mvElemOther, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance, int range) {
        calculPossibilities(board, tempoX, tempoY);
        calculDistance(tempoDistance, tempoX, tempoY, mvElemOther.getPos_x(), mvElemOther.getPos_y());

        /* pour créer cet effet d'éloignement du poisson mauve par rapport aux poissons rouges, on ne regarde plus la distance minimale mais plutôt la distance maximale */
        if (Collections.min(tempoDistance) < RANGE_DISTANCE && Collections.max(tempoDistance) < Collections.max(getDistances())) {
            setDistances(tempoDistance);
            setX_moveOptions(tempoX);
            setY_moveOptions(tempoY);
            index = getDistances().indexOf(Collections.max(getDistances()));
        }
    }
}
