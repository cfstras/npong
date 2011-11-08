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
public class OGUI extends Thread {

    int width, height;
    Pong pong;
    int mousex, mousey;

    public OGUI(int width, int height, Pong pong) {
        this.width = width;
        this.height = height;
        this.pong = pong;
    }

    public void create() {
        try {
            //Display
            Display.setDisplayMode(new DisplayMode(width, height));
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
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);
        //glClearDepth(1.0f);							// Depth Buffer Setup
	//rglEnable(GL_DEPTH_TEST);						// aktiviert Depth Test
	glDepthFunc(GL_LEQUAL);
        //glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);


    }

    public void processKeyboard() {
        //Square's Size
        while (Keyboard.next()) {
            int key = Keyboard.getEventKey();
            if (key == Keyboard.KEY_S) {
                //pong.p1pos -= 15;
                if (Keyboard.getEventKeyState()) {
                    pong.p1movingdown = true;
                } else {
                    pong.p1movingdown = false;
                }
            } else if (key == Keyboard.KEY_W) {
                if (Keyboard.getEventKeyState()) {
                    pong.p1movingup = true;
                } else {
                    pong.p1movingup = false;
                }
            } //Square's Z
            else if (key == Keyboard.KEY_DOWN) {
                if (Keyboard.getEventKeyState()) {
                    pong.p2movingdown = true;
                } else {
                    pong.p2movingdown = false;
                }
            } else if (key == Keyboard.KEY_UP) {
                if (Keyboard.getEventKeyState()) {
                    pong.p2movingup = true;
                } else {
                    pong.p2movingup = false;
                }
            }

        }

    }

    public void processMouse() {
        mousex = Mouse.getX();
        mousey = Mouse.getY();
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT);//| GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        //glTranslated(0, 0, -3);
        glViewport(0, 0, width, height);

        drawRect((float) pong.ballposx, (float) pong.ballposy, pong.ballradius, pong.ballradius, 0.0f, 1, 1, 1);

        drawRect((float) (pong.boardwidth >> 1), (float) pong.p1pos, pong.boardwidth, pong.boardlen, 0, 1.0f, 1.0f, 1.0f);
        drawRect(width - (float) (pong.boardwidth >> 1), (float) pong.p2pos, pong.boardwidth, pong.boardlen, 0, 1.0f, 1.0f, 1.0f);

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
    private void drawRect(float x, float y, float width, float height, float rotate, float r, float g, float b) {
        glPushMatrix();
        glTranslatef(x - width / 2, y - height / 2, 0.0f);
        glRotatef(rotate, 0.0f, 0.0f, 1.0f);
        glColor3f(r, g, b);
        glBegin(GL_QUADS);
        glTexCoord3f(0.0f, 0.0f,0.0f);glVertex3f(0.0f, 0.0f,1.0f);
        glTexCoord3f(1.0f, 0.0f,0.0f);glVertex3f(width, 0.0f,0.5f);
        glTexCoord3f(1.0f, 1.0f,0.0f);glVertex3f(width, height,0.4f);
        glTexCoord3f(0.0f, 1.0f,0.0f);glVertex3f(0.0f, height,1.0f);
        glEnd();
        //glTranslatef(-(x - width / 2), -(y - height / 2), 0.0f);
        glPopMatrix();
    }

    public void resizeGL() {

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        //glFrustum(-1000.0f, 1000, -1000.0f, 1000,0.5,10);
        gluOrtho2D(0, width,0, height);
        //gluPerspective(45.0f,width/height,0.1f,100.0f);
        glPushMatrix();



        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glPushMatrix();
    }

    @Override
    public void run() {
        create();
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && pong.play && Main.run) {
            pong.pongLoop();
            if (Display.isVisible()) {
                processKeyboard();
                processMouse();
                render();
            } else {
                if (Display.isDirty()) {
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
