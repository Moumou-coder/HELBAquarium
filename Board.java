import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 500;
    private final int B_HEIGHT = 500;
    private final int DOT_SIZE = 10;
    private final int RAND_POS = 29;
    private final int DELAY = 140;
    private final String PANEL_COLOR[] = {"blue", "orange", "purple", "red"};
    private final String PANEL_POWER[] = {"weak", "medium", "strong"};

    private boolean inGame = true;
    private Timer timer;

    private int pos_x;
    private int pos_y;

    private HashMap<String, ImageIcon> fixedGameElementImageMap;
    private int insectCounter;
    private int pelletCounter;
    private int decorationCounter;
    private ArrayList<FixedGameElement> fixedGameElementList;

    private HashMap<String, ImageIcon> movingGameElementImageMap;
    private int fishCounter;
    private int initSpeedFish;
    private ArrayList<MovingGameElement> movingGameElementList;

    private Image head;

    private int score;
    private int void_x = -1 * B_WIDTH;
    private int void_y = -1 * B_HEIGHT;

    private int target_x = 0;
    private int target_y = 0;

    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean isElemCollisionDecoration = false;

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
        for (int i = 0; i < PANEL_POWER.length; i++) {
            fixedGameElementImageMap.put(PANEL_POWER[i] + "Insect", new ImageIcon("./assets/" + PANEL_POWER[i] + "Insect.png"));
        }
        ImageIcon iip = new ImageIcon(Pellet.getPathToImage());
        fixedGameElementImageMap.put("pellet", iip);
        ImageIcon iid = new ImageIcon(Decoration.getPathToImage());
        fixedGameElementImageMap.put("decoration", iid);


        //L'image de l'icon pour les éléments en mouvement
        movingGameElementImageMap = new HashMap<String, ImageIcon>();
        for (int i = 0; i < PANEL_COLOR.length; i++) {
            movingGameElementImageMap.put(PANEL_COLOR[i] + "Fish", new ImageIcon("./assets/" + PANEL_COLOR[i] + "Fish.png"));
        }

        /* Head */
        ImageIcon iih = new ImageIcon("./assets/head.png");
        head = iih.getImage();

    }

    private void initGame() {

        score = 0;

        pos_x = B_WIDTH / 2;
        pos_y = B_HEIGHT / 2;

        insectCounter = 0;
        pelletCounter = 0;
        decorationCounter = 1;

        fishCounter = 1;

        //List contenant les éléments fixes
        fixedGameElementList = new ArrayList<FixedGameElement>();

        for (int i = 0; i < insectCounter; i++) {
            fixedGameElementList.add(new Insect(getRandomCoordinate(), getRandomCoordinate(), PANEL_POWER[i%PANEL_POWER.length]));
        }
        for (int i = 0; i < pelletCounter; i++) {
            fixedGameElementList.add(new Pellet(getRandomCoordinate(), getRandomCoordinate()));
        }
        for (int i = 0; i < decorationCounter; i++) {
//            fixedGameElementList.add(new Decoration(getRandomCoordinate(), getRandomCoordinate()));
            fixedGameElementList.add(new Decoration(50, 50));
        }

        //List contenant les poissons
        movingGameElementList = new ArrayList<MovingGameElement>();
        initSpeedFish = 3;
        for (int i = 0; i < fishCounter; i++) {
//            movingGameElementList.add(new Fish(getRandomCoordinate(), getRandomCoordinate(), initSpeedFish, PANEL_COLOR[i % PANEL_COLOR.length]));
            movingGameElementList.add(new Fish(getRandomCoordinate(), getRandomCoordinate(), initSpeedFish, PANEL_COLOR[1]));
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

            /* Head */
            g.drawImage(head, pos_x, pos_y, this);

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

    private void checkFixedGameElementCollision() {

        for (FixedGameElement elem : fixedGameElementList) {
            if ((elem.getClass() == Decoration.class)){
                if ((pos_x >= elem.getPosX() && pos_x <= elem.getPosX()+2*DOT_SIZE) && (pos_y >= elem.getPosY() && pos_y <= elem.getPosY()+DOT_SIZE)){
                    isElemCollisionDecoration = true;
                    System.out.println("blocked");
//                    elem.triggerAction(this);
                }
            }
            if ((elem.getClass() == Insect.class)){
                if (pos_x == elem.getPosX() && pos_y == elem.getPosY()){
                    elem.setPosX(void_x);
                    elem.setPosY(void_y);
//                    elem.triggerAction(this);
                }
            }
            if ((elem.getClass() == Pellet.class)){
                if (pos_x == elem.getPosX() && pos_y == elem.getPosY()){
                    elem.setPosX(void_x);
                    elem.setPosY(void_y);
//                    elem.triggerAction(this);
                }
            }
        }
    }

    public void incScore(int valueToIncrease) {
        score += valueToIncrease;
    }

    private void move() {

        ArrayList<Integer> x_moveOptions = new ArrayList<Integer>() ;
        ArrayList<Integer> y_moveOptions = new ArrayList<Integer>() ;
        ArrayList<Double> distances = new ArrayList<Double>() ;

        for (int i = -1 ; i <= 1 ; i++){
            for (int j = -1 ; j <= 1 ; j++){
                int test_pos_x = pos_x +i* DOT_SIZE;
                int test_pos_y = pos_y +j* DOT_SIZE;
                if (isValidPosition(test_pos_x, test_pos_y)){
                    x_moveOptions.add(test_pos_x);
                    y_moveOptions.add(test_pos_y);
                }
            }
        }

        // pourquoi juste la list x_moveOptions ?
        for (int i=0; i < x_moveOptions.size() ; i++){
            Double distance = getDistance(target_x, target_y, x_moveOptions.get(i), y_moveOptions.get(i));
            distances.add(distance);
        }

        // à quoi sert cette partie ?
        double min = Collections.min(distances);
        int min_index = distances.indexOf(min);

        pos_x = x_moveOptions.get(min_index);
        pos_y = y_moveOptions.get(min_index);
    }

    private void checkCollision() {
        inGame = isValidPosition(pos_x, pos_y);
        if (!inGame) {
            timer.stop();
        }
    }

    private int getRandomCoordinate() {

        int r = (int) (Math.random() * RAND_POS);
        return ((r * DOT_SIZE));
    }

    private boolean isValidPosition(int pos_x, int pos_y) {

        boolean res = true ;
        if (pos_y >= B_HEIGHT) {
            res = false;
        }

        if (pos_y < 0) {
            res = false;
        }

        if (pos_x >= B_WIDTH) {
            res = false;
        }

        if (pos_x < 0) {
            res = false;
        }
        if(isElemCollisionDecoration){
            System.out.println("this is a decoration - new target");
        }
        return res;
    }

    private double getDistance(int pos_x0, int pos_y0, int pos_x1, int pos_y1){
        int x_dist = pos_x1-pos_x0;
        int y_dist = pos_y1-pos_y0;
        return Math.sqrt(Math.pow(x_dist, 2)+Math.pow(y_dist, 2));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {
            checkFixedGameElementCollision();
            checkCollision();
            move();
            System.out.println(pos_x + " --- " + pos_y);
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

/* Key event pour update temperature
            if(key == KeyEvent.VK_NUMPAD0)
                setBackground(Color.lightGray);
            if(key == KeyEvent.VK_NUMPAD1)
                setBackground(Color.blue);
            if(key == KeyEvent.VK_NUMPAD2)
                setBackground(Color.pink);
            if(key == KeyEvent.VK_NUMPAD3)
                setBackground(Color.red);
*/

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            move();

        }
    }
}
