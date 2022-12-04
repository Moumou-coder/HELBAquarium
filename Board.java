import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 500;
    private final int B_HEIGHT = 500;
    private final int DOT_SIZE = 10;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

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

    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = false;

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
        ImageIcon iii = new ImageIcon(Insect.getPathToImage());
        fixedGameElementImageMap.put("insect", iii);
        ImageIcon iip = new ImageIcon(Pellet.getPathToImage());
        fixedGameElementImageMap.put("pellet", iip);
        ImageIcon iid = new ImageIcon(Decoration.getPathToImage());
        fixedGameElementImageMap.put("decoration", iid);


        //L'image de l'icon pour les éléments en mouvement
        movingGameElementImageMap = new HashMap<String, ImageIcon>();
        for (int i = 0; i < Fish.PANEL_COLOR.length; i++) {
            movingGameElementImageMap.put(Fish.PANEL_COLOR[i] + "Fish", new ImageIcon("./assets/" + Fish.PANEL_COLOR[i] + "Fish.png"));
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
        decorationCounter = 0;

        fishCounter = 0;

        //List contenant les éléments fixes
        fixedGameElementList = new ArrayList<FixedGameElement>();

        for (int i = 0; i < insectCounter; i++) {
            fixedGameElementList.add(new Insect(getRandomCoordinate(), getRandomCoordinate()));
        }
        for (int i = 0; i < pelletCounter; i++) {
            fixedGameElementList.add(new Pellet(getRandomCoordinate(), getRandomCoordinate()));
        }
        for (int i = 0; i < decorationCounter; i++) {
            fixedGameElementList.add(new Decoration(getRandomCoordinate(), getRandomCoordinate()));
        }

        //List contenant les poissons
        movingGameElementList = new ArrayList<MovingGameElement>();
        initSpeedFish = 3;
        for (int i = 0; i < fishCounter; i++) {
            movingGameElementList.add(new Fish(getRandomCoordinate(), getRandomCoordinate(), initSpeedFish, Fish.PANEL_COLOR[i % 4]));
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
            if ((pos_x == elem.getPosX()) && (pos_y == elem.getPosY())) {
                elem.setPosX(void_x);
                elem.setPosY(void_y);

                elem.triggerAction(this);

                System.out.println(score);
            }
        }
    }

    public void incScore(int valueToIncrease) {
        score += valueToIncrease;
    }

    private void move() {

        if (leftDirection) {
            pos_x -= DOT_SIZE;
        }

        if (rightDirection) {
            pos_x += DOT_SIZE;
        }

        if (upDirection) {
            pos_y -= DOT_SIZE;
        }

        if (downDirection) {
            pos_y += DOT_SIZE;
        }
    }

    private void checkCollision() {

        if (pos_y >= B_HEIGHT) {
            inGame = false;
        }

        if (pos_y < 0) {
            inGame = false;
        }

        if (pos_x >= B_WIDTH) {
            inGame = false;
        }

        if (pos_x < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private int getRandomCoordinate() {

        int r = (int) (Math.random() * RAND_POS);
        return ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {
            checkFixedGameElementCollision();
            checkCollision();
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
