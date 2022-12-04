public abstract class MovingGameElement {

    private int pos_x;
    private int pos_y;
    private int speed;

    public MovingGameElement(int pos_x, int pos_y, int speed) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.speed = speed;
    }

    public int getPos_x() {
        return pos_x;
    }

    public void setPos_x(int pos_x) {
        this.pos_x = pos_x;
    }

    public int getPos_y() {
        return pos_y;
    }

    public void setPos_y(int pos_y) {
        this.pos_y = pos_y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public abstract String getType();

    public abstract void triggerAction(Board board);

}