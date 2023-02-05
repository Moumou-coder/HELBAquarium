public class Fish extends MovingGameElement {
    private String color;

    public Fish(int pos_x, int pos_y, int target_x, int target_y, int speed, String color) {
        super(pos_x, pos_y, target_x, target_y, speed);
        this.color = color;
    }

    private int getRandomCoordinateX() {
        int b_WIDTH = 500;
        return ((int) (Math.random() * b_WIDTH));
    }

    private int getRandomCoordinateY() {
        int b_HEIGHT = 500;
        return ((int) (Math.random() * b_HEIGHT));
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return getColor() + "Fish";
    }

    public void move(){

    }

    public void triggerAction(Board board) {
    }
}
