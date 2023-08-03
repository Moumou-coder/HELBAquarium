import java.util.ArrayList;
import java.util.Collections;

public abstract class Fish extends MovingGameElement {
    private static final String[] PANEL_COLOR = {"Orange", "Red", "Blue", "Purple"};
    protected int index;
    protected final int RANGE_DISTANCE = 300;
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

    /* Initialise le comportement des classes filles.
     * Cette méthode calcule les positions possibles autour du poisson et détermine les distances entre ces positions et la position cible.
     * L'indice (index) de la distance minimale dans la liste des distances est également calculé pour déterminer le mouvement du poisson pour aller vers sa cible.*/
    protected void initFishBehaviour(Board board) {
        setX_moveOptions(new ArrayList<Integer>());
        setY_moveOptions(new ArrayList<Integer>());
        setDistances(new ArrayList<Double>());

        calculPossibilities(board, getX_moveOptions(), getY_moveOptions());
        calculDistance(getDistances(), getX_moveOptions(), getY_moveOptions(), getTarget_x(), getTarget_y());
        index = getDistances().indexOf(Collections.min(getDistances()));
    }

    /* Cette méthode permet au poisson de chasser une cible spécifique fixe du jeu pendant son mode spécial. */
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

    /* Mode de reproduction du poisson, calcule la trajectoire du mouvement du poisson en fonction des autres poissons du même type (mvElemOther).
     * Exemple : les poissons rouges vont se chercher mutuellement pour ser reproduire */
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

    /* Gère l'activation du mode spécial du poisson.
     * Le comportement dépend du type de cible (targetType) du poisson. Si la cible est un insecte ou une pastille comestible (Pellet),
     * le poisson cherchera ces éléments spécifiques dans fixedGameElementList du Board.
     * S'il s'agit d'un mode de reproduction (targetType est "ReproductionMode"), le poisson cherchera d'autres poissons de son propre type
     * (même classe que lui) dans la liste des poissons (fishList) du Board. */
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
}
