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
        //save scores
        //disconnect
        System.exit(0);
    }


    public Main() {
        pong = new Pong(800,600);
        gui = new OGUI(800,600,pong);
        

    }
    @Override
    public void run() {
        gui.start();
        pong.start();
    }

}
