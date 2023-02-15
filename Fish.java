import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

public class Fish extends MovingGameElement {
    private String color;
    private int index;
    private final double RANGE_DISTANCE = 100;

    public Fish(int pos_x, int pos_y, int target_x, int target_y, int speed, String color) {
        super(pos_x, pos_y, target_x, target_y, speed);

        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return getColor() + "Fish";
    }

    public void move(Board board) {
        super.setX_moveOptions(new ArrayList<Integer>());
        super.setY_moveOptions(new ArrayList<Integer>());
        super.setDistances(new ArrayList<Double>());


        calculPossibilities(board, getX_moveOptions(), getY_moveOptions());
        calculDistance(getDistances(), getX_moveOptions(), getY_moveOptions(), getTarget_x(), getTarget_y());
        index = getDistances().indexOf(Collections.min(getDistances()));

        if (getType().equals("orangeFish")) {
            setPositions();
            return; //afin d'éviter de rentrer dans le for pour les autres poissons
        }

        for (MovingGameElement mvElemOther : board.movingGameElementList) {

            ArrayList<Integer> tempoX = new ArrayList<Integer>();
            ArrayList<Integer> tempoY = new ArrayList<Integer>();
            ArrayList<Double> tempoDistance = new ArrayList<Double>();

            if (getType().equals("redFish") && !mvElemOther.getType().equals("redFish")) {
                fishBehaviour(board, mvElemOther, tempoX, tempoY, tempoDistance,false );
            }
            if (getType().equals("blueFish")  && this != mvElemOther && (mvElemOther.getType().equals("blueFish") || mvElemOther.getType().equals("purpleFish"))) {
                fishBehaviour(board, mvElemOther, tempoX, tempoY, tempoDistance,false);
            }
            if (getType().equals("purpleFish") && mvElemOther.getType().equals("redFish")) {
                fishBehaviour(board, mvElemOther, tempoX, tempoY, tempoDistance, true);
            }
        }
        setPositions();
    }

    private void fishBehaviour(Board board, MovingGameElement mvElemOther, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance, boolean isMax) {
        calculPossibilities(board, tempoX, tempoY);
        calculDistance(tempoDistance, tempoX, tempoY, mvElemOther.getPos_x(), mvElemOther.getPos_y());

        boolean replace = isMax ? Collections.max(tempoDistance) < Collections.max(getDistances()) :  Collections.min(tempoDistance) < Collections.min(getDistances());
        if (Collections.min(tempoDistance) < RANGE_DISTANCE && replace) {
            setDistances(tempoDistance);
            setX_moveOptions(tempoX);
            setY_moveOptions(tempoY);
            index = isMax ? getDistances().indexOf(Collections.max(getDistances())):getDistances().indexOf(Collections.min(getDistances()));
        }
    }

    private void setPositions() {
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

    public void triggerAction(Board board) {
    }
}