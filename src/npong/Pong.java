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

    Random r = new Random();
    boolean play = true;
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
    int ballradius = 10;
    int boardlen;
    int boardwidth = 10;
    double ballaccelspd = 1.0;
    long lastframe; //in micros
    long frametime = 30;
    long lastfpstime;
    long lastfps;
    long timetowaituntil;

    boolean p1movingdown;
    boolean p1movingup;
    boolean p2movingdown;
    boolean p2movingup;
    
    int newp1pos;
    int newp2pos;
    
    int boardmovevel=15;

    public Pong(int width, int height) {
        this.width = width;
        this.height = height;


        boardlen = height / 6;

        newBall(false);

    }

    void newBall(boolean wait) {
        ballposx = width / 2;
        ballposy = height / 2;
        ballaccelx = (r.nextBoolean() ? 1 : -1) * 10;
        ballaccely = (r.nextBoolean() ? 1 : -1) * 10;

        p1pos = height / 2;
        p2pos = p1pos;
        timetowaituntil=System.currentTimeMillis()+3000;
    }

    void start() {
        //while (play){
        //    pongLoop();
        //}
    }

    void pongLoop() {
        long time=System.currentTimeMillis();
        if(timetowaituntil<time){
            check_and_move_ball();
            //sync_fps();
        }
        move_players();
        frames++;
        if (lastfpstime != 0 && time - lastfpstime >= 1000) {
            lastfps = frames;
            frames = 0;
            lastfpstime = time;
        }
    }

    void pause() {
    }

    
    int p1lastmove;
    int p2lastmove;
    private void move_players(){
        if(p1movingdown) p1pos-=boardmovevel;
        if(p1movingup) p1pos+=boardmovevel;
        if(p2movingdown) p2pos-=boardmovevel;
        if(p2movingup) p2pos+=boardmovevel;
        
        int p1move=0;  
        if(newp1pos!=0) {
            p1move=newp1pos-p1pos;
            p1move=p1move/2;
        }
        p1move += p1lastmove/4;
        p1lastmove=p1move;
        p1pos+=p1move;
        //if(newp2pos!=0) {
        //    int p2move=newp2pos-p2pos;
        //    p2lastmove = p2lastmove <<1;// divide by 2
        //    p2pos=newp2pos+p2lastmove;
        //    p2lastmove = p2move;
        //}
        newp1pos=0;newp2pos=0;
        

        if (p1pos < (boardlen / 2)) {
            p1pos = boardlen / 2;
        } else if (p1pos > height - boardlen / 2) {
            p1pos = height - boardlen / 2;
        }
        if (p2pos < (boardlen / 2)) {
            p2pos = boardlen / 2;
        } else if (p2pos > height - boardlen / 2) {
            p2pos = height - boardlen / 2;
        }
    }

    private void check_and_move_ball() {
        double newposx = ballposx + ballaccelx;
        double newposy = ballposy + ballaccely;

        if (newposx < boardwidth || newposx > width - boardwidth) {
            if (newposx < boardwidth) { //for player 1
                if (newposy > p1pos - ballradius /*- ballradius */- boardlen / 2 && newposy < p1pos + ballradius + ballradius + boardlen / 2) { //got it.
                    //bounce
                } else {
                    //lose.
                    p2score++;
                    soutScore();
                    newBall(true);
                    return;
                }
            } else {//player 2
                if (newposy > p2pos - ballradius /*- ballradius */- boardlen / 2 && newposy < p2pos + ballradius + ballradius + boardlen / 2) { //got it.
                    //bounce
                } else {
                    //lose.
                    p1score++;
                    soutScore();
                    newBall(true);
                    return;
                }
            }
            ballaccelx *= -1;
            newposx = ballposx + ballaccelx;
        }
        if (newposy < 0 || newposy > height) {
            ballaccely *= -1;
            newposy = ballposy + ballaccely;
        }
        ballaccelx *= ballaccelspd;
        ballaccely *= ballaccelspd;

        ballposx = newposx;
        ballposy = newposy;
    }

    void soutScore() {
        System.out.println("Score: p1: " + p1score + " p2: " + p2score);
    }

    private void sync_fps() {
        if (lastframe == 0) {
            lastframe = System.currentTimeMillis();
            try {
                Thread.sleep(30);
            } catch (InterruptedException ex) {
            }
            return;
        }
        long time = System.currentTimeMillis();
        time = time - lastframe;

        if (time < frametime) {
            try {
                Thread.sleep(frametime - time);
            } catch (InterruptedException ex) {
            }
        }
        lastframe = System.currentTimeMillis();
    }
}
