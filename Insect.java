import javax.swing.*;

public class Insect extends FixedGameElement {

    private String power;
    private int durationSpeed = 0;
    private static final String[] PANEL_POWER = {"weak", "medium", "strong"};


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

    public static String[] getPANEL_POWER() {
        return PANEL_POWER;
    }

    public String getType(){
        return getPower() + "Insect";
    }
    
    public int triggerAction(){
        if(getPower().equals("weak")) durationSpeed = 5000;
        if(getPower().equals("medium")) durationSpeed = 10000;
        if(getPower().equals("strong")) durationSpeed = 15000;

        return durationSpeed;
    }

    @Override
    public void handleCollision(Fish fish, int void_x, int void_y){
        int speedFishIncreased = 14;
        this.setPosX(void_x);
        this.setPosY(void_y);

        Timer timer = new Timer(this.triggerAction(), actionEvent -> {
            Timer timerSpeed = (Timer) actionEvent.getSource();
            timerSpeed.stop();
            fish.setSpeed(Fish.INIT_SPEED);
        });
        timer.start();
        fish.setSpeed(speedFishIncreased);

    }
}




