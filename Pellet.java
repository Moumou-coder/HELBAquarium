public class Pellet extends FixedGameElement {

    public Pellet(int pos_x, int pos_y) {
        super(pos_x, pos_y);
    }

    public static String getPathToImage() {
        return "./assets/pellet.png";
    }

    public String getType() {
        return "Pellet";
    }

    /* Cette méthode va faire disparaitre la pilule comestible et donner le délai durant lequel le poisson sera plus lent */
    @Override
    public int handleCollision(int void_x, int void_y) {
        int oneSecond = 1000;
        this.setPosX(void_x);
        this.setPosY(void_y);
        return Board.decorationList.size() * oneSecond;
    }
}




