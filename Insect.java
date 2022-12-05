public class Insect extends FixedGameElement {


    public Insect(int pos_x, int pos_y) {
        
        super(pos_x, pos_y);
    }
    
    public static String getPathToImage(){
        return "./assets/insect.png";
    }
    
    public String getType(){
        return "insect";
    }
    
    public void triggerAction(Board board){
        board.incScore(2);
    }
}

/*todo :
   Il existe 3 sortes de poissons - weak / medium / strong
   Same logic que pour les voitures
 */




