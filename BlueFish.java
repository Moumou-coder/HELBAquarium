import java.util.ArrayList;
import java.util.Collections;

public class BlueFish extends Fish{

    public BlueFish(int pos_x, int pos_y, int target_x,  int target_y, int speed) {
        super(pos_x, pos_y, target_x, target_y, speed);
    }

    @Override
    public void move(Board board) {
        super.setX_moveOptions(new ArrayList<Integer>());
        super.setY_moveOptions(new ArrayList<Integer>());
        super.setDistances(new ArrayList<Double>());


        calculPossibilities(board, getX_moveOptions(), getY_moveOptions());
        calculDistance(getDistances(), getX_moveOptions(), getY_moveOptions(), getTarget_x(), getTarget_y());
        super.index = getDistances().indexOf(Collections.min(getDistances()));

        for (MovingGameElement mvElemOther : board.movingGameElementList) {
//
            ArrayList<Integer> tempoX = new ArrayList<Integer>();
            ArrayList<Integer> tempoY = new ArrayList<Integer>();
            ArrayList<Double> tempoDistance = new ArrayList<Double>();


            if (this != mvElemOther && (mvElemOther.getClass().getSimpleName().equals("BlueFish") || mvElemOther.getClass().getSimpleName().equals("PurpleFish"))) {
                super.fishBehaviour(board, mvElemOther, tempoX, tempoY, tempoDistance,false, super.SHORT_RANGE_DISTANCE);
            }
        }
        super.setPositions();
    }
}
