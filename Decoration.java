import java.util.ArrayList;
import java.util.Collections;

public class Decoration extends MovingGameElement {

    public Decoration(int pos_x, int pos_y, int target_x, int target_y) {
        super(pos_x, pos_y, target_x, target_y);
    }

    public static String getPathToImage() {
        return "./assets/decoration.png";
    }

    @Override
    public void move(Board board) {
        super.setX_moveOptions(new ArrayList<Integer>());
        super.setY_moveOptions(new ArrayList<Integer>());
        super.setDistances(new ArrayList<Double>());

        calculPossibilities(board, getX_moveOptions(), getY_moveOptions());
        calculDistance(getDistances(), getX_moveOptions(), getY_moveOptions(), getTarget_x(), getTarget_y());
        int min_index = getDistances().indexOf(Collections.min(getDistances()));

        setPositions(min_index);
    }

    private void setPositions(int min_index) {
        setPos_x(getX_moveOptions().get(min_index));
        setPos_y(getY_moveOptions().get(min_index));
    }
}
