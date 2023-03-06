import java.util.ArrayList;
import java.util.Collections;

public class BlueFish extends Fish{

    public BlueFish(int pos_x, int pos_y, int target_x,  int target_y) {
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

        for (MovingGameElement mvElemOther : board.movingGameElementList) {
            ArrayList<Integer> tempoX = new ArrayList<Integer>();
            ArrayList<Integer> tempoY = new ArrayList<Integer>();
            ArrayList<Double> tempoDistance = new ArrayList<Double>();


            if (this != mvElemOther && (mvElemOther.getClass().getSimpleName().equals("BlueFish") || mvElemOther.getClass().getSimpleName().equals("PurpleFish"))) {
                fishBehaviour(board, mvElemOther, tempoX, tempoY, tempoDistance, RANGE_DISTANCE);
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
}