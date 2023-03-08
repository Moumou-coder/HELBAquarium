import java.util.ArrayList;

public abstract class Fish extends MovingGameElement {
//        private static final String[] PANEL_COLOR = {"Orange", "Red", "Blue", "Purple"}; /* todo : limitations si nouveau poisson ???  */
        private static final String[] PANEL_COLOR = {"Orange"}; /* todo : limitations si nouveau poisson ???  */
    protected int index; /* todo : classes filles pas ici  */
    protected final int RANGE_DISTANCE = 900;
    private String targetType;
    private boolean isModeActivated = false;


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

    @Override
    public abstract void move(Board board);

    protected abstract void fishBehaviour(Board board, MovingGameElement mvElemOther, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance, int range);
    protected abstract void reproductionMode(Board board, MovingGameElement mvElemOther, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance);
    protected abstract void fishMode(Board board, FixedGameElement fixedElem, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance);
//    protected void fishMode(String typeMode){
//
//    }

    protected void setPositions() {
        setPos_x(getX_moveOptions().get(index));
        setPos_y(getY_moveOptions().get(index));
    }

    public double getDistance(int pos_x0, int pos_y0, int pos_x1, int pos_y1) {
        int x_dist = pos_x1 - pos_x0;
        int y_dist = pos_y1 - pos_y0;
        return Math.sqrt(Math.pow(x_dist, 2) + Math.pow(y_dist, 2));
    }

    public void calculPossibilities(Board board, ArrayList<Integer> arrayListX, ArrayList<Integer> arrayListY) {
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