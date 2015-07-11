package ca.claytonrogers.Common;

/**
 * Created by clayton on 2015-07-10.
 */
public class GolfGame {
    private State state;
    private GameState gameState = GameState.Waiting_for_draw_card;
    private int playerTurn;

    public GolfGame(State state) {
        this.state = state;
    }

    public void chooseDrawPile() {
        if (gameState != GameState.Waiting_for_draw_card) {
            System.out.println("Tried to choose the draw pile while in state: " + gameState);
        }

        gameState = GameState.Draw_card_waiting_for_hand_selection;
    }

    public void chooseDiscardPile() {
        if (gameState != GameState.Waiting_for_draw_card) {
            System.out.println("Tried to choose the draw pile while in state: " + gameState);
        }

        gameState = GameState.Discard_card_waiting_for_hand_selection;
    }

    public void chooseHandCard(int cardIndex) {
        if (gameState == GameState.Discard_card_waiting_for_hand_selection) {
            Card temp = state.getPlayerHands()[playerTurn].replaceCard(cardIndex, state.getDiscardPile().pop());
            state.getDiscardPile().push(temp);
        } else if (gameState == GameState.Draw_card_waiting_for_hand_selection) {
            Card temp = state.getPlayerHands()[playerTurn].replaceCard(cardIndex, state.getDrawPile().pop());
            state.getDiscardPile().push(temp);
        } else {
            System.out.println("Tried to choose a card from the hand while in state: " + gameState);
        }
        nextTurn();
    }

    public void discardDrawAndFlipHand(int cardIndex) {
        if (gameState != GameState.Draw_card_waiting_for_hand_selection) {
            System.out.println("Tried to discard the draw card when in state: " + gameState);
        }

        state.getDiscardPile().push(state.getDrawPile().pop());
        state.getPlayerHands()[playerTurn].flipCard(cardIndex);

        nextTurn();
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    private void nextTurn() {
        playerTurn++;
        playerTurn %= state.getNumberOfPlayers();
    }
}
