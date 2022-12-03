public class Decoration extends FixedGameElement {

    public Decoration(int pos_x, int pos_y) {

        super(pos_x, pos_y);
    }

    public static String getPathToImage() {
        return "./assets/decoration.png";
    }

    public String getType() {
        return "decoration";
    }

    public void triggerAction(Board board) {
    }
}
