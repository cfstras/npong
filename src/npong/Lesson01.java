package npong;

/*
 *      This Code Was Created By Jeff Molofee 2000
 *      A HUGE Thanks To Fredric Echols For Cleaning Up
 *      And Optimizing This Code, Making It More Flexible!
 *      If You've Found This Code Useful, Please Let Me Know.
 *      Visit My Site At nehe.gamedev.net
 */

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
//import org.lwjgl.opengl.util.GLU;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.glu.GLU;

/**
 * @author Mark Bernard
 * date:    16-Nov-2003
 *
 * Port of NeHe's Lesson 1 to LWJGL
 * Title: Setting Up An OpenGL Window
 * Uses version 0.8alpha of LWJGL http://www.lwjgl.org/
 *
 * Be sure that the LWJGL libraries are in your classpath
 *
 * Ported directly from the C++ version
 *
 * 2004-05-08: Updated to version 0.9alpha of LWJGL.
 *             Changed from all static to all instance objects.
 * 2004-09-21: Updated to version 0.92alpha of LWJGL.
 */
public class Lesson01 {
    private boolean done = false;
    private boolean fullscreen = false;
    private final String windowTitle = "NeHe's OpenGL Lesson 1 for LWJGL (Setting Up An OpenGL Window)";
    private boolean f1 = false;
    private DisplayMode displayMode;

    /**
     * Everything starts and ends here.  Takes 1 optional command line argument.
     * If fullscreen is specified on the command line then fullscreen is used,
     * otherwise windowed mode will be used.
     * @param args command line arguments
     */
    public static void main(String args[]) {
        boolean fullscreen = false;
        if(args.length>0) {
            if(args[0].equalsIgnoreCase("fullscreen")) {
                fullscreen = true;
            }
        }
        Lesson01 l1 = new Lesson01();
        l1.run(fullscreen);
    }

    /**
     * Launch point
     * @param fullscreen boolean value, set to true to run in fullscreen mode
     */
    public void run(boolean fullscreen) {
        this.fullscreen = fullscreen;
        try {
            init();
            while (!done) {
                mainloop();
                render();
                Display.update();
            }
            cleanup();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * All updating is done here.  Key and mouse polling as well as window closing and
     * custom updates, such as AI.
     */
    private void mainloop() {
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {       // Exit if Escape is pressed
            done = true;
        }
        if(Display.isCloseRequested()) {                     // Exit if window is closed
            done = true;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_F1) && !f1) {    // Is F1 Being Pressed?
            f1 = true;                                      // Tell Program F1 Is Being Held
            switchMode();                                   // Toggle Fullscreen / Windowed Mode
        }
        if(!Keyboard.isKeyDown(Keyboard.KEY_F1)) {          // Is F1 Being Pressed?
            f1 = false;
        }
    }

    private void switchMode() {
        fullscreen = !fullscreen;
        try {
            Display.setFullscreen(fullscreen);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * For rendering all objects to the screen
     * @return boolean for success or not
     */
    private boolean render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);          // Clear The Screen And The Depth Buffer
        GL11.glLoadIdentity();                          // Reset The Current Modelview Matrix

        float x=0.5f; int width=50; int height=50;int rotate=0;int r=1,g=1,b=1;
        float y=0.5f;

        glTranslatef(x - width / 2, y - height / 2, 0.0f);
        glRotatef(rotate, 0.0f, 0.0f, 1.0f);
        glColor3f(r, g, b);
        glBegin(GL_QUADS);
        glTexCoord3f(0.0f, 0.0f,0.0f);glVertex3f(0.0f, 0.0f,1.0f);
        glTexCoord3f(1.0f, 0.0f,0.0f);glVertex3f(width, 0.0f,0.5f);
        glTexCoord3f(1.0f, 1.0f,0.0f);glVertex3f(width, height,0.4f);
        glTexCoord3f(0.0f, 1.0f,0.0f);glVertex3f(0.0f, height,1.0f);
        glEnd();


        return true;
    }

    /**
     * Create a window depending on whether fullscreen is selected
     * @throws Exception Throws the Window.create() exception up the stack.
     */
    private void createWindow() throws Exception {
        Display.setFullscreen(fullscreen);
        //DisplayMode d[] = Display.getAvailableDisplayModes();
        displayMode=new DisplayMode(800,600);
        Display.setDisplayMode(displayMode);
        Display.setTitle(windowTitle);
        Display.create();
    }

    /**
     * Do all initilization code here.  Including Keyboard and OpenGL
     * @throws Exception Passes any exceptions up to the main loop to be handled
     */
    private void init() throws Exception {
        createWindow();

        initGL();
    }

    /**
     * Initialize OpenGL
     *
     */
    private void initGL() {
        GL11.glEnable(GL11.GL_TEXTURE_2D); // Enable Texture Mapping
        GL11.glShadeModel(GL11.GL_SMOOTH); // Enable Smooth Shading
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
        GL11.glClearDepth(1.0); // Depth Buffer Setup
        GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
        GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Testing To Do

        GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
        GL11.glLoadIdentity(); // Reset The Projection Matrix

        // Calculate The Aspect Ratio Of The Window
        GLU.gluPerspective(
          45.0f,
          (float) displayMode.getWidth() / (float) displayMode.getHeight(),
          0.1f,
          100.0f);
        glViewport(0, 0, 800, 600);
        GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix

        // Really Nice Perspective Calculations
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
    }

    /**
     * Cleanup all the resources.
     *
     */
    private void cleanup() {
        Display.destroy();
    }

}
