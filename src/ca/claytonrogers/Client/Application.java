package ca.claytonrogers.Client;

import ca.claytonrogers.Client.GUIObjects.GUIDeck;
import ca.claytonrogers.Client.GUIObjects.GUIHand;
import ca.claytonrogers.Client.GUIObjects.GUIObject;
import ca.claytonrogers.Common.*;
import ca.claytonrogers.Common.Messages.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
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
    private static final IntVector WINDOW_BOUNDS = new IntVector(700,700);

    private Queue<IntVector> mouseClickList = new ConcurrentLinkedQueue<>();
    private Connection serverConnection;
    private List<GUIObject> guiObjectList = new ArrayList<>(6);

    private String[] usernames;
    private int playerNumber;
    private int totalPlayers;
    private GolfGame game;

    public Application() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WINDOW_BOUNDS.x, WINDOW_BOUNDS.y);
        setVisible(true);
        InputHandler inputHandler = new InputHandler(this);
        addMouseListener(inputHandler);

        createBufferStrategy(2);
    }

    @Override
    public void run() {
        String username = JOptionPane.showInputDialog("Enter a username:");

        // Try to get a socket open with the server
        Socket socket;
        try {
            if (Constants.DEBUG) {
                socket = new Socket("localhost", Constants.PORT_NUMBER);
            } else {
                socket = new Socket(Constants.ADDRESS, Constants.PORT_NUMBER);
            }
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
                playerNumber = ((VersionInformationAuthenticated) message).getPlayerNumber();
                totalPlayers = ((VersionInformationAuthenticated) message).getTotalPlayers();
                break;
            default:
                System.out.println("Received something other than version validation: " + message.getMessageType());
                return;
        }

        // Send the username
        Username usernameMessage = new Username(username, playerNumber);
        serverConnection.send(usernameMessage);

        // Fill out the username database
        usernames[playerNumber] = username;
        for (int i = 0; i < totalPlayers-1; i++) {
            usernameMessage = (Username)serverConnection.waitForNextMessage();
            usernames[usernameMessage.getPlayerNumber()] = usernameMessage.getUsername();
        }

        drawLoop();
    }

    public void addClick(IntVector clickLocation) {
        mouseClickList.add(clickLocation);
    }

    private void drawLoop () {
        initialiseGame();

        boolean gameOver = false;
        while (!gameOver) {
            long frameStartTime = System.currentTimeMillis();

            handleMouseInputs();
            handleServerMessages();
            updateClickability();
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

        // Player 0 will always be the one to create the initial game state.
        Message message;
        State state;
        if (playerNumber == 0) {
            state = new State(totalPlayers);
            message = new StateUpdate(state);
            serverConnection.send(message);
        } else {
            message = serverConnection.waitForNextMessage();
            if (message.getMessageType() != Message.MessageType.StateUpdate) {
                System.out.println("Did not receive initial state from server. " + message.getMessageType());
                return;
            }
            state = ((StateUpdate)message).getState();
        }

        game = new GolfGame(state);

        // Initialise the GUI objects and add them to the draw list
        GUIHand[] guiHands = new GUIHand[totalPlayers];
        if (state.getNumberOfPlayers() == 2) {
            // if there's only two players, we want them sitting across from each other.
            guiHands[0] = new GUIHand(state.getPlayerHands()[0],0);
            guiHands[1] = new GUIHand(state.getPlayerHands()[1],2);
        } else {
            for (int i = 0; i < state.getNumberOfPlayers(); i++) {
                guiHands[i] = new GUIHand(state.getPlayerHands()[i], i);
            }
        }
        for (GUIHand hand : guiHands) {
            guiObjectList.add(hand);
        }

        GUIDeck drawPile = new GUIDeck(Constants.DRAW_PILE_LOCATION, state.getDrawPile(), GUIObject.Type.DrawPile);
        guiObjectList.add(drawPile);

        GUIDeck discardPile = new GUIDeck(Constants.DISCARD_PILE_LOCATION, state.getDiscardPile(), GUIObject.Type.DiscardPile);
        guiObjectList.add(discardPile);
    }

    private void drawWaitingForOtherPlayersScreen() {
        // TODO
    }

    private void handleMouseInputs() {
        // If it is not our turn then getNextGoodClick should not return anything.

        GUIObject.Type clickType = getNextGoodClickLocation();
        if (clickType == GUIObject.Type.None) {
            return;
        }

        Message msg;
        switch (clickType) {
            case DrawPile:
                game.chooseDrawPile();
                msg = new DrawCardClicked();
                serverConnection.send(msg);
                break;
            case DiscardPile:
                game.chooseDiscardPile();
                msg = new DiscardCardClicked();
                serverConnection.send(msg);
                break;
            case Hand:
                int cardIndex = getHandClick();
                game.chooseHandCard(cardIndex);
                msg = new HandSelection(cardIndex);
                serverConnection.send(msg);
                break;
        }
    }

    private GUIObject.Type getNextGoodClickLocation() {
        // TODO
        IntVector clickLocation = mouseClickList.poll();
        for (GUIObject object : guiObjectList) {
            if (object.checkClicked(clickLocation)) {
                // TODO handle what happens next based on type of hit and
                switch (object.getType()) {
                    case DrawPile:
                        break;
                    case DiscardPile:
                        break;
                    case Hand:
                        break;
                }
            }
        }
        return GUIObject.Type.None;
    }

    private int getHandClick() {
        // TODO
        return 0;
    }

    private void handleServerMessages() {
        while (true) {
            Message message = serverConnection.getMessage();
            if (message == null) {
                break;
            }

            if (game.getPlayerTurn() == playerNumber) {
                // Why are we getting messages while it's our turn???
                System.out.println("Received a message while it was our turn: " + message.getMessageType());
                continue;
            }

            switch (message.getMessageType()) {
                case DrawCardClicked:
                    game.chooseDrawPile();
                    break;
                case DiscardCardClicked:
                    game.chooseDiscardPile();
                    break;
                case HandSelection:
                    int cardIndex = ((HandSelection)message).getCardSelectionIndex();
                    game.chooseHandCard(cardIndex);
                    break;
                default:
                    System.out.println("Got a message that we didn't expect: " + message.getMessageType());
                    break;
            }
        }
    }

    private void updateClickability() {
        // TODO
    }
}
