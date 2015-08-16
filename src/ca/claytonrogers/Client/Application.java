package ca.claytonrogers.Client;

import ca.claytonrogers.Client.GUIObjects.*;
import ca.claytonrogers.Client.GUIScene.GameScreen;
import ca.claytonrogers.Client.GUIScene.Scene;
import ca.claytonrogers.Client.GUIScene.SceneChange;
import ca.claytonrogers.Client.GUIScene.ScoreScreen;
import ca.claytonrogers.Common.*;
import ca.claytonrogers.Common.Messages.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by clayton on 2015-07-05.
 */
public class Application extends JFrame implements Runnable {

    private static final int FRAME_TIME = 17;  // Frame time in ms
    private static final IntVector WINDOW_BOUNDS = new IntVector(400,550);

    private Connection serverConnection;
    private String[] usernames;
    private int playerNumber;
    private int totalPlayers;

    private boolean isRunning = true;

    private Scene currentScene;
    private Scene scoreScreen;
    private Scene gameScreen;

    public Application() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WINDOW_BOUNDS.x, WINDOW_BOUNDS.y);
        setVisible(true);
        InputHandler inputHandler = new InputHandler(this);
        addMouseListener(inputHandler);

        // TODO FUTURE For some reason you have to sometime wait for a bit before creating
        // Apparently the create should be called in the EDT
        // See: http://stackoverflow.com/questions/3435994/buffers-have-not-been-created-whilst-creating-buffers
        // the buffer strategy, otherwise it will throw an exception.
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            System.out.println("Interrupted while waiting for the buffer to be created.");
        }

        createBufferStrategy(2);

        // Cause the window to be spawned in the middle of the screen.
        setLocationRelativeTo(null);

        // Set the title bar
        setTitle("12Golf");

        // Set the Icon
        Image icon;
        try {
            icon = ImageIO.read(new File("res/Golf Icon.png"));
            setIconImage(icon);
        } catch (IOException e) {
            System.out.println("Could not find the icon.");
        }
    }

    @Override
    public void run() {
        String username = JOptionPane.showInputDialog("Enter a username:");

        // Try to get a socket open with the server
        Socket socket;
        try {
            socket = new Socket(Constants.ADDRESS, Constants.PORT_NUMBER);
        } catch (IOException e) {
            System.out.println("Could not connect to the default server.");
            try {
                String address = JOptionPane.showInputDialog("Enter the server address:");
                socket = new Socket(address, Constants.PORT_NUMBER);
            } catch (IOException e2) {
                System.out.println("Could not connect to the user entered server.");
                return;
            }
        }

        // Create a connection object from the socket
        serverConnection = new Connection(socket);

        // By now, we're connected, so tell the user we're just waiting on the other users.
        // TODO this should be a scene too
        drawWaitingForOtherPlayersScreen();

        // Send the version information
        VersionInformation version = new VersionInformation(Constants.VERSION);
        serverConnection.send(version);

        // Get the next message and deal with version mismatch if it occurs.
        Message message = serverConnection.waitForNextMessage();
        switch (message.getMessageType()) {
            case VersionInformationMismatch:
                VersionInformationMismatch msg = (VersionInformationMismatch) message;
                String error = "There was a version mismatch. Client:" + msg.getClientVersionNumber()
                        + " Server:" + msg.getServerVersionNumber();
                System.out.println(error);
                JOptionPane.showMessageDialog(this, error);
                return;
            case VersionInformationAuthenticated:
                break;
            default:
                System.out.println("Received something other than version validation: " + message.getMessageType());
                return;
        }

        // Get player info
        message = serverConnection.waitForNextMessage();
        if (message.getMessageType() != Message.MessageType.PlayerInfo) {
            System.out.println("Received something other than player info: " + message.getMessageType());
            return;
        }
        playerNumber = ((PlayerInfo) message).getPlayerNumber();
        totalPlayers = ((PlayerInfo) message).getTotalPlayers();

        // Send/receive the game seed
        long seed;
        if (playerNumber == 0) {
            seed = System.currentTimeMillis();
            message = new Seed(seed);
            serverConnection.send(message);
        } else {
            message = serverConnection.waitForNextMessage();
            if (message.getMessageType() != Message.MessageType.Seed) {
                System.out.println("Received something other than the game seed: " + message.getMessageType());
                return;
            }
            seed = ((Seed) message).getSeed();
        }
        Constants.random = new Random(seed);

        // Send the username
        Username usernameMessage = new Username(username, playerNumber);
        serverConnection.send(usernameMessage);

        // Fill out the username database
        usernames = new String[totalPlayers];
        usernames[playerNumber] = username;
        for (int i = 0; i < totalPlayers-1; i++) {
            usernameMessage = (Username)serverConnection.waitForNextMessage();
            usernames[usernameMessage.getPlayerNumber()] = usernameMessage.getUsername();
        }

        drawLoop();

        System.exit(1);
    }

    public void addClick(IntVector clickLocation) {
        currentScene.addClick(clickLocation);
    }

    private void drawLoop () {
        // Since the score card is persistent across rounds,
        // it is initialized outsize the main initialization method.
        scoreScreen = new ScoreScreen(usernames);
        initialiseGame();
        currentScene = gameScreen;

        while (isRunning) {
            long frameStartTime = System.currentTimeMillis();

            currentScene.handleInputs();
            currentScene.processState();
            currentScene.drawScreen(this, WINDOW_BOUNDS);
            SceneChange sceneChange = currentScene.getNextScene();
            if (sceneChange != null) {
                switch (sceneChange.getNextScene()) {
                    case Score:
                        currentScene = scoreScreen;
                        break;
                    case Game:
                        break;
                    case MainMenu:
                        break;
                    case Options:
                        break;
                    case Quit:
                        isRunning = false;
                        break;
                }
                currentScene.startScene(sceneChange);
            }

            long frameEndTime = System.currentTimeMillis();
            waitForNextFrame(frameStartTime, frameEndTime);
        }
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

    private void initialiseGame() {
        gameScreen = new GameScreen(serverConnection, usernames, playerNumber, totalPlayers);
    }

    private void drawWaitingForOtherPlayersScreen() {
        // TODO make scene

        BufferStrategy bf = getBufferStrategy();
        Graphics g = null;

        try {
            g = bf.getDrawGraphics();

            g.setColor(Constants.BACKGROUND_COLOR);
            g.fillRect(0, 0, WINDOW_BOUNDS.x, WINDOW_BOUNDS.y);

            g.setColor(Constants.CARD_FOREGROUND_COLOR);
            g.drawString(
                    "Waiting for leader to start the game...",
                    Constants.WAITING_FOR_PLAYERS.x,
                    Constants.WAITING_FOR_PLAYERS.y);

        } finally {
            if (g != null) {
                g.dispose();
            }
        }

        bf.show();
        Toolkit.getDefaultToolkit().sync();
    }
}
