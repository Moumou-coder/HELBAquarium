
public abstract class FixedGameElement {

    private int pos_x;
    private int pos_y;
    
    public FixedGameElement(int pos_x, int pos_y) {
        
        this.pos_x = pos_x ;
        this.pos_y = pos_y ;
    }
    
    public int getPosX(){
        return pos_x;
    }
    
    public int getPosY(){
        return pos_y;
    }
    
    public void setPosX(int newPos_x){
        pos_x = newPos_x;
    }
    
    public void setPosY(int newPos_y){
        pos_y = newPos_y;
    }
    
    public abstract String getType();
    
    public abstract void triggerAction(Board board);
    
}




