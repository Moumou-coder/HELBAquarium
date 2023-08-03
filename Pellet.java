import javax.swing.*;

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

    /* Lorsqu'un poisson mange une pilule, il ralentit sa vitesse en la divisant par trois
    pendant un certain nombre de secondes équivalent au nombre de décorations présentes dans l'aquarium.
    Cette méthode va faire disparaitre la pilule comestible et donner le délai durant lequel le poisson sera plus lent */
    @Override
    public void handleCollision(Fish fish, int void_x, int void_y) {
        int oneSecond = 1000;
        this.setPosX(void_x);
        this.setPosY(void_y);
        int delaySlowFish = Board.decorationList.size()*oneSecond;
        slowFishSpeed(fish, delaySlowFish);
    }

    /* Cette méthode met à jour la vitesse du poisson et le ralentit temporairement. */
    private void slowFishSpeed (Fish fish, int delay){
        int divider = 3;
        var timer = new Timer(delay, e -> {
            Timer timerSpeed = (Timer) e.getSource();
            timerSpeed.stop();
            fish.setSpeed(Fish.INIT_SPEED);
        });

        timer.start();
        fish.setSpeed(fish.getSpeed()/divider);
    }
}




