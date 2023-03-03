public class Pellet extends FixedGameElement {
 /* todo : extends depuis moving mtn !!! */
    public Pellet(int pos_x, int pos_y) {
        super(pos_x, pos_y);
    }

    public static String getPathToImage(){
        return "./assets/pellet.png";
    }

    public String getType(){
        return "pellet";
    }

}




