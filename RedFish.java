import java.util.ArrayList;
import java.util.Collections;

public class RedFish extends Fish {
    public RedFish(int pos_x, int pos_y, int target_x, int target_y) {
        super(pos_x, pos_y, target_x, target_y);
    }

    @Override
    public void move(Board board) {
        super.setX_moveOptions(new ArrayList<Integer>());
        super.setY_moveOptions(new ArrayList<Integer>());
        super.setDistances(new ArrayList<Double>());

        calculPossibilities(board, getX_moveOptions(), getY_moveOptions());
        calculDistance(getDistances(), getX_moveOptions(), getY_moveOptions(), getTarget_x(), getTarget_y());
        super.index = getDistances().indexOf(Collections.min(getDistances()));

        for (MovingGameElement mvElemOther : board.fishList) {
            ArrayList<Integer> tempoX = new ArrayList<Integer>();
            ArrayList<Integer> tempoY = new ArrayList<Integer>();
            ArrayList<Double> tempoDistance = new ArrayList<Double>();

            if (!mvElemOther.getClass().getSimpleName().equals("RedFish")) {
                fishBehaviour(board, mvElemOther, tempoX, tempoY, tempoDistance, RANGE_DISTANCE);
            }
        }

        if (isModeActivated()) {
            if (getTargetType().equals(Insect.class.getSimpleName())) {
                board.fixedGameElementList.stream().filter(f -> f.getClass().getSimpleName().equals(getTargetType())).forEach(f -> {
                    ArrayList<Integer> tempoX = new ArrayList<Integer>();
                    ArrayList<Integer> tempoY = new ArrayList<Integer>();
                    ArrayList<Double> tempoDistance = new ArrayList<Double>();
                    fishMode(board, f, tempoX, tempoY, tempoDistance);
                });
            }
            if (getTargetType().equals(Pellet.class.getSimpleName())) {
                board.fixedGameElementList.stream().filter(f -> f.getClass().getSimpleName().equals(getTargetType())).forEach(f -> {
                    ArrayList<Integer> tempoX = new ArrayList<Integer>();
                    ArrayList<Integer> tempoY = new ArrayList<Integer>();
                    ArrayList<Double> tempoDistance = new ArrayList<Double>();
                    fishMode(board, f, tempoX, tempoY, tempoDistance);
                });
            }
            if(getTargetType().equals("ReproductionMode")){
                board.fishList.stream().filter(f -> f != this && f.getClass().getSimpleName().equals(this.getClass().getSimpleName())).forEach(m -> {
                    ArrayList<Integer> tempoX = new ArrayList<Integer>();
                    ArrayList<Integer> tempoY = new ArrayList<Integer>();
                    ArrayList<Double> tempoDistance = new ArrayList<Double>();
                    reproductionMode(board, m, tempoX, tempoY, tempoDistance);
                });
            }
        }

        super.setPositions();
    }

    @Override
    protected void fishBehaviour(Board board, MovingGameElement mvElemOther, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance, int range) {
        calculPossibilities(board, tempoX, tempoY);
        calculDistance(tempoDistance, tempoX, tempoY, mvElemOther.getPos_x(), mvElemOther.getPos_y());

        if (Collections.min(tempoDistance) < RANGE_DISTANCE && Collections.min(tempoDistance) < Collections.min(getDistances())) {
            setDistances(tempoDistance);
            setX_moveOptions(tempoX);
            setY_moveOptions(tempoY);
            index = getDistances().indexOf(Collections.min(getDistances()));
        }
    }

    @Override
    protected void reproductionMode(Board board, MovingGameElement mvElemOther, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance) {
        calculPossibilities(board, tempoX, tempoY);
        calculDistance(tempoDistance, tempoX, tempoY, mvElemOther.getPos_x(), mvElemOther.getPos_y());

        if (Collections.min(tempoDistance) < Collections.min(getDistances())) {
            setDistances(tempoDistance);
            setX_moveOptions(tempoX);
            setY_moveOptions(tempoY);
            index = getDistances().indexOf(Collections.min(getDistances()));
        }
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
