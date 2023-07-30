import java.util.ArrayList;
import java.util.Collections;

public abstract class Fish extends MovingGameElement {
    private static final String[] PANEL_COLOR = {"Orange", "Red", "Blue", "Purple"}; /* todo : limitations si nouveau poisson ??? - réponse : fichier texte et le programme reprendra depuis le fichier texte et injecter les poissons en créant des classes en fonction de ce qu'il y a dans le .txt au runtime = injection dependace   */
    protected int index; /* todo : classes filles pas ici  */
    protected final int RANGE_DISTANCE = 900;
    private String targetType;
    private boolean isModeActivated = false;

    public Fish(int pos_x, int pos_y, int target_x, int target_y) {
        super(pos_x, pos_y, target_x, target_y);
    }

    public static String[] getPANEL_COLOR() {
        return PANEL_COLOR;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public boolean isModeActivated() {
        return isModeActivated;
    }

    public void setModeActivated(boolean modeActivated) {
        isModeActivated = modeActivated;
    }

    public abstract void move(Board board);

    protected abstract void fishBehaviour(Board board, MovingGameElement mvElemOther, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance, int range);

    protected void setPositions() {
        setPos_x(getX_moveOptions().get(index));
        setPos_y(getY_moveOptions().get(index));
    }

    //initiationFishChild
    protected void initFishBehaviour(Board board) {
        setX_moveOptions(new ArrayList<Integer>());
        setY_moveOptions(new ArrayList<Integer>());
        setDistances(new ArrayList<Double>());

        calculPossibilities(board, getX_moveOptions(), getY_moveOptions());
        calculDistance(getDistances(), getX_moveOptions(), getY_moveOptions(), getTarget_x(), getTarget_y());
        index = getDistances().indexOf(Collections.min(getDistances()));
    }

    //reproduction
    protected void reproductionMode(Board board, MovingGameElement mvElemOther, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance) {
        calculPossibilities(board, tempoX, tempoY);
        calculDistance(tempoDistance, tempoX, tempoY, mvElemOther.getPos_x(), mvElemOther.getPos_y());

        if (Collections.min(tempoDistance) < Collections.min(getDistances())) {
            setDistances(tempoDistance);
            setX_moveOptions(tempoX);
            setY_moveOptions(tempoY);
            index = getDistances().indexOf(Collections.min(getDistances()));
        }
    }

    //isModeActivated
    protected void handleModeActivation(Board board) {
        if (getTargetType().equals(Insect.class.getSimpleName()) || getTargetType().equals(Pellet.class.getSimpleName())) {
            board.fixedGameElementList.stream().filter(f -> f.getClass().getSimpleName().equals(getTargetType())).forEach(f -> {
                ArrayList<Integer> tempoX = new ArrayList<Integer>();
                ArrayList<Integer> tempoY = new ArrayList<Integer>();
                ArrayList<Double> tempoDistance = new ArrayList<Double>();
                fishMode(board, f, tempoX, tempoY, tempoDistance);
            });
        }
        if (getTargetType().equals("ReproductionMode")) {
            board.fishList.stream().filter(f -> f != this && f.getClass().getSimpleName().equals(this.getClass().getSimpleName())).forEach(m -> {
                ArrayList<Integer> tempoX = new ArrayList<Integer>();
                ArrayList<Integer> tempoY = new ArrayList<Integer>();
                ArrayList<Double> tempoDistance = new ArrayList<Double>();
                reproductionMode(board, m, tempoX, tempoY, tempoDistance);
            });
        }
    }

    //fishMode
    private void fishMode(Board board, FixedGameElement fixedElem, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance) {
        calculPossibilities(board, tempoX, tempoY);
        calculDistance(tempoDistance, tempoX, tempoY, fixedElem.getPosX(), fixedElem.getPosY());

        if (Collections.min(tempoDistance) < Collections.min(getDistances())) {
            setDistances(tempoDistance);
            setX_moveOptions(tempoX);
            setY_moveOptions(tempoY);
            index = getDistances().indexOf(Collections.min(getDistances()));
        }
    }
}