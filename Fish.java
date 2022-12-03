public class Fish extends MovingGameElement {

    public Fish(int pos_x, int pos_y) {
        super(pos_x, pos_y);
    }

    public static String getPathToImage(){
        return "./assets/redFish.png";
    }

    public String getType(){
        return "fish";
    }

    public void triggerAction(Board board){
    }
}
