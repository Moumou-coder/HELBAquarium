public class Fish extends MovingGameElement {

    private String color;
    static final String PANEL_COLOR[] = {"blue", "orange", "purple", "red"};


    public Fish(int pos_x, int pos_y, int speed, String color) {
        super(pos_x, pos_y, speed);
        this.color = color;
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

    public void triggerAction(Board board) {
    }
}
