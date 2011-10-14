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

    int frames;

    int width;
    int height;

    int p1pos;
    int p2pos;

    int p1score;
    int p2score;

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
    long lastfpstime;
    long lastfps;

    public Pong(int width,int height){
        this.width=width;
        this.height=height;

        
        boardlen=height/6;

        newBall(false);

    }

    void newBall(boolean wait){
        ballposx=width/2;
        ballposy=height/2;
        ballaccelx=(r.nextBoolean()?1:-1)*10;
        ballaccely=(r.nextBoolean()?1:-1)*10;

        p1pos=height/2;
        p2pos=p1pos;
        if(wait){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {}
        }
    }

    void start() {
        while (play){
            check_and_move_ball();
            sync_fps();
            

            frames++;
            if(lastfpstime !=0 && System.currentTimeMillis()-lastfpstime>=1000){
                lastfps=frames;
                frames=0;
                lastfpstime=System.currentTimeMillis();
            }
        }
    }

    void pause() {
        
    }

    private void check_and_move_ball() {
        double newposx=ballposx+ballaccelx;
        double newposy=ballposy+ballaccely;
        
        if(newposx < boardwidth || newposx > width-boardwidth){
            if(newposx<boardwidth){ //for player 1
                if(newposy>p1pos-ballradius-ballradius-boardlen/2 && newposy<p1pos+ballradius+ballradius+boardlen/2){ //got it.
                    //bounce
                } else {
                    //lose.
                    p2score++;
                    soutScore();
                    newBall(true);
                    return;
                }
            } else {//player 2
                if(newposy>p2pos-ballradius-ballradius-boardlen/2 && newposy<p2pos+ballradius+ballradius+boardlen/2){ //got it.
                    //bounce
                } else {
                    //lose.
                    p1score++;
                    soutScore();
                    newBall(true);
                    return;
                }
            }
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
    void soutScore(){
        System.out.println("Score: p1: "+p1score+" p2: "+p2score);
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
