import java.awt.EventQueue;
import javax.swing.JFrame;

public class Aquarium extends JFrame {

    public Aquarium() {
        
        initUI();
    }
    
    private void initUI() {
        
        add(new Board());
               
        setResizable(false);
        pack();
        
        setTitle("HELBAquarium");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex = new Aquarium();
            ex.setVisible(true);
        });
    }
}
