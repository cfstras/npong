/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package npong;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

/**
 *
 * @author claus
 */
public class GUI extends Thread {

    JFrame frame;
    //Canvas canvas;
    int width, height;
    Pong pong;
    long lastframe; //in micros
    long frametime=30;

    Graphics g;
    BufferStrategy bs;
    private final GraphicsDevice graphicsdevice;

    public GUI(int width, int height) {
        this.width = width;
        this.height = height;
        graphicsdevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        //graphicsdevice.getDisplayMode();
        frame = new JFrame(graphicsdevice.getDefaultConfiguration());
        frame.setUndecorated(true);
        frame.setIgnoreRepaint(true);
        frame.setResizable(false);
        frame.addKeyListener(null);
        //frame.add(canvas);
        //frame.pack();
        frame.setVisible(true);
        frame.createBufferStrategy(2);
        frame.setIgnoreRepaint(true);
        bs=frame.getBufferStrategy();
        
    }

    @Override
    public void run() {
        pong=Main.pong;
        while (pong.play){
            //canvas.repaint();
            //frame.repaint();
            draw();
            sync_fps();
        }
    }

    void draw() {
        g=bs.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0,0,width,height);
        g.setColor(Color.white);
        g.fillRect((int)(pong.ballposx), (int)(pong.ballposy), 20, 20);
        g.fillRect(0,(int)(pong.p1pos),(int)(pong.boardwidth),(int)(pong.boardlen));
        g.fillRect((int)(width-pong.boardwidth),(int)(pong.p2pos),(int)(pong.boardwidth),(int)(pong.boardlen));

        bs.show();
        g.dispose();
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
                //Thread.sleep(frametime - time);
                Thread.sleep(30);
            } catch (InterruptedException ex) {}
        }
        lastframe=System.currentTimeMillis();
    }
}
