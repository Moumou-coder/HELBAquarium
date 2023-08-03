import java.util.ArrayList;
import java.util.List;

public abstract class MovingGameElement {
    private int pos_x;
    private int pos_y;
    private int target_x;
    private int target_y;
    private ArrayList<Integer> x_moveOptions;
    private ArrayList<Integer> y_moveOptions;
    private ArrayList<Double> distances;
    private int speed;
    public static final int INIT_SPEED = 7;

    public MovingGameElement(int pos_x, int pos_y, int target_x, int target_y) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.target_x = target_x;
        this.target_y = target_y;
        this.speed = INIT_SPEED;
    }

    public int getPos_x() {
        return pos_x;
    }

    public void setPos_x(int pos_x) {
        this.pos_x = pos_x;
    }

    public int getPos_y() {
        return pos_y;
    }

    public void setPos_y(int pos_y) {
        this.pos_y = pos_y;
    }

    public int getTarget_x() {
        return target_x;
    }

    public void setTarget_x(int target_x) {
        this.target_x = target_x;
    }

    public int getTarget_y() {
        return target_y;
    }

    public void setTarget_y(int target_y) {
        this.target_y = target_y;
    }

    public ArrayList<Integer> getX_moveOptions() {
        return x_moveOptions;
    }

    public void setX_moveOptions(ArrayList<Integer> x_moveOptions) {
        this.x_moveOptions = x_moveOptions;
    }

    public ArrayList<Integer> getY_moveOptions() {
        return y_moveOptions;
    }

    public void setY_moveOptions(ArrayList<Integer> y_moveOptions) {
        this.y_moveOptions = y_moveOptions;
    }

    public ArrayList<Double> getDistances() {
        return distances;
    }

    public void setDistances(ArrayList<Double> distances) {
        this.distances = distances;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public abstract void move(Board board);

    /* Ces méthodes aident à déterminer les mouvements entre différents objets
    * en calculant le chemin plus rapide à prendre pour l'objet en mouvement par rapport aux distances entre ses positions et la position de la cible. */
    protected double getDistance(int pos_x0, int pos_y0, int pos_x1, int pos_y1) {
        int x_dist = pos_x1 - pos_x0;
        int y_dist = pos_y1 - pos_y0;
        return Math.sqrt(Math.pow(x_dist, 2) + Math.pow(y_dist, 2));
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
    protected void calculDistance(ArrayList<Double> arraylistDistance, ArrayList<Integer> arrayListX, ArrayList<Integer> arrayListY, int targetX, int targetY) {
        for (int i = 0; i < arrayListX.size(); i++) {
            double distance = getDistance(targetX, targetY, arrayListX.get(i), arrayListY.get(i));
            arraylistDistance.add(distance);
        }
    }
}
