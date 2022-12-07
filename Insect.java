public class Insect extends FixedGameElement {

    private String power;

    public Insect(int pos_x, int pos_y, String power) {
        super(pos_x, pos_y);
        this.power = power;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getType(){
        return getPower() + "Insect";
    }
    
    public void triggerAction(Board board){
        board.incScore(2);
    }
}




