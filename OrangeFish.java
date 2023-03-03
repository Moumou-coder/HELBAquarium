public class OrangeFish extends Fish {
    static String pathToImage = "./assets/orangeFish.png";
    public OrangeFish(int pos_x, int pos_y, int target_x, int target_y, int speed) {
        super(pos_x, pos_y, target_x, target_y, speed);
    }

    @Override
    public void move(Board board) {
        super.setInitialTargets(board);
        super.setPositions();
    }

    @Override
    public String getPathToImage() {
        return pathToImage;
    }
}
