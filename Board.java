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
    private final int B_HEIGHT = 600;
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

        /* HashMap for FixedGameElement */
        for (String p : Insect.getPANEL_POWER()) {
            fixedGameElementImageMap.put(p + "Insect", new ImageIcon("./assets/" + p + "Insect.png"));
        }

        ImageIcon iip = new ImageIcon(Pellet.getPathToImage());
        fixedGameElementImageMap.put("Pellet", iip);

        /* HashMap for MovingGameElement */
        ImageIcon iid = new ImageIcon(Decoration.getPathToImage());
        movingGameElementImageMap.put("Decoration", iid);

        for (String c : Fish.getPANEL_COLOR()) {
            movingGameElementImageMap.put(c + "Fish", new ImageIcon("./assets/" + c + "Fish.png"));
        }
    }

    private void initGame() {
        fixedGameElementList = new ArrayList<>();
        decorationList = new ArrayList<>();
        fishList = new ArrayList<>();
        probabilityOfReproduction = new ArrayList<>();

        int insectCounter = 1;
        int pelletCounter = 1;
        int fishCounter = 4;
        final int DECO_AMOUNT = 4;

        /* Creation of FixedGameElement Object */
        for (int i = 0; i < insectCounter; i++) {
            createNewInsect();
        }
        for (int i = 0; i < pelletCounter; i++) {
            createNewPellet();
        }

        /* Creation of MovingGameElement Object */
        for (int i = 0; i < DECO_AMOUNT; i++) {
            createNewDecoration();
        }
        for (int i = 0; i < fishCounter; i++) {
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
        final int posX = getRandomCoordinateX();
        decorationList.add(new Decoration(posX, getRandomCoordinateDeco(), posX, getRandomTargetYUpDown()));
    }

    private void createNewFish() {
        String colorChoice = Fish.getPANEL_COLOR()[(int) (Math.random() * Fish.getPANEL_COLOR().length)];
        int[] TargetArraySides = getRandomPositionSidesBoard();
        Fish fish = null;
        if (colorChoice.equals("Orange"))
            fish = new OrangeFish(getRandomCoordinate(), getRandomCoordinate(), TargetArraySides[0], TargetArraySides[1]);
        if (colorChoice.equals("Blue"))
            fish = new BlueFish(getRandomCoordinate(), getRandomCoordinate(), TargetArraySides[0], TargetArraySides[1]);
        if (colorChoice.equals("Purple"))
            fish = new PurpleFish(getRandomCoordinate(), getRandomCoordinate(), TargetArraySides[0], TargetArraySides[1]);
        if (colorChoice.equals("Red"))
            fish = new RedFish(getRandomCoordinate(), getRandomCoordinate(), TargetArraySides[0], TargetArraySides[1]);

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

    private int getRandomCoordinate() {
        return (int) (Math.random() * (B_WIDTH - DOT_SIZE));
    }

    private int getRandomCoordinateDeco() {
        return (int) (Math.random() * (B_WIDTH - DECO_WIDTH));
    }

    private int getRandomCoordinateX() {
        return (int) (Math.random() * (B_WIDTH - DOT_SIZE));
    }

    private int[] getRandomPositionSidesBoard() {
        int randPos = getRandomCoordinate();
        // 0 = (B_WIDTH - B_WIDTH) OU (B_HEIGHT - B_HEIGHT) => IntelliJIDEA me demande de changer si je ne met pas le 0.
        int[][] tabPos = {{randPos, 0}, {B_WIDTH, randPos}, {randPos, B_HEIGHT}, {0, randPos}};
        return tabPos[(int) (Math.random() * tabPos.length)];
    }

    private int getRandomTargetYUpDown() {
        // 0 = (B_WIDTH - B_WIDTH) OU (B_HEIGHT - B_HEIGHT) => IntelliJIDEA me demande de changer si je ne met pas le 0.
        int[] arrayUpDown = {0, (B_HEIGHT - DECO_HEIGHT)};
        int indexArray = (int) (Math.random() * arrayUpDown.length);
        return arrayUpDown[indexArray];
    }

    public void changeTargets(Fish fish) {
        int[] randTarget = getRandomPositionSidesBoard();
        fish.setTarget_x(randTarget[0]);
        fish.setTarget_y(randTarget[1]);
    }

    private void move() {
        fishList.forEach(f -> f.move(this));
        decorationList.forEach(d -> d.move(this));
    }

    public boolean isValidPosition(MovingGameElement movingObject, int pos_x, int pos_y) {
        boolean isPositionValid = true;

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

    private void checkFixedGameElementCollision() {
        int void_x = -1 * B_WIDTH;
        int void_y = -1 * B_HEIGHT;

        for (FixedGameElement fixedElem : fixedGameElementList) {
            for (Fish fish : fishList) {
                if ((fish.getPos_x() >= fixedElem.getPosX() - (DOT_SIZE) && fish.getPos_x() <= fixedElem.getPosX() + (DOT_SIZE)) && (fish.getPos_y() >= fixedElem.getPosY() - (DOT_SIZE) && fish.getPos_y() <= fixedElem.getPosY() + (DOT_SIZE))) {
                    fixedElem.handleCollision(fish, void_x, void_y);
                }
            }
        }
    }

    private void stopSpeedFishes(String fishType, int delay) {
        ArrayList<Fish> otherFish = (ArrayList<Fish>) fishList.stream().filter(fish -> !fish.getClass().getSimpleName().equals(fishType)).collect(Collectors.toList());

        var timer = new Timer(delay, e -> {
            Timer timerSpeed = (Timer) e.getSource();
            timerSpeed.stop();
            otherFish.forEach(fish -> fish.setSpeed(Fish.INIT_SPEED));
        });

        timer.start();
        otherFish.forEach(fish -> fish.setSpeed(0));
        fishList.stream().filter(fish -> fish.getClass().getSimpleName().equals(fishType)).forEach(fish -> fish.setSpeed(Fish.INIT_SPEED));
    }

    private void checkFishCollision() {
        checkReproduction();
        redFishEatsOtherFishes();
    }

    private void redFishEatsOtherFishes() {
        ArrayList<Fish> redFishes = (ArrayList<Fish>) fishList.stream().filter(fish -> fish instanceof RedFish).collect(Collectors.toList());

        fishList.removeIf(fishOther -> {
            if (fishOther instanceof RedFish) return false;
            return redFishes.stream().anyMatch(fishRed -> (fishOther.getPos_x() >= fishRed.getPos_x() - (DOT_SIZE / 2) && fishOther.getPos_x() <= fishRed.getPos_x() + (DOT_SIZE / 2)) && (fishOther.getPos_y() >= fishRed.getPos_y() - (DOT_SIZE / 2) && fishOther.getPos_y() <= fishRed.getPos_y() + (DOT_SIZE / 2)));
        });
        probabilityOfReproduction.removeIf(value -> !value);
    }

    public void checkReproduction() {
        ArrayList<Fish> copyMovingList = new ArrayList<>(fishList);

        int count = 0;
        for (Fish fish : copyMovingList) {
            for (Fish secondFish : copyMovingList) {
                if ((fish.getClass().getSimpleName().equals(secondFish.getClass().getSimpleName())) &&
                        (fish != secondFish) &&
                        (secondFish.getPos_x() >= fish.getPos_x() - (DOT_SIZE / 2) && secondFish.getPos_x() <= fish.getPos_x() + (DOT_SIZE / 2)) && (secondFish.getPos_y() >= fish.getPos_y() - (DOT_SIZE / 2) && secondFish.getPos_y() <= fish.getPos_y() + (DOT_SIZE / 2))) {

                    int sizeOfList = probabilityOfReproduction.size();
                    int randomValueOfReproductionList = (int) (Math.random() * sizeOfList);
                    boolean retrievedValueOfReproductionList = probabilityOfReproduction.get(randomValueOfReproductionList);

                    if (retrievedValueOfReproductionList) {
                        fishList.remove(fish);
                        fishList.remove(secondFish);

                        count++;
                        if (count % 2 == 0) {
                            for (int i = 0; i < 3; i++) {
                                int[] TargetArraySides = getRandomPositionSidesBoard();
                                if (fish instanceof OrangeFish)
                                    fishList.add(new OrangeFish(getRandomCoordinate(), getRandomCoordinate(), TargetArraySides[0], TargetArraySides[1]));
                                if (fish instanceof BlueFish)
                                    fishList.add(new BlueFish(getRandomCoordinate(), getRandomCoordinate(), TargetArraySides[0], TargetArraySides[1]));
                                if (fish instanceof PurpleFish)
                                    fishList.add(new PurpleFish(getRandomCoordinate(), getRandomCoordinate(), TargetArraySides[0], TargetArraySides[1]));
                                if (fish instanceof RedFish)
                                    fishList.add(new RedFish(getRandomCoordinate(), getRandomCoordinate(), TargetArraySides[0], TargetArraySides[1]));

                                for (int probability = 0; probability < Fish.getPANEL_COLOR().length; probability++)
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

    private void checkTemperature() {
        fishList.stream()
                .filter(fish -> fish instanceof RedFish)
                .forEach(fish -> fish.setSpeed(getBackground() == Color.cyan ? (Fish.INIT_SPEED - 3) : (getBackground() == Color.pink ? (Fish.INIT_SPEED + 3) : (Fish.INIT_SPEED))));
    }

    public static int amountOfOrangeFish() {
        return (int) fishList.stream().filter(fish -> fish instanceof OrangeFish).count();
    }

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
                stopSpeedFishes("redFish", hugeDelay);
            if (key == KeyEvent.VK_B)
                stopSpeedFishes("blueFish", hugeDelay);
            if (key == KeyEvent.VK_M)
                stopSpeedFishes("purpleFish", hugeDelay);
            if (key == KeyEvent.VK_O)
                stopSpeedFishes("orangeFish", hugeDelay);

        }
    }
}
