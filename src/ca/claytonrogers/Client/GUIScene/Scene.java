package ca.claytonrogers.Client.GUIScene;

import ca.claytonrogers.Client.GUIObjects.GUIObject;
import ca.claytonrogers.Common.Constants;
import ca.claytonrogers.Common.IntVector;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This abstract class can be extended to create a GUI scene (collection of GUI elements,
 * and clickable elements, etc.).
 * <p>
 * The T type parameter is the type of payload the scene expects when it is started using
 * startScene.
 * <p>
 * Created by clayton on 2015-08-15.
 */
public abstract class Scene<T> {

    /**
     * The scene type denotes all the possible types of scenes in the program.
     */
    public enum SceneType {
        Score,
        Game,
        MainMenu,
        Options,
        Quit,
        LostConnection,
        Waiting
    }

    /**
     * The GUI object list denotes all the objects which should be draw and/or checked for click
     * hits.
     */
    protected final List<GUIObject> guiObjectList = new ArrayList<>(10);

    /**
     * The mouse click list contains all the mouse clicks which have be queued externally.
     */
    protected final Queue<IntVector> mouseClickList = new ConcurrentLinkedQueue<>();

    /**
     * The next scene to load. This will be null until the scene should change.
     */
    protected SceneChange nextScene = null;

    /**
     * Clicks are added the the scene externally by the application's click handler.
     *
     * @param clickLocation The location of the click.
     */
    public final void addClick(IntVector clickLocation) {
        mouseClickList.add(clickLocation);
    }

    /**
     * This method is called once for every time a scene change occurs to this scene. It optionally
     * contains a payload.
     *
     * @param sceneChange The sceneChange object which initiated the scene change.
     */
    public abstract void startScene(SceneChange<T> sceneChange);

    /**
     * This is called once every draw loop and is expected to handled any inputs in the mouse click
     * list.
     */
    public abstract void handleInputs();

    /**
     * This is called once every draw loop and is expected to handled any updated to the game/etc.
     * state that is required.
     */
    public abstract void processState();

    /**
     * This is called once every draw loop and will draw all objects in the GUI object list. (By
     * default objects will only be drawn if their visibility is true.
     *
     * @param window The container to draw in (typically the JFrame of the application.
     * @param windowBounds The current size of the window.
     */
    public void drawScreen(Window window, IntVector windowBounds) {
        BufferStrategy bf = window.getBufferStrategy();
        Graphics g = null;

        try {
            g = bf.getDrawGraphics();

            g.setColor(Constants.BACKGROUND_COLOR);
            g.fillRect(0, 0, windowBounds.x, windowBounds.y);

            for (GUIObject object : guiObjectList) {
                object.draw(g);
            }
        } finally {
            if (g != null) {
                g.dispose();
            }
        }

        bf.show();
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * This is called once every draw loop to check if a scene wants to change to another scene.
     * It will return the name of the scene it wants to change to (along with an optional payload).
     * It will return null if it does not want to change.
     * @return The scene change to be performed, or null if non is required.
     */
    public SceneChange getNextScene() {
        SceneChange temp = nextScene;
        nextScene = null;
        return temp;
    }

    /**
     * Method for internal use by the implementing class. Will check the mouse click list for hits
     * against GUI objects in the GUI object list. It will return the type of object if a hit is
     * found. If there are no clicks queued or the clicks do not hit anything then it will return
     * GUIObject.Type.None.
     * <p>
     * NOTE: This does not remove the click, only peek()s at it.
     * <p>
     * @return The type of GUI object hit. "None" if no objects are hit.
     */
    protected GUIObject.Type getNextGoodClickLocation() {
        IntVector clickLocation = mouseClickList.peek();
        if (clickLocation == null) {
            return GUIObject.Type.None;
        }
        for (GUIObject object : guiObjectList) {
            if (object.checkClicked(clickLocation)) {
                return object.getType();
            }
        }
        // Since the mouse click did not hit anything, we throw it out and return none.
        mouseClickList.poll();
        return GUIObject.Type.None;
    }
}
