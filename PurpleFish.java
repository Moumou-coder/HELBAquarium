import java.util.ArrayList;
import java.util.Collections;

public class PurpleFish extends Fish {
    static String pathToImage = "./assets/purpleFish.png";
    public PurpleFish(int pos_x, int pos_y, int target_x, int target_y, int speed) {
        super(pos_x, pos_y, target_x, target_y, speed);
    }

    @Override
    public void move(Board board) {
        super.setInitialTargets(board);

        for (MovingGameElement mvElemOther : board.movingGameElementList) {
            ArrayList<Integer> tempoX = new ArrayList<Integer>();
            ArrayList<Integer> tempoY = new ArrayList<Integer>();
            ArrayList<Double> tempoDistance = new ArrayList<Double>();


            if (mvElemOther.getClass() == RedFish.class) {
                calculPossibilities(board, tempoX, tempoY);
                calculDistance(tempoDistance, tempoX, tempoY, mvElemOther.getPos_x(), mvElemOther.getPos_y());

                if (Collections.min(tempoDistance) < super.RANGE_DISTANCE && Collections.max(tempoDistance) < Collections.max(getDistances())) {
                    setDistances(tempoDistance);
                    setX_moveOptions(tempoX);
                    setY_moveOptions(tempoY);
                    index = getDistances().indexOf(Collections.max(getDistances()));
                }
            }
        }
        setPositions();
    }

    @Override
    public String getPathToImage() {
        return pathToImage;
    }
}
