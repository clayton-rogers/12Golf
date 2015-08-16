package ca.claytonrogers.Client.GUIScene;

import ca.claytonrogers.Client.GUIObjects.*;
import ca.claytonrogers.Common.Connection;
import ca.claytonrogers.Common.Constants;
import ca.claytonrogers.Common.GolfGame;
import ca.claytonrogers.Common.Messages.DiscardCardClicked;
import ca.claytonrogers.Common.Messages.DrawCardClicked;
import ca.claytonrogers.Common.Messages.HandSelection;
import ca.claytonrogers.Common.Messages.Message;
import ca.claytonrogers.Common.State;

import javax.swing.*;
import java.io.PrintWriter;

/**
 * Created by clayton on 2015-08-16.
 */
public class GameScreen extends Scene<SceneChange.NullPayloadType> {

    private Connection serverConnection;
    private String[] usernames;
    private int playerNumber;
    private int totalPlayers;

    private GolfGame game;

    private GUIHand[] guiHands;
    private GUIDeck drawPile;
    private GUIDeck discardPile;
    private GUIStatusString statusString;
    private GUIButton scoreScreenButton;

    public GameScreen(
            Connection serverConnection,
            String[] usernames,
            int playerNumber,
            int totalPlayers) {
        this.serverConnection = serverConnection;
        this.usernames = usernames;
        this.playerNumber = playerNumber;
        this.totalPlayers = totalPlayers;

        // The next game state can be independently generated on each client because
        // they were all seeded with the same value and have all kept the same state.
        State state = new State(totalPlayers);
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
    }

    @Override
    public void startScene(SceneChange<SceneChange.NullPayloadType> sceneChange) {
        // TODO
        System.exit(1);
    }

    @Override
    public void handleInputs() {

        // *** HANDLE MOUSE/USER INPUTS *** //
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
                nextScene = new SceneChange<>(SceneType.Score, game.getScores());
                break;
        }
        // Since we have now handled the click, throw it out.
        mouseClickList.poll();

        // *** HANDLE SERVER INPUTS *** //
        while (true) {
            if (!serverConnection.isGood()) {
                JOptionPane.showMessageDialog(null, "Connection to the server has been lost...");
                nextScene = new SceneChange<SceneChange.NullPayloadType>(SceneType.Quit, null);
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

    @Override
    public void processState() {

        scoreScreenButton.setVisibility(game.isGameOver());

        // Clickability update
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

        // Status update
        String statusMsg = "";
        if (!game.isGameOver()) {
            if (game.getPlayerTurn() == playerNumber) {
                statusMsg = "Your turn: " + game.getGameState();
            } else {
                statusMsg = "Not your turn. Waiting for " + usernames[game.getPlayerTurn()] + "...";
            }
        } else {
            int[] scores = game.getScores();
            int winner = -1;
            int winnerScore = 1000; // Best scores are low
            boolean isTie = false;
            for (int i = 0; i < scores.length; i++) {
                if (scores[i] < winnerScore) {
                    winner = i;
                    winnerScore = scores[i];
                    isTie = false;
                } else if (scores[i] == winnerScore) {
                    isTie = true;
                }
            }
            if (isTie) {
                statusMsg = "Game Over! " + usernames[winner] + " won!";
            } else {
                statusMsg = "Game Over! Game was a tie!";
            }
        }
        statusString.setString(statusMsg);
    }
}