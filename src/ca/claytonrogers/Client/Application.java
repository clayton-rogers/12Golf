package ca.claytonrogers.Client;

import ca.claytonrogers.Client.GUIObjects.GUIState;
import ca.claytonrogers.Common.IntVector;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by clayton on 2015-07-05.
 */
public class Application extends JFrame implements Runnable {

    private static final int FRAME_TIME = 17;  // Frame time in ms
    private static final IntVector WINDOW_BOUNDS = new IntVector(700,700);

    private InputHandler inputHandler;
    private GUIState guiState;
    private Queue<IntVector> mouseClickList = new ConcurrentLinkedQueue<>();

    public Application() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WINDOW_BOUNDS.x, WINDOW_BOUNDS.y);
        setVisible(true);
        inputHandler = new InputHandler(this);
        addMouseListener(inputHandler);

        createBufferStrategy(2);
    }

    @Override
    public void run() {
        // TODO get the username
        // TODO get a connection
        // TODO send version info

        drawLoop();
    }

    public void addClick(IntVector clickLocation) {
        mouseClickList.add(clickLocation);
    }

    private void drawLoop () {
        boolean gameOver = false;
        while (!gameOver) {
            long frameStartTime = System.currentTimeMillis();

            // TODO handle inputs

            // TODO handle server messages

            drawScreen();

            long frameEndTime = System.currentTimeMillis();
            waitForNextFrame(frameStartTime, frameEndTime);
        }
    }

    private void drawScreen () {
        BufferStrategy bf = getBufferStrategy();
        Graphics g = null;

        try {
            g = bf.getDrawGraphics();

            guiState.draw(g);

        } finally {
            if (g != null) {
                g.dispose();
            }
        }

        bf.show();
        Toolkit.getDefaultToolkit().sync();
    }

    private void waitForNextFrame (long startTime, long endTime) {
        long waitTime = FRAME_TIME - (endTime - startTime);
        if (waitTime < 0L) {
            waitTime = 0L;
        }

        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            System.out.println("The draw loop was interrupted: " + e);
        }
    }
}
