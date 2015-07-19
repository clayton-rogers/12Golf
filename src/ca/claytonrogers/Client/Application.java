package ca.claytonrogers.Client;

import ca.claytonrogers.Client.GUIObjects.*;
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

    private Queue<IntVector> mouseClickList = new ConcurrentLinkedQueue<>();
    private Connection serverConnection;
    private List<GUIObject> guiObjectList = new ArrayList<>(6);

    private String[] usernames;
    private int playerNumber;
    private int totalPlayers;
    private GolfGame game;
    private boolean isOnScoreScreen = false;

    private GUIHand[] guiHands;
    private GUIDeck drawPile;
    private GUIDeck discardPile;
    private GUIStatusString statusString;
    private GUIButton scoreScreenButton;
    private GUIButton nextGameButton;
    private GUIScoreCard scoreCard;

    public Application() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WINDOW_BOUNDS.x, WINDOW_BOUNDS.y);
        setVisible(true);
        InputHandler inputHandler = new InputHandler(this);
        addMouseListener(inputHandler);

        // TODO FUTURE For some reason you have to sometime wait for a bit before creating
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
        usernames = new String[totalPlayers];
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
        // Since the score card is persistent across rounds,
        // it is initialized outsize the main initialization method.
        scoreCard = new GUIScoreCard(usernames);
        initialiseGame();

        while (true) {
            long frameStartTime = System.currentTimeMillis();

            handleMouseInputs();
            handleServerMessages();
            processState();
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

            g.setColor(Constants.BACKGROUND_COLOR);
            g.fillRect(0, 0, WINDOW_BOUNDS.x, WINDOW_BOUNDS.y);

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
                throw new IllegalStateException("Did not receive initial state from server. " + message.getMessageType());
            }
            state = ((StateUpdate)message).getState();
        }

        guiObjectList.clear();
        game = new GolfGame(state);

        // Initialise the GUI objects and add them to the draw list
        guiHands = new GUIHand[totalPlayers];
        if (state.getNumberOfPlayers() == 2) {
            // If there's only two players, we want them sitting across from each other.
            // With the self player on the bottom.
            if (playerNumber == 0) {
                guiHands[0] = new GUIHand(state.getPlayerHands()[0], 0);
                guiHands[1] = new GUIHand(state.getPlayerHands()[1], 2);
            } else {
                guiHands[0] = new GUIHand(state.getPlayerHands()[0], 2);
                guiHands[1] = new GUIHand(state.getPlayerHands()[1], 0);
            }
        } else {
            int numPlayers = state.getNumberOfPlayers();
            for (int i = 0; i < numPlayers; i++) {
                guiHands[i] = new GUIHand(state.getPlayerHands()[i], (i-playerNumber+numPlayers)%numPlayers);
            }
        }
        for (GUIHand hand : guiHands) {
            guiObjectList.add(hand);
        }

        drawPile = new GUIDeck(Constants.DRAW_PILE_LOCATION, state.getDrawPile(), GUIObject.Type.DrawPile);
        guiObjectList.add(drawPile);

        discardPile = new GUIDeck(Constants.DISCARD_PILE_LOCATION, state.getDiscardPile(), GUIObject.Type.DiscardPile);
        discardPile.setIsFaceUp(true); // The discard pile should always be face up and never change.
        guiObjectList.add(discardPile);

        statusString = new GUIStatusString();
        guiObjectList.add(statusString);

        scoreScreenButton = new GUIButton(
                Constants.SCORE_SCREEN_BUTTON_LOCATION,
                Constants.SCORE_SCREEN_BUTTON_SIZE,
                Constants.SCORE_SCREEN_BUTTON_TEXT,
                GUIObject.Type.ScoreScreenButton
        );
        guiObjectList.add(scoreScreenButton);

        nextGameButton = new GUIButton(
                Constants.NEXT_GAME_BUTTON_LOCATION,
                Constants.NEXT_GAME_BUTTON_SIZE,
                Constants.NEXT_GAME_BUTTON_TEXT,
                GUIObject.Type.NextGameButton
        );
        guiObjectList.add(nextGameButton);

        // We need to re add the score card to the gui list
        // but we do not reinitialize it.
        guiObjectList.add(scoreCard);
    }

    private void drawWaitingForOtherPlayersScreen() {

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

    private void handleMouseInputs() {
        // If it is not our turn then getNextGoodClick should not return anything.
        // This method handles one click each time it's called.

        GUIObject.Type clickType = getNextGoodClickLocation();
        if (clickType == GUIObject.Type.None) {
            return;
        }

        Message msg;
        switch (clickType) {
            case DrawPile:
                game.chooseDrawPile();
                drawPile.setIsFaceUp(true);

                msg = new DrawCardClicked();
                serverConnection.send(msg);
                break;
            case DiscardPile:
                game.chooseDiscardPile();
                drawPile.setIsFaceUp(false);

                msg = new DiscardCardClicked();
                serverConnection.send(msg);
                break;
            case Hand:
                int cardIndex = guiHands[playerNumber].getClickedCard(mouseClickList.peek());
                if (cardIndex == -1) {
                    // This means that the hand area was clicked but an actual card wasn't.
                    break;
                }
                game.chooseHandCard(cardIndex);
                drawPile.setIsFaceUp(false);

                msg = new HandSelection(cardIndex);
                serverConnection.send(msg);
                break;
            case ScoreScreenButton:
                isOnScoreScreen = true;
                scoreCard.scoreCard.add(game.getScores());
                break;
            case NextGameButton:
                isOnScoreScreen = false;
                drawWaitingForOtherPlayersScreen();
                initialiseGame();

                break;
        }
        // Since we have now handled the click, throw it out.
        mouseClickList.poll();
    }

    private GUIObject.Type getNextGoodClickLocation() {
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

    private void handleServerMessages() {
        while (true) {
            if (isOnScoreScreen) {
                return;
            }
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
                    drawPile.setIsFaceUp(true);
                    break;
                case DiscardCardClicked:
                    game.chooseDiscardPile();
                    drawPile.setIsFaceUp(false);
                    break;
                case HandSelection:
                    int cardIndex = ((HandSelection)message).getCardSelectionIndex();
                    game.chooseHandCard(cardIndex);
                    drawPile.setIsFaceUp(false);
                    break;
                default:
                    System.out.println("Got a message that we didn't expect: " + message.getMessageType());
                    break;
            }
        }
    }

    private void processState() {
        // Update visibilities
        scoreCard.setVisibility(isOnScoreScreen);
        nextGameButton.setVisibility(isOnScoreScreen);

        for (GUIObject hand : guiHands) {
            hand.setVisibility(!isOnScoreScreen);
        }
        drawPile.setVisibility(!isOnScoreScreen);
        discardPile.setVisibility(!isOnScoreScreen);
        scoreScreenButton.setVisibility(!isOnScoreScreen && game.isGameOver());

        // Clickability update]
        if (!isOnScoreScreen) {
            if (game.getPlayerTurn() != playerNumber ||
                    game.isGameOver()) {
                // It's not my turn, so nothing should be clickable.
                guiHands[playerNumber].setClickability(false);
                drawPile.setClickability(false);
                discardPile.setClickability(false);
            } else {
                switch (game.getGameState()) {
                    case Waiting_for_draw_selection:
                        guiHands[playerNumber].setClickability(false);
                        drawPile.setClickability(true);
                        discardPile.setClickability(true);
                        break;
                    case Draw_card_selected:
                        guiHands[playerNumber].setClickability(true);
                        drawPile.setClickability(false);
                        discardPile.setClickability(true);
                        break;
                    case Discard_card_selected:
                    case Draw_card_discarded:
                        guiHands[playerNumber].setClickability(true);
                        drawPile.setClickability(false);
                        discardPile.setClickability(false);
                        break;
                }
            }
        }

        // Status update
        String statusMsg = "";
        if (!game.isGameOver()) {
            if (game.getPlayerTurn() == playerNumber) {
                statusMsg += "Your turn: ";
            } else {
                statusMsg += "Not your turn: ";
            }
            statusMsg += game.getGameState();
        } else {
            statusMsg = "Game Over!";
        }
        statusString.setString(statusMsg);
    }
}
