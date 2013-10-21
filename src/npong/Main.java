package npong;

import java.io.*;

/**
 *
 * @author claus
 */
public class Main extends Thread {
    static OGUI gui;
    static Pong pong;
    static UDPListener listener1;
    
    public static boolean run=true;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String system;
            String prop = System.getProperty("os.name");
            if(prop==null) {
                System.out.println("Error: could not determine system type.");
                return;
            }
            if(prop.contains("Linux")){
                system = "linux";
            } else if(prop.contains("Windows")){
                system = "windows";
            } else if(prop.contains("Mac")){
                system = "macosx";
            } else if(prop.contains("Solaris")){
                system = "solaris";
            } else {
                System.out.println("can't identify system \""+prop+"\"");
                return;
            }
            
            System.setProperty("org.lwjgl.librarypath",new File(".").getCanonicalPath()+"/lib/native/"+system);
            System.setProperty("org.lwjgl.util.Debug","true");
            System.setProperty("org.lwjgl.util.NoChecks","false");
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
    }
    @Override
    public void run() {

        gui.start();
        pong.start();
        
        listener1.start();
        //listener2.start();
    }

}
