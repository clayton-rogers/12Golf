package ca.claytonrogers.Client;

import ca.claytonrogers.Client.GUIObjects.GUIState;
import ca.claytonrogers.Common.IntVector;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by clayton on 2015-07-05.
 */
public class InputHandler extends MouseInputAdapter {

    Application application;

    public InputHandler(Application application) {
        this.application = application;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        application.addClick(new IntVector(e.getX(), e.getY()));
    }
}
