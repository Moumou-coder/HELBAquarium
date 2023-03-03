import java.util.ArrayList;
import java.util.List;

public abstract class MovingGameElement {
    private int pos_x;
    private int pos_y;
    private int target_x;
    private int target_y;
    /* todo : ça ne devrait plus etre ici car ce n est pas la meme chose pour tous les objets en mouvements... */
    private ArrayList<Integer> x_moveOptions;
    private  ArrayList<Integer> y_moveOptions;
    private  ArrayList<Double> distances;
    private int speed;

    public MovingGameElement(int pos_x, int pos_y, int target_x, int target_y, int speed) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.target_x = target_x;
        this.target_y = target_y;
        this.speed = speed;
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
    

}