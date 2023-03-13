import java.util.ArrayList;
import java.util.Collections;

public class RedFish extends Fish {
    private int index_position;

    public RedFish(int pos_x, int pos_y, int target_x, int target_y) {
        super(pos_x, pos_y, target_x, target_y);
    }

    @Override
    public void move(Board board) {
        setMoveAndDistances();
        defaultFishBehaviour(board);

        if (isModeActivated()) {
            if (getTargetType().equals(Insect.class.getSimpleName()) || getTargetType().equals(Pellet.class.getSimpleName())) {
                board.fixedGameElementList.stream().filter(f -> f.getClass().getSimpleName().equals(getTargetType())).forEach(f -> {
                    initTempoArrayList();
                    fishMode(board, f, tempoX, tempoY, tempoDistance);
                });
            }
            if(getTargetType().equals("ReproductionMode")){
                Board.fishList.stream().filter(f -> f != this && f.getClass().getSimpleName().equals(this.getClass().getSimpleName())).forEach(m -> {
                    initTempoArrayList();
                    reproductionMode(board, m, tempoX, tempoY, tempoDistance);
                });
            }
        }else{
            for (MovingGameElement mvElemOther : Board.fishList) {
                initTempoArrayList();
                if (!mvElemOther.getClass().getSimpleName().equals("RedFish")) {
                    fishBehaviour(board, mvElemOther, tempoX, tempoY, tempoDistance, RANGE_DISTANCE);
                }
            }
        }

        super.setPositions();
    }

    @Override
    protected void fishBehaviour(Board board, MovingGameElement mvElemOther, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance, int range) {
        initCalculation(board, tempoX, tempoY, tempoDistance, mvElemOther.getPos_x(), mvElemOther.getPos_y());
        updateDistancesAndMovementWithMinIndex(Collections.min(tempoDistance) < RANGE_DISTANCE && Collections.min(tempoDistance) < Collections.min(getDistances()), tempoDistance, tempoX, tempoY);
    }

    @Override
    protected void reproductionMode(Board board, MovingGameElement mvElemOther, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance) {
        initCalculation(board, tempoX, tempoY, tempoDistance, mvElemOther.getPos_x(), mvElemOther.getPos_y());
        updateDistancesAndMovementWithMinIndex(Collections.min(tempoDistance) < Collections.min(getDistances()), tempoDistance, tempoX, tempoY);
    }

    @Override
    protected void fishMode(Board board, FixedGameElement fixedElem, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance) {
        initCalculation(board, tempoX, tempoY, tempoDistance, fixedElem.getPosX(), fixedElem.getPosY());
        updateDistancesAndMovementWithMinIndex(Collections.min(tempoDistance) < Collections.min(getDistances()), tempoDistance, tempoX, tempoY);
    }
}
