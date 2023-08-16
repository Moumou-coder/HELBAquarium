import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 600;
    private final int B_HEIGHT = 700;
    private final int DOT_SIZE = 10;
    private boolean inGame = true;
    private final int DECO_WIDTH = 30;
    private final int DECO_HEIGHT = 20;
    private Timer timer;
    private HashMap<String, ImageIcon> fixedGameElementImageMap;
    private HashMap<String, ImageIcon> movingGameElementImageMap;
    public ArrayList<FixedGameElement> fixedGameElementList;
    public static ArrayList<Decoration> decorationList;
    public static ArrayList<Fish> fishList;
    private ArrayList<Boolean> probabilityOfReproduction;
    private final int indexZero = 0;
    private final int indexOne = 1;

    public Board() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setBackground(Color.lightGray);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {
        fixedGameElementImageMap = new HashMap<>();
        movingGameElementImageMap = new HashMap<>();

        /* HashMap reprenant le type de l'objet et le chemin des éléments fixes du jeu (String) */
        for (String p : Insect.getPANEL_POWER()) {
            fixedGameElementImageMap.put(p + "Insect", new ImageIcon("./assets/" + p + "Insect.png"));
        }

        ImageIcon iip = new ImageIcon(Pellet.getPathToImage());
        fixedGameElementImageMap.put("Pellet", iip);

        /* HashMap reprenant le type de l'objet et le chemin des éléments mobiles du jeu (String) */
        ImageIcon iid = new ImageIcon(Decoration.getPathToImage());
        movingGameElementImageMap.put("Decoration", iid);

        for (String c : Fish.getPANEL_COLOR()) {
            movingGameElementImageMap.put(c + "Fish", new ImageIcon("./assets/" + c + "Fish.png"));
        }
    }

    private void initGame() {
        /* Initialisation des arraylist et du nombre d'objets qu'on souhaite afficher */
        fixedGameElementList = new ArrayList<>();
        decorationList = new ArrayList<>();
        fishList = new ArrayList<>();
        probabilityOfReproduction = new ArrayList<>();

        int insectCounter = 4;
        int pelletCounter = 4;
        int fishCounter = 4;
        final int DECO_AMOUNT = 4;

        /* Création des objets éléments fixes du jeu en fonction du nombre d'objets qu'on souhaite affiche (initialisé juste dessus) */
        for (int i = indexZero; i < insectCounter; i++) {
            createNewInsect();
        }
        for (int i = indexZero; i < pelletCounter; i++) {
            createNewPellet();
        }

        /* Création des objets éléments mobiles du jeu */
        for (int i = indexZero; i < DECO_AMOUNT; i++) {
            createNewDecoration();
        }
        for (int i = indexZero; i < fishCounter; i++) {
            createNewFish();
        }

        int DELAY = 140;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void createNewInsect() {
        fixedGameElementList.add(new Insect(getRandomCoordinate(), getRandomCoordinate(), Insect.getPANEL_POWER()[(int) (Math.random() * Insect.getPANEL_POWER().length)]));
    }

    private void createNewPellet() {
        fixedGameElementList.add(new Pellet(getRandomCoordinate(), getRandomCoordinate()));
    }

    private void createNewDecoration() {
        final int absolutePositionX = getRandomCoordinateDecoX();
        decorationList.add(new Decoration(absolutePositionX, getRandomCoordinateDecoY(), absolutePositionX, getRandomTargetYUpDown()));
    }

    private void createNewFish() {
        String colorChoice = Fish.getPANEL_COLOR()[(int) (Math.random() * Fish.getPANEL_COLOR().length)];
        int[] TargetArraySides = getRandomPositionSidesBoard();
        Fish fish = null;
        if (colorChoice.equals("Orange"))
            fish = new OrangeFish(getRandomCoordinate(), getRandomCoordinate(), TargetArraySides[indexZero], TargetArraySides[indexOne]);
        if (colorChoice.equals("Blue"))
            fish = new BlueFish(getRandomCoordinate(), getRandomCoordinate(), TargetArraySides[indexZero], TargetArraySides[indexOne]);
        if (colorChoice.equals("Purple"))
            fish = new PurpleFish(getRandomCoordinate(), getRandomCoordinate(), TargetArraySides[indexZero], TargetArraySides[indexOne]);
        if (colorChoice.equals("Red"))
            fish = new RedFish(getRandomCoordinate(), getRandomCoordinate(), TargetArraySides[indexZero], TargetArraySides[indexOne]);

        fishList.add(fish);
        probabilityOfReproduction.add(true);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if (inGame) {
            for (FixedGameElement elem : fixedGameElementList) {
                g.drawImage(fixedGameElementImageMap.get(elem.getType()).getImage(), elem.getPosX(), elem.getPosY(), this);
            }
            for (Decoration elem : decorationList) {
                g.drawImage(movingGameElementImageMap.get(elem.getClass().getSimpleName()).getImage(), elem.getPos_x(), elem.getPos_y(), this);
            }
            for (Fish elem : fishList) {
                g.drawImage(movingGameElementImageMap.get(elem.getClass().getSimpleName()).getImage(), elem.getPos_x(), elem.getPos_y(), this);
            }

            Toolkit.getDefaultToolkit().sync();
        }
    }

    /* méthode retournant un nombre qui servira pour la position aléatoire des poissons, insectes et pilules */
    private int getRandomCoordinate() {
        return (int) (Math.random() * (B_WIDTH - DOT_SIZE));
    }

    /* méthode retournant un nombre pour la position verticale aléatoire des décorations, car la déco a une dimension différente */
    private int getRandomCoordinateDecoY() {
        return (int) (Math.random() * (B_WIDTH - DECO_HEIGHT));
    }

    /* méthode retournant un nombre pour la position horizontale aléatoire des décorations, car la déco a une dimension différente */
    private int getRandomCoordinateDecoX() {
        return (int) (Math.random() * (B_WIDTH - DECO_WIDTH));
    }

    private int[] getRandomPositionSidesBoard() {
        int randPos = getRandomCoordinate();
        // 0 = (B_WIDTH - B_WIDTH) OU (B_HEIGHT - B_HEIGHT) => IntelliJIDEA me demande de changer si je ne met pas le 0.
        int[][] tabPos = {{randPos, 0}, {B_WIDTH, randPos}, {randPos, B_HEIGHT}, {0, randPos}};
        return tabPos[(int) (Math.random() * tabPos.length)];
    }

    /* méthode retournant un arraylist des 2 bouts du board pour que la déco puisse avoir une trajectoire verticale en continu */
    private int getRandomTargetYUpDown() {
        // 0 = (B_WIDTH - B_WIDTH) OU (B_HEIGHT - B_HEIGHT) => IntelliJIDEA me demande de changer si je ne met pas le 0.
        int[] arrayUpDown = {0, (B_HEIGHT - DECO_HEIGHT)};
        int indexArray = (int) (Math.random() * arrayUpDown.length);
        return arrayUpDown[indexArray];
    }

    /* méthode définissant une cible aléatoire pour les poissons au sein du board */
    public void changeTargets(Fish fish) {
        int[] randTarget = getRandomPositionSidesBoard();
        fish.setTarget_x(randTarget[indexZero]);
        fish.setTarget_y(randTarget[indexOne]);
    }

    private void move() {
        fishList.forEach(f -> f.move(this));
        decorationList.forEach(d -> d.move(this));
    }

    /* Cette méthode vérifie la position des éléments mobiles dans le jeu afin d'assurer qu'ils sont bien dans le board
     * ainsi que si un poisson rencontre une décoration afin qu'il ait une nouvelle direction aléatoire */
    public boolean isValidPosition(MovingGameElement movingObject, int pos_x, int pos_y) {
        boolean isPositionValid = true;

        // 0 = (B_WIDTH - B_WIDTH) OU (B_HEIGHT - B_HEIGHT) => IntelliJIDEA me demande de changer si je ne met pas le 0.
        if (movingObject instanceof Decoration) {
            if (pos_y < 0 || pos_y >= (B_HEIGHT - DECO_HEIGHT)) {
                isPositionValid = false;
                movingObject.setTarget_y((pos_y < 0) ? (B_HEIGHT - DECO_HEIGHT) : 0);
            }
        } else {
            if (pos_y < 0 || pos_y >= (B_HEIGHT - DOT_SIZE)) {
                isPositionValid = false;
                changeTargets((Fish) movingObject);
            }
            if (pos_x < 0 || pos_x >= (B_WIDTH - DOT_SIZE)) {
                isPositionValid = false;
                changeTargets((Fish) movingObject);
            }
            for (Decoration deco : decorationList) {
                if ((pos_x >= deco.getPos_x() - DECO_WIDTH && pos_x <= deco.getPos_x() + DECO_WIDTH) && (pos_y >= deco.getPos_y() - DECO_HEIGHT && pos_y <= deco.getPos_y() + DECO_HEIGHT)) {
                    isPositionValid = false;
                    changeTargets((Fish) movingObject);
                }
            }
        }

        return isPositionValid;
    }

    /* Cette méthode vérifie si un poisson rencontre un élément fixe pour ensuite le retirer du jeu après l'avoir mangé
     * et déclenche une action en faisant appel à la méthode handleCollision de l'objet en question */
    private void checkFixedGameElementCollision() {
        int minusOne = -1;
        int void_x = minusOne * B_WIDTH;
        int void_y = minusOne * B_HEIGHT;

        for (FixedGameElement fixedElem : fixedGameElementList) {
            for (Fish fish : fishList) {
                if ((fish.getPos_x() >= fixedElem.getPosX() - (DOT_SIZE) && fish.getPos_x() <= fixedElem.getPosX() + (DOT_SIZE)) && (fish.getPos_y() >= fixedElem.getPosY() - (DOT_SIZE) && fish.getPos_y() <= fixedElem.getPosY() + (DOT_SIZE))) {

                    /* Augmente temporairement à l'aide d'un Timer la vitesse du poisson lorsque celui-ci mange un type d'insecte.
                     * Le temps d'augmentation est déterminé par la durée d'action de l'insecte fourni par la méthode triggerAction. */
                    if (fixedElem instanceof Insect) {
                        Timer timer = new Timer(((Insect) fixedElem).triggerAction(), actionEvent -> {
                            Timer timerSpeed = (Timer) actionEvent.getSource();
                            timerSpeed.stop();
                            fish.setSpeed(Fish.INIT_SPEED);
                        });
                        timer.start();
                        fish.setSpeed(fixedElem.handleCollision(void_x, void_y));
                    }

                    /* Lorsqu'un poisson mange une pilule, il ralentit sa vitesse en la divisant par trois
                     * pendant un certain nombre de secondes équivalent au nombre de décorations présentes dans l'aquarium (handleCollision) */
                    if (fixedElem instanceof Pellet) {
                        int divider = 3;
                        var timer = new Timer(fixedElem.handleCollision(void_x, void_y), e -> {
                            Timer timerSpeed = (Timer) e.getSource();
                            timerSpeed.stop();
                            fish.setSpeed(Fish.INIT_SPEED);
                        });

                        timer.start();
                        fish.setSpeed(fish.getSpeed() / divider);
                    }
                }
            }
        }
    }

    /* Cette méthode arrête tous les poissons durant un certain délai à l'aide du timer sauf les types du poisson qu'on souhaite qu'il continue à nager */
    private void stopSpeedFishes(String fishType, int delay) {
        ArrayList<Fish> otherFish = (ArrayList<Fish>) fishList.stream().filter(fish -> !fish.getClass().getSimpleName().equals(fishType)).collect(Collectors.toList());
        int speedNull = 0;

        var timer = new Timer(delay, e -> {
            Timer timerSpeed = (Timer) e.getSource();
            timerSpeed.stop();
            otherFish.forEach(fish -> fish.setSpeed(Fish.INIT_SPEED));
        });

        timer.start();
        otherFish.forEach(fish -> fish.setSpeed(speedNull));
        fishList.stream().filter(fish -> fish.getClass().getSimpleName().equals(fishType)).forEach(fish -> fish.setSpeed(Fish.INIT_SPEED));
    }

    private void checkFishCollision() {
        checkReproduction();
        redFishEatsOtherFishes();
    }

    /* Cette méthode récupère tous les poissons rouges dans une liste et tous les autres poissons dans une autre liste
     * afin de vérifier si un poisson rouge rencontre un poisson d'une autre couleur, celui-ci le mange, ce qui a pour effet de le faire disparaitre de l'aquarium. */
    private void redFishEatsOtherFishes() {
        ArrayList<Fish> redFishes = (ArrayList<Fish>) fishList.stream().filter(fish -> fish instanceof RedFish).collect(Collectors.toList());
        int divider = 2;

        fishList.removeIf(fishOther -> {
            if (fishOther instanceof RedFish) return false;
            return redFishes.stream().anyMatch(fishRed -> (fishOther.getPos_x() >= fishRed.getPos_x() - (DOT_SIZE / divider) && fishOther.getPos_x() <= fishRed.getPos_x() + (DOT_SIZE / divider)) && (fishOther.getPos_y() >= fishRed.getPos_y() - (DOT_SIZE / divider) && fishOther.getPos_y() <= fishRed.getPos_y() + (DOT_SIZE / divider)));
        });
        probabilityOfReproduction.removeIf(value -> !value);
    }

    /* Cette méthode vérifie si deux poissons de la même espèce se croisent alors ils se reproduisent mais,
     * le résultat de la reproduction dépend de la probabilité définie dans la liste probabilityOfReproduction.
     * En fonction de la probabilité, soit il y a reproduction donc trois nouveaux poissons ou bien les deux poissons ont une nouvelle position aléatoire. */
    public void checkReproduction() {
        ArrayList<Fish> copyMovingList = new ArrayList<>(fishList);

        int count = 0;
        int divider = 2;
        int moduloZero = 0;
        int minIndex = 0;
        int maxIndex = 3;
        for (Fish fish : copyMovingList) {
            for (Fish secondFish : copyMovingList) {
                if ((fish.getClass().getSimpleName().equals(secondFish.getClass().getSimpleName())) &&
                        (fish != secondFish) &&
                        (secondFish.getPos_x() >= fish.getPos_x() - (DOT_SIZE / divider) && secondFish.getPos_x() <= fish.getPos_x() + (DOT_SIZE / divider)) && (secondFish.getPos_y() >= fish.getPos_y() - (DOT_SIZE / divider) && secondFish.getPos_y() <= fish.getPos_y() + (DOT_SIZE / divider))) {

                    int sizeOfList = probabilityOfReproduction.size();
                    int randomValueOfReproductionList = (int) (Math.random() * sizeOfList);
                    boolean retrievedValueOfReproductionList = probabilityOfReproduction.get(randomValueOfReproductionList);

                    if (retrievedValueOfReproductionList) {
                        fishList.remove(fish);
                        fishList.remove(secondFish);

                        count++;
                        if (count % divider == moduloZero) {
                            for (int i = minIndex; i < maxIndex; i++) {
                                int[] TargetArraySides = getRandomPositionSidesBoard();
                                if (fish instanceof OrangeFish)
                                    fishList.add(new OrangeFish(getRandomCoordinate(), getRandomCoordinate(), TargetArraySides[0], TargetArraySides[1]));
                                if (fish instanceof BlueFish)
                                    fishList.add(new BlueFish(getRandomCoordinate(), getRandomCoordinate(), TargetArraySides[0], TargetArraySides[1]));
                                if (fish instanceof PurpleFish)
                                    fishList.add(new PurpleFish(getRandomCoordinate(), getRandomCoordinate(), TargetArraySides[0], TargetArraySides[1]));
                                if (fish instanceof RedFish)
                                    fishList.add(new RedFish(getRandomCoordinate(), getRandomCoordinate(), TargetArraySides[0], TargetArraySides[1]));

                                for (int probability = minIndex; probability < Fish.getPANEL_COLOR().length; probability++)
                                    probabilityOfReproduction.add(false);
                            }
                        }
                    } else {
                        fish.setPos_x(getRandomCoordinate());
                        fish.setPos_y(getRandomCoordinate());
                        secondFish.setPos_x(getRandomCoordinate());
                        secondFish.setPos_y(getRandomCoordinate());
                    }

                }
            }
        }
    }

    /* cette méthode agit sur la vitesse des poissons, en fonction de la couleur de fond les poissons sont plus lents, normaux ou rapides */
    private void checkTemperature() {
        int speedVariation = 3;
        fishList.stream()
                .filter(fish -> fish instanceof RedFish)
                .forEach(fish -> fish.setSpeed(getBackground() == Color.cyan ? (Fish.INIT_SPEED - speedVariation) : (getBackground() == Color.pink ? (Fish.INIT_SPEED + speedVariation) : (Fish.INIT_SPEED))));
    }

    /* Fonctionnalité supplémentaire
     *  Cette méthode met en arrêt tous les objets en mouvement et enlève tous les éléments fixes du jeu pendant une durée limitée telle la tombée de la nuit avant le levé du jour */
    private void nightTime() {
        int sleepTime = 0;
        int tenSecond = 10000;
        int newFixedElem = 3;

        var timer = new Timer(tenSecond, e -> {
            Timer timerSpeed = (Timer) e.getSource();
            timerSpeed.stop();
            setBackground(Color.lightGray);
            fishList.forEach(f -> f.setSpeed(Fish.INIT_SPEED));
            decorationList.forEach(d -> d.setSpeed(Decoration.INIT_SPEED));
            for (int i = indexZero; i < newFixedElem; i++) {
                createNewInsect();
                createNewPellet();
            }
        });

        timer.start();
        fishList.forEach(f -> f.setSpeed(sleepTime));
        decorationList.forEach(d -> d.setSpeed(sleepTime));
        fixedGameElementList.clear();
    }

    public static int amountOfOrangeFish() {
        return (int) fishList.stream().filter(fish -> fish instanceof OrangeFish).count();
    }

    /* Cette méthode est appelée lorsque l'utilisateur appuie sur des touches spécifiques.
     * Une fois que le mode est activé, les poissons peuvent changer leur comportement en fonction de la cible spécifiée.
     * Ceci s'applique à tous les poissons peu importe la couleur. */
    private static void triggerMode(String type) {
        for (Fish fish : fishList) {
            fish.setTargetType(type);
            fish.setModeActivated(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkFixedGameElementCollision();
            checkFishCollision();
            amountOfOrangeFish();
            move();
        }
        repaint();
    }

    /* La classe TAdapter s'occupe du traitement des touches du clavier appuyé par l'utilisateur.
     * Chaque touche est liée à une action spécifique qui déclenche certaines fonctionnalités. */
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            int hugeDelay = 999999999;

            if (key == KeyEvent.VK_NUMPAD0) {
                timer.stop();
                setBackground(Color.lightGray);
                initGame();
            }
            if (key == KeyEvent.VK_NUMPAD1) {
                setBackground(Color.cyan);
                checkTemperature();
            }
            if (key == KeyEvent.VK_NUMPAD2) {
                setBackground(Color.lightGray);
                checkTemperature();
            }
            if (key == KeyEvent.VK_NUMPAD3) {
                setBackground(Color.pink);
                checkTemperature();
            }
            /* Fonctionnalité supplémentaire */
            if (key == KeyEvent.VK_N) {
                setBackground(Color.darkGray);
                nightTime();
            }
            if (key == KeyEvent.VK_NUMPAD4)
                createNewInsect();
            if (key == KeyEvent.VK_NUMPAD5)
                createNewPellet();
            if (key == KeyEvent.VK_NUMPAD6) {
                triggerMode(Insect.class.getSimpleName());
            }
            if (key == KeyEvent.VK_NUMPAD7) {
                triggerMode(Pellet.class.getSimpleName());
            }
            if (key == KeyEvent.VK_NUMPAD8) {
                triggerMode("ReproductionMode");
            }
            if (key == KeyEvent.VK_NUMPAD9)
                createNewFish();
            if (key == KeyEvent.VK_R)
                stopSpeedFishes("RedFish", hugeDelay);
            if (key == KeyEvent.VK_B)
                stopSpeedFishes("BlueFish", hugeDelay);
            if (key == KeyEvent.VK_M)
                stopSpeedFishes("PurpleFish", hugeDelay);
            if (key == KeyEvent.VK_O)
                stopSpeedFishes("OrangeFish", hugeDelay);

        }
    }
}
