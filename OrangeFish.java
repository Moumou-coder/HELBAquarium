import java.util.ArrayList;
import java.util.Collections;

public class OrangeFish extends Fish {

    private final int initSpeed = 7;

    public OrangeFish(int pos_x, int pos_y, int target_x, int target_y) {
        super(pos_x, pos_y, target_x, target_y);
    }

    @Override
    public void move(Board board) {
        super.setX_moveOptions(new ArrayList<Integer>());
        super.setY_moveOptions(new ArrayList<Integer>());
        super.setDistances(new ArrayList<Double>());

        calculPossibilities(board, getX_moveOptions(), getY_moveOptions());
        calculDistance(getDistances(), getX_moveOptions(), getY_moveOptions(), getTarget_x(), getTarget_y());
        index = getDistances().indexOf(Collections.min(getDistances()));

        if (isModeActivated()) {
            if (getTargetType().equals(Insect.class.getSimpleName())) {
                board.fixedGameElementList.stream().filter(f -> f.getClass().getSimpleName().equals(getTargetType())).forEach(f -> {
                    System.out.println("je suis un " + f.getClass().getSimpleName());
                    ArrayList<Integer> tempoX = new ArrayList<Integer>();
                    ArrayList<Integer> tempoY = new ArrayList<Integer>();
                    ArrayList<Double> tempoDistance = new ArrayList<Double>();
                    fishMode(board, f, tempoX, tempoY, tempoDistance);
                });
            } else if (getTargetType().equals(Pellet.class.getSimpleName())) {
                board.fixedGameElementList.stream().filter(f -> f.getClass().getSimpleName().equals(getTargetType())).forEach(f -> {
                    System.out.println("I AM ...  " + f.getClass().getSimpleName());
                    ArrayList<Integer> tempoX = new ArrayList<Integer>();
                    ArrayList<Integer> tempoY = new ArrayList<Integer>();
                    ArrayList<Double> tempoDistance = new ArrayList<Double>();
                    fishMode(board, f, tempoX, tempoY, tempoDistance);
                });
            }
        }

        super.setPositions();

    }

    @Override
    protected void fishBehaviour(Board board, MovingGameElement mvElemOther, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance, int range) {
    }

    @Override
    protected void fishMode(Board board, FixedGameElement fixedElem, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance) {
        calculPossibilities(board, tempoX, tempoY);
        calculDistance(tempoDistance, tempoX, tempoY, fixedElem.getPosX(), fixedElem.getPosY());

        if (Collections.min(tempoDistance) < Collections.min(getDistances())) {
            setDistances(tempoDistance);
            setX_moveOptions(tempoX);
            setY_moveOptions(tempoY);
            index = getDistances().indexOf(Collections.min(getDistances()));
        }
    }
}
