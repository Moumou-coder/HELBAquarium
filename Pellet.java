public class Pellet extends FixedGameElement {

    public Pellet(int pos_x, int pos_y) {
        super(pos_x, pos_y);
    }

    public static String getPathToImage(){
        return "./assets/pellet.png";
    }

    public String getType(){
        return "Pellet";
    }
}




