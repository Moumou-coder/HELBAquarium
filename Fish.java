import java.util.ArrayList;
import java.util.Collections;

public abstract class Fish extends MovingGameElement {
//        private static final String[] PANEL_COLOR = {"Orange", "Red", "Blue", "Purple"}; /* todo : limitations si nouveau poisson ???  */
        private static final String[] PANEL_COLOR = {"Red"}; /* todo : limitations si nouveau poisson ???  */
    protected int index; /* todo : classes filles pas ici  */
    protected final int RANGE_DISTANCE = 900;
    private String targetType;
    private boolean isModeActivated = false;
    protected ArrayList<Integer> tempoX;
    protected ArrayList<Integer> tempoY;
    protected ArrayList<Double> tempoDistance;


    public Fish(int pos_x, int pos_y, int target_x, int target_y) {
        super(pos_x, pos_y, target_x, target_y);
    }


    public static String[] getPANEL_COLOR() {
        return PANEL_COLOR;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public boolean isModeActivated() {
        return isModeActivated;
    }

    public void setModeActivated(boolean modeActivated) {
        isModeActivated = modeActivated;
    }

    public ArrayList<Integer> getTempoX() {
        return tempoX;
    }

    public void setTempoX(ArrayList<Integer> tempoX) {
        this.tempoX = tempoX;
    }

    public ArrayList<Integer> getTempoY() {
        return tempoY;
    }

    public void setTempoY(ArrayList<Integer> tempoY) {
        this.tempoY = tempoY;
    }

    public ArrayList<Double> getTempoDistance() {
        return tempoDistance;
    }

    public void setTempoDistance(ArrayList<Double> tempoDistance) {
        this.tempoDistance = tempoDistance;
    }

    @Override
    public abstract void move(Board board);

    protected void initTempoArrayList(){
        tempoX = new ArrayList<Integer>();
        tempoY = new ArrayList<Integer>();
        tempoDistance = new ArrayList<Double>();
    }
    protected abstract void fishBehaviour(Board board, MovingGameElement mvElemOther, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance, int range);
    protected abstract void reproductionMode(Board board, MovingGameElement mvElemOther, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance);
    protected abstract void fishMode(Board board, FixedGameElement fixedElem, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance);

    protected void setMoveAndDistances() {
        super.setX_moveOptions(new ArrayList<Integer>());
        super.setY_moveOptions(new ArrayList<Integer>());
        super.setDistances(new ArrayList<Double>());
    }

    protected void defaultFishBehaviour(Board board) {
        calculPossibilities(board, getX_moveOptions(), getY_moveOptions());
        calculDistance(getDistances(), getX_moveOptions(), getY_moveOptions(), getTarget_x(), getTarget_y());
        index = getDistances().indexOf(Collections.min(getDistances()));
    }

    protected void initCalculation(Board board, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance, int mvElemOtherPosX, int mvElemOtherPosY) {
        calculPossibilities(board, tempoX, tempoY);
        calculDistance(tempoDistance, tempoX, tempoY, mvElemOtherPosX, mvElemOtherPosY);
    }

    protected void setPositions() {
        setPos_x(getX_moveOptions().get(index));
        setPos_y(getY_moveOptions().get(index));
    }

    protected double getDistance(int pos_x0, int pos_y0, int pos_x1, int pos_y1) {
        int x_dist = pos_x1 - pos_x0;
        int y_dist = pos_y1 - pos_y0;
        return Math.sqrt(Math.pow(x_dist, 2) + Math.pow(y_dist, 2));
    }

    protected void updateDistancesAndMovementWithMinIndex(boolean tempoDistance, ArrayList<Double> tempoDistance1, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY) {
        if (tempoDistance) {
            setDistances(tempoDistance1);
            setX_moveOptions(tempoX);
            setY_moveOptions(tempoY);
            index = getDistances().indexOf(Collections.min(getDistances()));
        }
    }

    protected void calculPossibilities(Board board, ArrayList<Integer> arrayListX, ArrayList<Integer> arrayListY) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {

                int test_pos_x = getPos_x() + i * getSpeed();
                int test_pos_y = getPos_y() + j * getSpeed();
                if (board.isValidPosition(this, test_pos_x, test_pos_y)) {
                    arrayListX.add(test_pos_x);
                    arrayListY.add(test_pos_y);
                }
            }
        }
    }

    public void calculDistance(ArrayList<Double> arraylistDistance, ArrayList<Integer> arrayListX, ArrayList<Integer> arrayListY, int targetX, int targetY) {
        for (int i = 0; i < arrayListX.size(); i++) {
            double distance = getDistance(targetX, targetY, arrayListX.get(i), arrayListY.get(i));
            arraylistDistance.add(distance);
        }
    }
}