package ca.claytonrogers.Client;

import ca.claytonrogers.Common.GUIObject;
import ca.claytonrogers.Common.IntVector;

import javax.swing.*;

/**
 * Created by clayton on 2015-07-05.
 */
public class Application extends JFrame implements Runnable{

    private static final IntVector WINDOW_BOUNDS = new IntVector(700,700);

    private InputLoop inputLoop;
    private final GUIState guiState = new GUIState();

    public Application() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WINDOW_BOUNDS.x, WINDOW_BOUNDS.y);
        setVisible(true);
        inputLoop = new InputLoop(guiState);
        inputLoop.start();
        addMouseListener(inputLoop);

        createBufferStrategy(2);
    }

    @Override
    public void run() {
        // TODO draw loop
    }
}
