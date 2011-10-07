package npong;

import java.util.logging.LogRecord;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * @author jediTofu
 * @see <a href="http://lwjgl.org/">LWJGL Home Page</a>
 */
public class OGUI extends Thread{

  int width, height;
  Pong pong;
  int mousex,mousey;

  public OGUI(int width,int height,Pong pong) {
      this.width=width;this.height=height;
      this.pong=pong;
  }

  public void create(){
        try {
            //Display
            Display.setDisplayMode(new DisplayMode(width,height));
            Display.setFullscreen(false);
            Display.setTitle("npong");
            Display.create();

            //Keyboard
            Keyboard.create();

            //Mouse
            Mouse.setGrabbed(false);
            Mouse.create();

            //OpenGL
            initGL();
            resizeGL();
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        }
  }

  public void destroyWindow() {
    //Methods already check if created before destroying.
    Mouse.destroy();
    Keyboard.destroy();
    Display.destroy();
  }

  public void initGL() {
    //2D Initialization
    glClearColor(0.0f,0.0f,0.0f,0.0f);
    glDisable(GL_DEPTH_TEST);
    glDisable(GL_LIGHTING);
  }

  public void processKeyboard() {
    //Square's Size
    if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
      pong.p1pos-=15;
    }
    if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
      pong.p1pos+=15;
    }

    //Square's Z
    if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
      pong.p2pos+=15;
    }
    if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
      pong.p2pos-=15;
    }
  }

  public void processMouse() {
    mousex = Mouse.getX();
    mousey = Mouse.getY();
  }

  public void render() {
    glClear(GL_COLOR_BUFFER_BIT);
    glLoadIdentity();
    
    glViewport(0,0,width,height);
    
    drawRect((float)pong.ballposx,(float)pong.ballposy,pong.ballradius,pong.ballradius,0.0f,1,1,1);
    
    drawRect((float)(pong.boardwidth>>1),(float)pong.p1pos, pong.boardwidth, pong.boardlen, 0,1.0f, 1.0f, 1.0f);
    drawRect(width-(float)(pong.boardwidth>>1),(float)pong.p2pos, pong.boardwidth, pong.boardlen, 0,1.0f, 1.0f, 1.0f);
  }
  
  /**
   * draws a rectangle
   * @param x rectangle middle x coord
   * @param y rectangle middle y coord
   * @param mx left-from-middle distance
   * @param px right-from-middle distance
   * @param my up-from-middle distance
   * @param py down-from-middle distance
   * @param rotate rotation of the whole thing
   * @param r red
   * @param g green
   * @param b blue
   */
  private void drawRect(float x,float y, float width,float height,float rotate,float r,float g,float b){
        glTranslatef(x-width/2,y-height/2,0.0f);
        glRotatef(rotate,0.0f,0.0f,1.0f);
        glColor3f(r,g,b);
        glBegin(GL_QUADS);
            glTexCoord2f(0.0f,0.0f); glVertex2f(0.0f,0.0f);
            glTexCoord2f(1.0f,0.0f); glVertex2f(width,0.0f);
            glTexCoord2f(1.0f,1.0f); glVertex2f(width,height);
            glTexCoord2f(0.0f,1.0f); glVertex2f(0.0f,height);
        glEnd();
        glTranslatef(-(x-width/2),-(y-height/2), 0.0f);
  }
  

  public void resizeGL() {
    
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluOrtho2D(0.0f,800,0.0f,600);
    glPushMatrix();

    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    glPushMatrix();
  }

    @Override
  public void run() {
      create();
    while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
      if(Display.isVisible()) {
        processKeyboard();
        processMouse();
        render();
      }
      else {
        if(Display.isDirty()) {
          render();
        }
        //try {
        //  Thread.sleep(30);
        //}
        //catch(InterruptedException ex) {
        //}
      }
      Display.update();
      Display.sync(60);
    }
    destroyWindow();
    Main.exit();
  }

}

