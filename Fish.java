import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

public abstract class Fish extends MovingGameElement {
    protected int index;
    protected final int RANGE_DISTANCE = 100;
    private int initSpeed = 7;

    public Fish(int pos_x, int pos_y, int target_x, int target_y, int speed) {
        super(pos_x, pos_y, target_x, target_y, speed);
    }

    public abstract void move(Board board);

    protected void setInitialTargets(Board board) {
        super.setX_moveOptions(new ArrayList<Integer>());
        super.setY_moveOptions(new ArrayList<Integer>());
        super.setDistances(new ArrayList<Double>());

        calculPossibilities(board, getX_moveOptions(), getY_moveOptions());
        calculDistance(getDistances(), getX_moveOptions(), getY_moveOptions(), getTarget_x(), getTarget_y());
        index = getDistances().indexOf(Collections.min(getDistances()));
    }

    protected void setPositions() {
        setPos_x(getX_moveOptions().get(index));
        setPos_y(getY_moveOptions().get(index));
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

    public double getDistance(int pos_x0, int pos_y0, int pos_x1, int pos_y1) {
        int x_dist = pos_x1 - pos_x0;
        int y_dist = pos_y1 - pos_y0;
        return Math.sqrt(Math.pow(x_dist, 2) + Math.pow(y_dist, 2));
    }

    public void calculDistance(ArrayList<Double> arraylistDistance, ArrayList<Integer> arrayListX, ArrayList<Integer> arrayListY, int targetX, int targetY) {
        for (int i = 0; i < arrayListX.size(); i++) {
            double distance = getDistance(targetX, targetY, arrayListX.get(i), arrayListY.get(i));
            arraylistDistance.add(distance);
        }
    }

}

/* todo : lorsque les bleu se rencontrent et que la probabilité est faible, il faut juste les téléportés à une position aléatoire */