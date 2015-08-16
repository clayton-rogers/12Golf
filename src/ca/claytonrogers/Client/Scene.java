package ca.claytonrogers.Client;

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
 * clickables, etc.
 *
 * Created by clayton on 2015-08-15.
 */
public abstract class Scene {

    private List<GUIObject> guiObjectList = new ArrayList<>(10);
    private Queue<IntVector> mouseClickList = new ConcurrentLinkedQueue<>();

    public Scene() {
        
    }

    public final void addClick(IntVector clickLocation) {
        mouseClickList.add(clickLocation);
    }

    public abstract void handleInputs();
    public abstract void processState();
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

}
