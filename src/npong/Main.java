/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package npong;

/**
 *
 * @author claus
 */
public class Main extends Thread{
    static OGUI gui;
    static Pong pong;
    static UDPListener listener1;
    static UDPListener listener2;
    
    public static boolean run=true;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Main().start();
        
    }
    public static void pause(){
        pong.pause();
    }

    static void exit() {
        run=false;
        pong.play=false;
       
        //save scores
        //disconnect
        System.exit(0);
    }


    public Main() {
        pong = new Pong(800,600);
        gui = new OGUI(800,600,pong);
        listener1=new UDPListener(false,pong);
        listener2=new UDPListener(true,pong);
    }
    @Override
    public void run() {
        

        gui.start();
        pong.start();
        
        listener1.start();
        //listener2.start();
    }

}
