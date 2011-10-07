/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package npong;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author claus
 */
public class Pong {
    Random r=new Random();

    boolean play=true;

    int x;

    int width;
    int height;

    int p1pos;
    int p2pos;

    double ballposx;
    double ballposy;

    double ballaccelx;
    double ballaccely;
    
    
    int ballradius=10;
    int boardlen;
    int boardwidth=10;

    double ballaccelspd=1.0;

    long lastframe; //in micros
    long frametime=30;

    public Pong(int width,int height){
        this.width=width;
        this.height=height;

        ballposx=width/2;
        ballposy=height/2;
        boardlen=height/6;

        ballaccelx=(r.nextBoolean()?1:-1)*10;
        ballaccely=(r.nextBoolean()?1:-1)*10;

        p1pos=height/2;
        p2pos=p1pos;
    }

    void start() {
        while (play){
            check_and_move_ball();
            sync_fps();
            x++;
        }
    }

    void pause() {
        
    }

    private void check_and_move_ball() {
        double newposx=ballposx+ballaccelx;
        double newposy=ballposy+ballaccely;
        
        if(newposx < boardwidth || newposx > width-boardwidth){
            ballaccelx*=-1;
            newposx=ballposx+ballaccelx;
        }
        if(newposy < 0 || newposy > height){
            ballaccely*=-1;
            newposy=ballposy+ballaccely;
        }
        ballaccelx*=ballaccelspd;
        ballaccely*=ballaccelspd;

        ballposx=newposx;
        ballposy=newposy;
    }

    private void sync_fps() {
        if(lastframe==0) {
            lastframe=System.currentTimeMillis();
            try {
                Thread.sleep(30);
            } catch (InterruptedException ex) {}
            return;
        }
        long time=System.currentTimeMillis();
        time=time-lastframe;

        if(time<frametime){
            try {
                Thread.sleep(frametime - time);
            } catch (InterruptedException ex) {}
        }
        lastframe=System.currentTimeMillis();
    }
}
