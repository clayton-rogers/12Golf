package ca.claytonrogers.Golf12.Client.GUIScene;

import ca.claytonrogers.Golf12.Client.GUIObjects.*;
import ca.claytonrogers.Golf12.Common.*;
import ca.claytonrogers.Golf12.Common.Messages.DiscardCardClicked;
import ca.claytonrogers.Golf12.Common.Messages.DrawCardClicked;
import ca.claytonrogers.Golf12.Common.Messages.HandSelection;
import ca.claytonrogers.Golf12.Common.Messages.Message;

import java.util.Collections;

/**
 * This scene shows the score screen and waits for the player to click the next round button.
 * <p>
 * Created by clayton on 2015-08-16.
 */
public class GameScreen extends Scene<SceneChange.NullPayloadType> {

    private static final IntVector DISCARD_PILE_LOCATION = Constants.FIELD_OFFSET.add(new IntVector(100, 180));
    private static final IntVector DRAW_PILE_LOCATION = Constants.FIELD_OFFSET.add(new IntVector(150, 180));
    private static final IntVector STATUS_STRING_LOCATION = Constants.FIELD_OFFSET.add(new IntVector(  0, 280));
    private static final IntVector[] HAND_LOCATIONS = new IntVector[4];
    static {
        HAND_LOCATIONS[0] = Constants.FIELD_OFFSET.add(new IntVector(  0,300));  // Bottom
        HAND_LOCATIONS[1] = Constants.FIELD_OFFSET.add(new IntVector(100,100));  // Left
        HAND_LOCATIONS[2] = Constants.FIELD_OFFSET.add(new IntVector(  0,  0));  // Top
        HAND_LOCATIONS[3] = Constants.FIELD_OFFSET.add(new IntVector(600,100));  // Right
    }
    private static final IntVector SCORE_SCREEN_BUTTON_LOCATION = Constants.FIELD_OFFSET.add(new IntVector(100,144));
    private static final IntVector SCORE_SCREEN_BUTTON_SIZE = new IntVector(80, 15);
    private static final String    SCORE_SCREEN_BUTTON_TEXT = "Score Screen";

    private final Connection serverConnection;
    private final String[] usernames;
    private final int playerNumber;
    private final int totalPlayers;

    private GolfGame game;

    private GUIHand[] guiHands;
    private GUIDeck drawPile;
    private GUIDeck discardPile;
    private GUIString statusString;
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

        initGame();
    }

    @Override
    public void startScene(SceneChange<SceneChange.NullPayloadType> sceneChange) {
        // If we already finished the game then we need to initialize a new one.
        // Otherwise there is nothing to do.
        if (game.isGameOver()) {
            initGame();
        }
    }

    private void initGame() {

        guiObjectList.clear();
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
                guiHands[0] = new GUIHand(state.getPlayerHands()[0], 0, getHandLocation(0));
                guiHands[1] = new GUIHand(state.getPlayerHands()[1], 2, getHandLocation(2));
            } else {
                guiHands[0] = new GUIHand(state.getPlayerHands()[0], 2, getHandLocation(2));
                guiHands[1] = new GUIHand(state.getPlayerHands()[1], 0, getHandLocation(0));
            }
        } else {
            int numPlayers = state.getNumberOfPlayers();
            for (int i = 0; i < numPlayers; i++) {
                int playerRelativePositionNumber = (i-playerNumber+numPlayers)%numPlayers;
                guiHands[i] = new GUIHand(state.getPlayerHands()[i], playerRelativePositionNumber, getHandLocation(playerRelativePositionNumber));
            }
        }
        Collections.addAll(guiObjectList, guiHands);

        drawPile = new GUIDeck(DRAW_PILE_LOCATION, state.getDrawPile(), GUIObject.Type.DrawPile);
        guiObjectList.add(drawPile);

        discardPile = new GUIDeck(DISCARD_PILE_LOCATION, state.getDiscardPile(), GUIObject.Type.DiscardPile);
        discardPile.setIsFaceUp(true); // The discard pile should always be face up and never change.
        guiObjectList.add(discardPile);

        statusString = new GUIString(STATUS_STRING_LOCATION);
        guiObjectList.add(statusString);

        scoreScreenButton = new GUIButton(
                SCORE_SCREEN_BUTTON_LOCATION,
                SCORE_SCREEN_BUTTON_SIZE,
                SCORE_SCREEN_BUTTON_TEXT,
                GUIObject.Type.ScoreScreenButton
        );
        guiObjectList.add(scoreScreenButton);
    }

    @Override
    public void handleInputs() {
        internalHandleMouseInputs();
        internalHandleServerInputs();
    }

    private void internalHandleMouseInputs() {
        // If it is not our turn then getNextGoodClick should not return anything.
        // This method handles one click each time it's called.

        String clickType = getNextGoodClickLocation();
        if (clickType == GUIObject.NONE_TYPE) {
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
                if (game.isGameOver()) {
                    nextScene = new SceneChange<>(
                            SceneType.Score,
                            new ScoreScreen.OptionalScores(true, game.getScores()));
                } else {
                    nextScene = new SceneChange<>(
                            SceneType.Score,
                            new ScoreScreen.OptionalScores(false, null));
                }
                break;
        }
        // Since we have now handled the click, throw it out.
        mouseClickList.poll();
    }

    private void internalHandleServerInputs() {
        while (true) {
            if (!serverConnection.isGood()) {
                nextScene = new SceneChange<SceneChange.NullPayloadType>(SceneType.LostConnection, null);
                return;
            }
            if (game.isGameOver()) {
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
        String statusMsg;
        if (!game.isGameOver()) {
            if (game.getPlayerTurn() == playerNumber) {
                statusMsg = game.getGameState().toString();
            } else {
                statusMsg = usernames[game.getPlayerTurn()] + "'s turn.";
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
                statusMsg = "Game Over! Game was a tie!";
            } else {
                statusMsg = "Game Over! " + usernames[winner] + " won!";
            }
        }
        statusString.setString(statusMsg);
    }

    private static IntVector getHandLocation(int position) {
        return HAND_LOCATIONS[position];
    }
}
