import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fish extends MovingGameElement {
    private String color;

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

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int test_pos_x = getPos_x() + i * getSpeed();
                int test_pos_y = getPos_y() + j * getSpeed();
                if (board.isValidPosition(this, test_pos_x, test_pos_y)){
                    getX_moveOptions().add(test_pos_x);
                    getY_moveOptions().add(test_pos_y);

                }
            }
        }

        for (int i = 0; i < getX_moveOptions().size(); i++) {
            Double distance = getDistance(getTarget_x(), getTarget_y(), getX_moveOptions().get(i), getY_moveOptions().get(i));
            getDistances().add(distance);
        }

        double min = Collections.min(getDistances());
        int min_index = getDistances().indexOf(min);

        setPos_x(getX_moveOptions().get(min_index));
        setPos_y(getY_moveOptions().get(min_index));
    }


    public double getDistance(int pos_x0, int pos_y0, int pos_x1, int pos_y1) {
        int x_dist = pos_x1 - pos_x0;
        int y_dist = pos_y1 - pos_y0;
        return Math.sqrt(Math.pow(x_dist, 2) + Math.pow(y_dist, 2));
    }

    public void triggerAction(Board board) {

    }

}
