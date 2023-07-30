import java.util.ArrayList;
import java.util.Collections;

public class OrangeFish extends Fish {

    private final int initSpeed = 7;

    public OrangeFish(int pos_x, int pos_y, int target_x, int target_y) {
        super(pos_x, pos_y, target_x, target_y);
    }

    @Override
    public void move(Board board) {
        initFishBehaviour(board);

        if (isModeActivated()) {
            handleModeActivation(board);
        }

        super.setPositions();
    }

    @Override
    protected void fishBehaviour(Board board, MovingGameElement mvElemOther, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance, int range) {
    }
}
