import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 800;
    private final int B_HEIGHT = 800;
    private final int DOT_SIZE = 10;
    private final int DELAY = 140;

    private final boolean inGame = true;
    private Timer timer;

    private final String PANEL_POWER[] = {"weak", "medium", "strong"};
    private final String PANEL_COLOR[] = {BlueFish.pathToImage,OrangeFish.pathToImage,PurpleFish.pathToImage,RedFish.pathToImage};
    private HashMap<String, ImageIcon> fixedGameElementImageMap;
    private HashMap<String, ImageIcon> movingGameElementImageMap;

    private ArrayList<FixedGameElement> fixedGameElementList;
    public ArrayList<Fish> movingGameElementList;

    private final int initSpeedFish = 7; //mettre ça dans la classe mere


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
        /* todo: mettre le chemin acces dans les classes meres */

        //L'image de l'icon pour les éléments fixes du jeu
        fixedGameElementImageMap = new HashMap<String, ImageIcon>();
        for (String p : PANEL_POWER) {
            fixedGameElementImageMap.put(p + "Insect", new ImageIcon("./assets/" + p + "Insect.png"));
        }
        ImageIcon iip = new ImageIcon(Pellet.getPathToImage());
        fixedGameElementImageMap.put("pellet", iip);
        ImageIcon iid = new ImageIcon(Decoration.getPathToImage());
        fixedGameElementImageMap.put("decoration", iid);

        //L'image de l'icon pour les éléments en mouvement
        movingGameElementImageMap = new HashMap<String, ImageIcon>();
        for (String c : PANEL_COLOR) {
            String fishName = c.substring(c.lastIndexOf("/") + 1, c.lastIndexOf("."));
            movingGameElementImageMap.put(fishName, new ImageIcon(c));
        }
    }
    private void initGame() {
        //FixeGameElement
        int insectCounter = 5;
        int pelletCounter = 5;
        //MovingGameElement
        int decorationCounter = 4;
        int fishCounter = 10;

        //List contenant les éléments fixes
        fixedGameElementList = new ArrayList<FixedGameElement>();
        for (int i = 0; i < insectCounter; i++) {
            fixedGameElementList.add(new Insect(getRandomCoordinate(), getRandomCoordinate(), PANEL_POWER[i % PANEL_POWER.length]));
        }
        for (int i = 0; i < pelletCounter; i++) {
            fixedGameElementList.add(new Pellet(getRandomCoordinate(), getRandomCoordinate()));
        }
        for (int i = 0; i < decorationCounter; i++) {
            fixedGameElementList.add(new Decoration(getRandomCoordinate(), getRandomCoordinate()));
        }

        //List contenant les poissons
        movingGameElementList = new ArrayList<Fish>();
        for (int i = 0; i < fishCounter; i++) {
            createNewFish();
        }

        /* todo : Demander au prof si c'est ça qu'il veut par rapport au poisson mauve - decoration
            => no, il veut que dès que le jeu se lance la vitesse du mauve varie en fonction du orange et diminuer si orangeFish est mange */
        checkQuantityOfDecorations();


        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void createNewFish() {
        int[] randTarget = getArrayTarget();
        String fishChoice = PANEL_COLOR[(int) (Math.random() * PANEL_COLOR.length)];
        Fish fish;

        switch (fishChoice.substring(fishChoice.lastIndexOf("/") + 1, fishChoice.lastIndexOf("."))) {
            case "blueFish" :
                fish = new BlueFish(getRandomCoordinate(), getRandomCoordinate(), randTarget[0], randTarget[1], initSpeedFish);
                break;
            case "orangeFish" :
                fish = new OrangeFish(getRandomCoordinate(), getRandomCoordinate(), randTarget[0], randTarget[1], initSpeedFish);
                break;
            case "purpleFish" :
                fish = new PurpleFish(getRandomCoordinate(), getRandomCoordinate(), randTarget[0], randTarget[1], initSpeedFish);
                break;
            case "redFish" :
                fish = new RedFish(getRandomCoordinate(), getRandomCoordinate(), randTarget[0], randTarget[1], initSpeedFish);
                break;
            default:
                fish = null;
        }
        movingGameElementList.add(fish);
    }

    private void checkQuantityOfDecorations() {
        int amountOfOrangeFish = (int) movingGameElementList.stream().filter(oFish -> oFish.getClass() == OrangeFish.class).count();
        System.out.println(amountOfOrangeFish);
        /* changer vitesse du poisson mauve */
        movingGameElementList.stream().filter(fish -> fish.getClass() == PurpleFish.class).forEach(fish -> fish.setSpeed(initSpeedFish + amountOfOrangeFish));

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
            for (MovingGameElement elem : movingGameElementList) {
                g.drawImage(movingGameElementImageMap.get(elem.getPathToImage()).getImage(), elem.getPos_x(), elem.getPos_y(), this);
            }

            Toolkit.getDefaultToolkit().sync();

        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {

        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    /* todo: fusionner les deux méthodes checkCollision */
    private void checkFixedGameElementCollision() {
        for (FixedGameElement fxElem : fixedGameElementList) {
            for (MovingGameElement mvElem : movingGameElementList) {
                if ((mvElem.getPos_x() >= fxElem.getPosX() - (DOT_SIZE / 2) && mvElem.getPos_x() <= fxElem.getPosX() + (DOT_SIZE / 2)) && (mvElem.getPos_y() >= fxElem.getPosY() - (DOT_SIZE / 2) && mvElem.getPos_y() <= fxElem.getPosY() + (DOT_SIZE / 2))) {
                    if (fxElem.getClass() == Insect.class) {
                        int speedFishIncreased = 14;
                        fxElem.setPosX(void_x);
                        fxElem.setPosY(void_y);

                        Timer timer = new Timer(((Insect) fxElem).triggerAction(), actionEvent -> {
                            Timer timerSpeed = (Timer) actionEvent.getSource();
                            timerSpeed.stop();
                            mvElem.setSpeed(initSpeedFish);

                        });
                        timer.start();
                        mvElem.setSpeed(speedFishIncreased);

                    }
                    if (fxElem.getClass() == Pellet.class) {
                        fxElem.setPosX(void_x);
                        fxElem.setPosY(void_y);
                        /* todo : fishDelay = le nombre de poissons oranges qu'il y a dans aquarium  => voir si solution comme pour poissons mauves avec stream */
//                        stopSpeedFishes(mvElem, 10000);
                    }
                }
            }
        }
    }

    private void checkFishCollision() {
        checkReproduction();
        ArrayList<Fish> redFishes = (ArrayList<Fish>) movingGameElementList.stream().filter(fish -> fish.getClass() == RedFish.class).collect(Collectors.toList());

        movingGameElementList.removeIf(fishOther -> {
            if (fishOther.getClass() == RedFish.class) return false;
            return redFishes.stream().anyMatch(fishRed -> (fishOther.getPos_x() >= fishRed.getPos_x() - (DOT_SIZE / 2) && fishOther.getPos_x() <= fishRed.getPos_x() + (DOT_SIZE / 2)) && (fishOther.getPos_y() >= fishRed.getPos_y() - (DOT_SIZE / 2) && fishOther.getPos_y() <= fishRed.getPos_y() + (DOT_SIZE / 2)));
        });
    }

    private int getRandomCoordinate() {
        return (int) (Math.random() * (B_WIDTH - DOT_SIZE));
    }

    private void move() {
        for (MovingGameElement mvElem : movingGameElementList) {
            mvElem.move(this);
            System.out.println("speed => " + mvElem.getSpeed());
        }
    }

    /* todo: changer nom méthode et variable plus logique */
    public void changeTargets(MovingGameElement mvElem) {
        int[] randTarget = getArrayTarget();
        mvElem.setTarget_x(randTarget[0]);
        mvElem.setTarget_y(randTarget[1]);
    }

    /* todo : limiter la reproduction des poissosn en fonction du nombres de poissons */
    public void checkReproduction() {
        int count = 0;
//        ArrayList<MovingGameElement> copylist = new ArrayList<>(movingGameElementList);
//
//        for (MovingGameElement mvElem1 : copylist) {
//            for (MovingGameElement mvElem2 : copylist) {
//                if ((Objects.equals(mvElem1.getType(), mvElem2.getType())) &&
//                        (mvElem1 != mvElem2) &&
//                        (mvElem2.getPos_x() >= mvElem1.getPos_x() - (DOT_SIZE / 2) && mvElem2.getPos_x() <= mvElem1.getPos_x() + (DOT_SIZE / 2)) && (mvElem2.getPos_y() >= mvElem1.getPos_y() - (DOT_SIZE / 2) && mvElem2.getPos_y() <= mvElem1.getPos_y() + (DOT_SIZE / 2))) {
//                    movingGameElementList.remove(mvElem1);
//                    movingGameElementList.remove(mvElem2);
//                    count++;
//                    if (count % 2 == 0) {
//                        for (int i = 0; i < 3; i++) {
//                            int[] randTarget = getArrayTarget();
//                            movingGameElementList.add(new Fish(getRandomCoordinate(), getRandomCoordinate(), randTarget[0], randTarget[1], initSpeedFish, mvElem1.getType().substring(0, mvElem2.getType().length() - 4)));
//                        }
//                    }
//                }
//            }
//        }
    }

    private int[] getArrayTarget() {
        int randPos = getRandomCoordinate();
        int[][] tabPos = {{randPos, (B_HEIGHT - B_WIDTH)}, {B_WIDTH, randPos}, {randPos, B_HEIGHT}, {(B_WIDTH - B_HEIGHT), randPos}};
        return tabPos[(int) (Math.random() * tabPos.length)];
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

    private void checkTemperature() {
        movingGameElementList.stream()
                .filter(fish -> fish.getClass() == RedFish.class)
                .forEach(fish -> fish.setSpeed(getBackground() == Color.cyan ? (initSpeedFish - 3) : (getBackground() == Color.pink ? (initSpeedFish + 3) : (initSpeedFish))));
    }

    private void stopSpeedFishes(Fish fishType, int fishDelay){
        /*  todo : est ce que c'est bon ?? */
        ArrayList<Fish> otherFishes = (ArrayList<Fish>) movingGameElementList.stream().filter(fish -> !(fish.getClass() == fishType.getClass())).collect(Collectors.toList());
        var timer = new Timer(fishDelay, e -> {
            Timer timerSpeed = (Timer) e.getSource();
            timerSpeed.stop();
            otherFishes.forEach(fish -> fish.setSpeed(initSpeedFish));
        });

        timer.start();
        otherFishes.forEach(fish -> fish.setSpeed(0));
        movingGameElementList.stream().filter(fish -> fish.getClass() == fishType.getClass()).forEach(fish -> fish.setSpeed(initSpeedFish));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkFixedGameElementCollision();
            checkFishCollision();
            move();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            // Key event pour update temperature //
            if (key == KeyEvent.VK_NUMPAD0){
                /* todo : demander au prof si ok ? */
                resetGame();
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
                fixedGameElementList.add(new Insect(getRandomCoordinate(), getRandomCoordinate(), PANEL_POWER[(int) (Math.random() * PANEL_POWER.length)]));
            if(key == KeyEvent.VK_NUMPAD5)
                fixedGameElementList.add(new Pellet(getRandomCoordinate(), getRandomCoordinate()));
            /* todo : changement de targets comportement poissons */
//            if(key == KeyEvent.VK_NUMPAD6)
//                fixedGameElementList.add(new Pellet(getRandomCoordinate(), getRandomCoordinate()));
//            if(key == KeyEvent.VK_NUMPAD7)
//                fixedGameElementList.add(new Pellet(getRandomCoordinate(), getRandomCoordinate()));
//            if(key == KeyEvent.VK_NUMPAD8)
//                fixedGameElementList.add(new Pellet(getRandomCoordinate(), getRandomCoordinate()));
            if(key == KeyEvent.VK_NUMPAD9)
                createNewFish();
            /* todo : lorsque j'appuie sur autre que r, les rouges stop mais si je rappuie sur r, rien ne bouge tant que le miniteur n'a pas terminé => solution trop grand delay mais
            *   il faut savoir que si le mauve est pret du rouge, le rouge bouge à cause du delay */

            /* todo : variable magique - ok pour le prof à expliquer dans rapport */
//            if(key == KeyEvent.VK_R)
//                stopSpeedFishes("redFish", 999999999);
//            if(key == KeyEvent.VK_B)
//                stopSpeedFishes("blueFish", 999999999);
//            if(key == KeyEvent.VK_M)
//                stopSpeedFishes("purpleFish", 999999999);
//            if(key == KeyEvent.VK_O)
//                stopSpeedFishes("orangeFish", 999999999);

        }
    }

    private void resetGame() {
        timer.stop();
        initBoard();
    }
}
