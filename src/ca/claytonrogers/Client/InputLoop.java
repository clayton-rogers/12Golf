package ca.claytonrogers.Client;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by clayton on 2015-07-05.
 */
public class InputLoop extends MouseInputAdapter implements Runnable {

    Thread inputHandlerThread;
    private final GUIState guiState;

    public InputLoop(GUIState guiState) {
        this.guiState = guiState;
    }

    public void start() {
        inputHandlerThread = new Thread(this);
        inputHandlerThread.start();
    }

    @Override
    public void run() {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        // TODO
    }
}
