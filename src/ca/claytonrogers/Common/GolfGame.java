package ca.claytonrogers.Common;

/**
 * Created by clayton on 2015-07-10.
 */
public class GolfGame {
    private State state;
    private GameState gameState = GameState.Waiting_for_draw_selection;
    private int playerTurn = 0;

    public GolfGame(State state) {
        this.state = state;
    }

    public void chooseDrawPile() {
        if (gameState != GameState.Waiting_for_draw_selection) {
            System.out.println("Tried to choose the draw pile while in state: " + gameState);
        }

        gameState = GameState.Draw_card_selected;
    }

    public void chooseDiscardPile() {
        if (gameState == GameState.Waiting_for_draw_selection) {
            gameState = GameState.Discard_card_selected;
        } else if (gameState == GameState.Draw_card_selected) {
            gameState = GameState.Draw_card_discarded;
        } else {
            System.out.println("Tried to choose the draw pile while in state: " + gameState);
        }
    }

    public void chooseHandCard(int cardIndex) {
        if (gameState == GameState.Discard_card_selected) {
            Card temp = state.getPlayerHands()[playerTurn].replaceCard(cardIndex, state.getDiscardPile().pop());
            state.getDiscardPile().push(temp);
        } else if (gameState == GameState.Draw_card_selected) {
            Card temp = state.getPlayerHands()[playerTurn].replaceCard(cardIndex, state.getDrawPile().pop());
            state.getDiscardPile().push(temp);
        } else if (gameState == GameState.Draw_card_discarded) {
            state.getDiscardPile().push(state.getDrawPile().pop());
            state.getPlayerHands()[playerTurn].flipCard(cardIndex);
        } else {
            System.out.println("Tried to choose a card from the hand while in state: " + gameState);
            return;
        }
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

    public GameState getGameState() {
        return gameState;
    }
}
