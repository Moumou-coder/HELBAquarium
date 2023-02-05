import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 500;
    private final int B_HEIGHT = 500;
    private final int DOT_SIZE = 10;
    //    private final int RAND_POS = 29;
    private final int DELAY = 140;
    private final String PANEL_COLOR[] = {"blue", "orange", "purple", "red"};
    private final String PANEL_POWER[] = {"weak", "medium", "strong"};

    private boolean inGame = true;
    private Timer timer;

    private HashMap<String, ImageIcon> fixedGameElementImageMap;
    private int insectCounter;
    private int pelletCounter;
    private int decorationCounter;
    private ArrayList<FixedGameElement> fixedGameElementList;

    private HashMap<String, ImageIcon> movingGameElementImageMap;
    private int fishCounter;
    private int initSpeedFish;
    private ArrayList<MovingGameElement> movingGameElementList;

    private int score;

    private int void_x = -1 * B_WIDTH;
    private int void_y = -1 * B_HEIGHT;

    private boolean isElemCollisionDecoration = false;
    private int xOrY = (int) (Math.random() * 1);
    private int zeroOrFiveHundred = (int) (Math.random() * 1);


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
            movingGameElementImageMap.put(c + "Fish", new ImageIcon("./assets/" + c + "Fish.png"));
        }
    }

    private void initGame() {
        score = 0;

        insectCounter = 0;
        pelletCounter = 0;
        decorationCounter = 0;

        fishCounter = 8;

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
        movingGameElementList = new ArrayList<MovingGameElement>();
        initSpeedFish = 1;
        for (int i = 0; i < fishCounter; i++) {
//            movingGameElementList.add(new Fish(getRandomCoordinate(), getRandomCoordinate(), initSpeedFish, PANEL_COLOR[i % PANEL_COLOR.length]));
            movingGameElementList.add(new Fish(getRandomCoordinate(), getRandomCoordinate(), getArrayTarget()[0], getArrayTarget()[1], initSpeedFish, PANEL_COLOR[1]));
        }

        timer = new Timer(DELAY, this);
        timer.start();
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
                g.drawImage(movingGameElementImageMap.get(elem.getType()).getImage(), elem.getPos_x(), elem.getPos_y(), this);
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


   /* private void checkFixedGameElementCollision() {
        for (FixedGameElement elem : fixedGameElementList) {
            for (MovingGameElement mvElem : movingGameElementList) {
                if ((elem.getClass() == Decoration.class)) {
                    if ((mvElem.getPos_x() >= elem.getPosX() && mvElem.getPos_x() <= elem.getPosX() + 2 * DOT_SIZE) && (mvElem.getPos_y() >= elem.getPosY() && mvElem.getPos_y() <= elem.getPosY() + DOT_SIZE)) {
                        isElemCollisionDecoration = true;
                    }
                }
                if ((elem.getClass() == Insect.class)) {
                    if (mvElem.getPos_x() == elem.getPosX() && mvElem.getPos_y() == elem.getPosY()) {
                        elem.setPosX(void_x);
                        elem.setPosY(void_y);
//                    elem.triggerAction(this);
                    }
                }
                if ((elem.getClass() == Pellet.class)) {
                    if (mvElem.getPos_x() == elem.getPosX() && mvElem.getPos_y() == elem.getPosY()) {
                        elem.setPosX(void_x);
                        elem.setPosY(void_y);
//                    elem.triggerAction(this);
                    }
                }
            }
        }
    }*/

    public void incScore(int valueToIncrease) {
        score += valueToIncrease;
    }

    private int getRandomCoordinate(){
        return (int)(Math.random()*(B_WIDTH-DOT_SIZE));
    }

    /* TODO: Déplacer toute la méthode move dans le classe fish - responsabilité des class */
    private void move() {


        for (MovingGameElement movingElem : movingGameElementList) {

            ArrayList<Integer> x_moveOptions = new ArrayList<Integer>();
            ArrayList<Integer> y_moveOptions = new ArrayList<Integer>();
            ArrayList<Double> distances = new ArrayList<Double>();

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int test_pos_x = movingElem.getPos_x() + i * DOT_SIZE;
                    int test_pos_y = movingElem.getPos_y() + j * DOT_SIZE;
                    if (isValidPosition(movingElem, test_pos_x, test_pos_y)) {
                        x_moveOptions.add(test_pos_x);
                        y_moveOptions.add(test_pos_y);
                    }
                }
            }

            for (int i = 0; i < x_moveOptions.size(); i++) {
                Double distance = getDistance(movingElem.getTarget_x(), movingElem.getTarget_y(), x_moveOptions.get(i), y_moveOptions.get(i));
                distances.add(distance);
            }

            double min = Collections.min(distances);
            int min_index = distances.indexOf(min);

//            if (min_index < x_moveOptions.size()) {
                movingElem.setPos_x(x_moveOptions.get(min_index));
                movingElem.setPos_y(y_moveOptions.get(min_index));
//            }

        }
    }

    private boolean isValidPosition(MovingGameElement mvElem, int pos_x, int pos_y) {
        boolean isPositionValid = true;

        if (pos_y < 0 || pos_y >= B_HEIGHT) {
            isPositionValid = false;
            changeTargets(mvElem);
        }
        if (pos_x < 0 || pos_x >= B_WIDTH) {
            isPositionValid = false;
            changeTargets(mvElem);
        }

        return isPositionValid;
    }

    private double getDistance(int pos_x0, int pos_y0, int pos_x1, int pos_y1) {
        int x_dist = pos_x1 - pos_x0;
        int y_dist = pos_y1 - pos_y0;
        return Math.sqrt(Math.pow(x_dist, 2) + Math.pow(y_dist, 2));
    }

    private void changeTargets(MovingGameElement mvElem) {
        int[] randTarget = getArrayTarget();
        mvElem.setTarget_x(randTarget[0]);
        mvElem.setTarget_y(randTarget[1]);
    }

    private int[] getArrayTarget(){
        int randPos = getRandomCoordinate();
        int[][] tabPos = {{randPos, 0}, {500, randPos}, {randPos, 500}, {0, randPos}};
        return tabPos[(int) (Math.random() * tabPos.length)];
    }

//    private void checkCollision() {
//        for (MovingGameElement mvElem : movingGameElementList) {
//            inGame = isValidPosition(mvElem, mvElem.getPos_x(), mvElem.getPos_y());
//        }
//        if (!inGame) {
//            timer.stop();
//        }
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
//            checkFixedGameElementCollision();
//            checkCollision();
            move();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            // Key event pour update temperature //
            if (key == KeyEvent.VK_NUMPAD0)
                setBackground(Color.lightGray);
            if (key == KeyEvent.VK_NUMPAD1)
                setBackground(Color.blue);
            if (key == KeyEvent.VK_NUMPAD2)
                setBackground(Color.pink);
            if (key == KeyEvent.VK_NUMPAD3)
                setBackground(Color.red);

        }
    }
}
