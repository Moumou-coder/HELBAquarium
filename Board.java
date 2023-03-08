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

    private final int B_WIDTH = 800;
    private final int B_HEIGHT = 800;
    private final int DOT_SIZE = 10;
    private final int DELAY = 140;
    private boolean inGame = true;
    private Timer timer;

    private HashMap<String, ImageIcon> fixedGameElementImageMap;
    /* todo : au niveau bonne pratique, puis-je l'initier dans le initGame comme me propose IDE */
    private int insectCounter;
    private int pelletCounter;
    private int decorationCounter;
    private ArrayList<FixedGameElement> fixedGameElementList;

    private HashMap<String, ImageIcon> movingGameElementImageMap;
    private int fishCounter;
    private String colorChoice;
    private int speedFishIncreased;
    public static ArrayList<Fish> movingGameElementList;
    ArrayList<Integer> probabilityOfReproduction = new ArrayList<Integer>();

    private int void_x = -1 * B_WIDTH;
    private int void_y = -1 * B_HEIGHT;

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
        fixedGameElementImageMap = new HashMap<String, ImageIcon>();
        for (String p : Insect.getPANEL_POWER()) {
            fixedGameElementImageMap.put(p + "Insect", new ImageIcon("./assets/" + p + "Insect.png"));
        }
        ImageIcon iip = new ImageIcon(Pellet.getPathToImage());
        fixedGameElementImageMap.put("pellet", iip);

        ImageIcon iid = new ImageIcon(Decoration.getPathToImage());
        fixedGameElementImageMap.put("decoration", iid);

        movingGameElementImageMap = new HashMap<String, ImageIcon>();
        for (String c : Fish.getPANEL_COLOR()) {
            movingGameElementImageMap.put(c + "Fish", new ImageIcon("./assets/" + c + "Fish.png"));
        }
    }

    private void initGame() {

        insectCounter = 0;
        pelletCounter =0;
        decorationCounter = 0;
        fishCounter = 10;

        //List contenant les éléments fixes
        fixedGameElementList = new ArrayList<FixedGameElement>();

        for (int i = 0; i < insectCounter; i++) {
            createNewInsect();
        }
        for (int i = 0; i < pelletCounter; i++) {
            createNewPellet();
        }

        //list contenant les éléments en mouvements
        movingGameElementList = new ArrayList<Fish>();

        for (int i = 0; i < decorationCounter; i++) {
            createNewDecoration();
        }
        for (int i = 0; i < fishCounter; i++) {
            createNewFish();
        }

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void createNewInsect(){
        fixedGameElementList.add(new Insect(getRandomCoordinate(), getRandomCoordinate(),  Insect.getPANEL_POWER()[(int) (Math.random() *  Insect.getPANEL_POWER().length)]));
    }
    private void createNewPellet() {
        fixedGameElementList.add(new Pellet(getRandomCoordinate(), getRandomCoordinate()));
    }

    private void createNewDecoration() {
        fixedGameElementList.add(new Decoration(getRandomCoordinate(), getRandomCoordinate()));
    }

    private void createNewFish() {
        colorChoice = Fish.getPANEL_COLOR()[(int) (Math.random() * Fish.getPANEL_COLOR().length)];
        int[] randomTargetArray = getRandomPositionSidesBoard();
        Fish fish = null;
        if(colorChoice.equals("Orange"))
            fish = new OrangeFish(getRandomCoordinate(), getRandomCoordinate(), randomTargetArray[0], randomTargetArray[1]);
        if(colorChoice.equals("Blue"))
            fish = new BlueFish(getRandomCoordinate(), getRandomCoordinate(), randomTargetArray[0], randomTargetArray[1]);
        if(colorChoice.equals("Purple"))
            fish = new PurpleFish(getRandomCoordinate(), getRandomCoordinate(), randomTargetArray[0], randomTargetArray[1]);
        if(colorChoice.equals("Red"))
            fish = new RedFish(getRandomCoordinate(), getRandomCoordinate(), randomTargetArray[0], randomTargetArray[1]);

        movingGameElementList.add(fish);
        probabilityOfReproduction.add(0);

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
            for (Fish elem : movingGameElementList) {
                g.drawImage(movingGameElementImageMap.get(elem.getClass().getSimpleName()).getImage(), elem.getPos_x(), elem.getPos_y(), this);
            }

            Toolkit.getDefaultToolkit().sync();

        } else {
            gameOver(g);
        }
    }

    /* todo : enlever le gameOver car il n y a plus vraiment de gameOver mtn */
    private void gameOver(Graphics g) {

        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private int getRandomCoordinate() {
        return (int) (Math.random() * (B_WIDTH - DOT_SIZE));
    }
    private int[] getRandomPositionSidesBoard() {
        int randPos = getRandomCoordinate();
        int[][] tabPos = {{randPos, (B_HEIGHT - B_WIDTH)}, {B_WIDTH, randPos}, {randPos, B_HEIGHT}, {(B_WIDTH - B_HEIGHT), randPos}};
        return tabPos[(int) (Math.random() * tabPos.length)];
    }

    public void changeTargets(MovingGameElement mvElem) {
        int[] randTarget = getRandomPositionSidesBoard();
        mvElem.setTarget_x(randTarget[0]);
        mvElem.setTarget_y(randTarget[1]);
    }

    private void move() {
        for (MovingGameElement mvElem : movingGameElementList) {
            mvElem.move(this);
        }
    }

    public boolean isValidPosition(MovingGameElement movElem, int pos_x, int pos_y) {
        boolean isPositionValid = true;
        if (pos_y < 0 || pos_y >= (B_HEIGHT - DOT_SIZE)) {
            isPositionValid = false;
            changeTargets(movElem);
        }
        if (pos_x < 0 || pos_x >= (B_WIDTH - DOT_SIZE)) {
            isPositionValid = false;
            changeTargets(movElem);
        }

        for (FixedGameElement fxElem : fixedGameElementList) {
            if (fxElem.getClass() == Decoration.class) {
                if ((pos_x >= fxElem.getPosX() && pos_x <= fxElem.getPosX() + 3 * DOT_SIZE) && (pos_y >= fxElem.getPosY() && pos_y <= fxElem.getPosY() + 2 * DOT_SIZE)) {
                    isPositionValid = false;
                    changeTargets(movElem);
                }
            }
        }

        return isPositionValid;
    }

    private void checkFixedGameElementCollision() {
        for (FixedGameElement fxElem : fixedGameElementList) {
            for (MovingGameElement mvElem : movingGameElementList) {
                if ((mvElem.getPos_x() >= fxElem.getPosX() - (DOT_SIZE / 2) && mvElem.getPos_x() <= fxElem.getPosX() + (DOT_SIZE / 2)) && (mvElem.getPos_y() >= fxElem.getPosY() - (DOT_SIZE / 2) && mvElem.getPos_y() <= fxElem.getPosY() + (DOT_SIZE / 2))) {
                    if (fxElem instanceof Insect) {
                        speedFishIncreased = 14;
                        fxElem.setPosX(void_x);
                        fxElem.setPosY(void_y);

                        Timer timer = new Timer(((Insect) fxElem).triggerAction(), actionEvent -> {
                            Timer timerSpeed = (Timer) actionEvent.getSource();
                            timerSpeed.stop();
                            mvElem.setSpeed(MovingGameElement.INIT_SPEED);

                        });
                        timer.start();
                        mvElem.setSpeed(speedFishIncreased);

                    }

                    if (fxElem instanceof Pellet) {
                        fxElem.setPosX(void_x);
                        fxElem.setPosY(void_y);
                        int delayToStopFish = 10000;
                        stopSpeedFishes(mvElem.getClass().getSimpleName(), delayToStopFish);
                    }
                }
            }
        }
    }

    private void stopSpeedFishes(String fishType, int delay){
        ArrayList<Fish> otherFish = (ArrayList<Fish>) movingGameElementList.stream().filter(fish -> !fish.getClass().getSimpleName().equals(fishType)).collect(Collectors.toList());

        var timer = new Timer(delay, e -> {
            Timer timerSpeed = (Timer) e.getSource();
            timerSpeed.stop();
            otherFish.forEach(fish -> fish.setSpeed(MovingGameElement.INIT_SPEED));
        });

        timer.start();
        otherFish.forEach(fish -> fish.setSpeed(0));
        movingGameElementList.stream().filter(fish -> fish.getClass().getSimpleName().equals(fishType)).forEach(fish -> fish.setSpeed(MovingGameElement.INIT_SPEED));
    }

    private void checkFishCollision() {
        checkReproduction();
        redFishEatsOtherFishes();

    }
    private void redFishEatsOtherFishes(){
        ArrayList<Fish> redFishes = (ArrayList<Fish>) movingGameElementList.stream().filter(fish -> fish instanceof RedFish).collect(Collectors.toList());

        movingGameElementList.removeIf(fishOther -> {
            if (fishOther instanceof RedFish) return false;
            return redFishes.stream().anyMatch(fishRed -> (fishOther.getPos_x() >= fishRed.getPos_x() - (DOT_SIZE / 2) && fishOther.getPos_x() <= fishRed.getPos_x() + (DOT_SIZE / 2)) && (fishOther.getPos_y() >= fishRed.getPos_y() - (DOT_SIZE / 2) && fishOther.getPos_y() <= fishRed.getPos_y() + (DOT_SIZE / 2)));
        });

        probabilityOfReproduction.removeIf(value -> value == 1);
    }

    /* todo : limiter la reproduction des poissosn en fonction du nombres de poissons */
    public void checkReproduction() {
        ArrayList<Fish> copyMovingList = new ArrayList<>(movingGameElementList);

        int count = 0;
        for (MovingGameElement mvElem1 : copyMovingList) {
            for (MovingGameElement mvElem2 : copyMovingList) {
                if ((mvElem1.getClass().getSimpleName().equals(mvElem2.getClass().getSimpleName())) &&
                        (mvElem1 != mvElem2) &&
                        (mvElem2.getPos_x() >= mvElem1.getPos_x() - (DOT_SIZE / 2) && mvElem2.getPos_x() <= mvElem1.getPos_x() + (DOT_SIZE / 2)) && (mvElem2.getPos_y() >= mvElem1.getPos_y() - (DOT_SIZE / 2) && mvElem2.getPos_y() <= mvElem1.getPos_y() + (DOT_SIZE / 2))) {

                    int sizeOfList = probabilityOfReproduction.size();
                    int randomValueOfReproductionList = (int)(Math.random()*sizeOfList);
                    int retrievedValueOfReproductionList = probabilityOfReproduction.get(randomValueOfReproductionList);

                    if(retrievedValueOfReproductionList == 0){
                        movingGameElementList.remove(mvElem1);
                        movingGameElementList.remove(mvElem2);

                        count++;
                        if (count % 2 == 0) {
                            for (int i = 0; i < 3; i++) {
                                int[] randomTargetArray = getRandomPositionSidesBoard();
                                if(mvElem1 instanceof OrangeFish)
                                    movingGameElementList.add(new OrangeFish(getRandomCoordinate(), getRandomCoordinate(), randomTargetArray[0], randomTargetArray[1]));
                                if(mvElem1 instanceof BlueFish)
                                    movingGameElementList.add(new BlueFish(getRandomCoordinate(), getRandomCoordinate(), randomTargetArray[0], randomTargetArray[1]));
                                if(mvElem1 instanceof PurpleFish)
                                    movingGameElementList.add(new PurpleFish(getRandomCoordinate(), getRandomCoordinate(), randomTargetArray[0], randomTargetArray[1]));
                                if(mvElem1 instanceof RedFish)
                                    movingGameElementList.add(new RedFish(getRandomCoordinate(), getRandomCoordinate(), randomTargetArray[0], randomTargetArray[1]));

                                for(int probability = 0; probability < Fish.getPANEL_COLOR().length; probability++) probabilityOfReproduction.add(1);
                            }
                        }
                    }
                    else{
                        mvElem1.setPos_x(getRandomCoordinate());
                        mvElem1.setPos_y(getRandomCoordinate());
                        mvElem2.setPos_x(getRandomCoordinate());
                        mvElem2.setPos_y(getRandomCoordinate());
                    }

                }
            }
        }
    }

    private void checkTemperature() {
        movingGameElementList.stream()
                .filter(fish -> fish instanceof RedFish)
                .forEach(fish -> fish.setSpeed(getBackground() == Color.cyan ? (MovingGameElement.INIT_SPEED - 3) : (getBackground() == Color.pink ? (MovingGameElement.INIT_SPEED + 3) : (MovingGameElement.INIT_SPEED))));
    }


    public static int amountOfOrangeFish() {
        return (int) movingGameElementList.stream().filter(fish -> fish instanceof OrangeFish).count();
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

            /* todo : voir avoir prof si c'est pas mieux d'utiliser un switch au lieu d'avoir plusieurs if comme ça car une seule variable */
            if (key == KeyEvent.VK_NUMPAD0){
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
            if(key == KeyEvent.VK_NUMPAD5)
                createNewPellet();
            /* ------------------------------------------------------------ */
            /* todo : changement de targets comportement poissons */
            if(key == KeyEvent.VK_NUMPAD6)
                System.out.println("mode ....");
            if(key == KeyEvent.VK_NUMPAD7)
                System.out.println("mode ....");
            if(key == KeyEvent.VK_NUMPAD8)
                System.out.println("mode ....");
            /* ------------------------------------------------------------ */
            if(key == KeyEvent.VK_NUMPAD9)
                createNewFish();
            if(key == KeyEvent.VK_R)
                stopSpeedFishes("redFish", 999999999);
            if(key == KeyEvent.VK_B)
                stopSpeedFishes("blueFish", 999999999);
            if(key == KeyEvent.VK_M)
                stopSpeedFishes("purpleFish", 999999999);
            if(key == KeyEvent.VK_O)
                stopSpeedFishes("orangeFish", 999999999);

        }
    }
}
