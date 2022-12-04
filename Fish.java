import java.util.Random;

public class Fish extends MovingGameElement {

    public Fish(int pos_x, int pos_y, int speed) {
        super(pos_x, pos_y, speed);
    }

    public static String getPathToImage(String color) {
        return "./assets/" + color + "Fish.png";
    }

    public String getType() {
        return "fish";
    }

    public void triggerAction(Board board) {
    }
}
