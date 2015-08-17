package ca.claytonrogers.Common;

/**
 * Created by clayton on 2015-07-10.
 */
public class GolfGame {
    private final State state;
    private GameState gameState = GameState.Waiting_for_draw_selection;
    private int playerTurn = 0;
    private boolean isLastTurn = false;
    private int playerWhoEndedIt = -1;

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
            state.getDiscardPile().push(state.getDrawPile().pop());
            gameState = GameState.Draw_card_discarded;
        } else {
            System.out.println("Tried to choose the draw pile while in state: " + gameState);
        }
    }

    public void chooseHandCard(int cardIndex) {
        if (gameState == GameState.Discard_card_selected) {
            Card discardedCard = state.getDiscardPile().pop();
            discardedCard.isFaceUp = true;
            Card cardToBeDiscarded = state.getPlayerHands()[playerTurn].replaceCard(cardIndex, discardedCard);
            state.getDiscardPile().push(cardToBeDiscarded);
        } else if (gameState == GameState.Draw_card_selected) {
            Card drawCard = state.getDrawPile().pop();
            drawCard.isFaceUp = true;
            Card cardToBeDiscarded = state.getPlayerHands()[playerTurn].replaceCard(cardIndex, drawCard);
            state.getDiscardPile().push(cardToBeDiscarded);
        } else if (gameState == GameState.Draw_card_discarded) {
            state.getPlayerHands()[playerTurn].flipCard(cardIndex);
        } else {
            System.out.println("Tried to choose a card from the hand while in state: " + gameState);
            return;
        }
        gameState = GameState.Waiting_for_draw_selection;
        nextTurn();
    }

    private void nextTurn() {
        if (isLastTurn) {
            incrementPlayer();
            if (playerTurn == playerWhoEndedIt) {
                gameState = GameState.Game_Over;
                for (Hand hand : state.getPlayerHands()) {
                    hand.flipRestOfCards();
                }
            }
        } else {
            Hand hand = state.getPlayerHands()[playerTurn];
            if (hand.isAllFlippedOver()) {
                playerWhoEndedIt = playerTurn;
                isLastTurn = true;
            }
            incrementPlayer();
        }
        if (state.getDrawPile().size() == 0) {
            shuffleDiscard();
        }
    }

    private void shuffleDiscard() {
        Deck discard = state.getDiscardPile();
        Deck draw = state.getDrawPile();

        Card topDiscardCard = discard.pop();
        int discardSize = discard.size();
        for (int i = 0; i < discardSize; i++) {
            draw.push(discard.pop());
        }
        discard.push(topDiscardCard);
        draw.shuffle(Constants.random);
    }

    private void incrementPlayer() {
        playerTurn++;
        playerTurn %= state.getNumberOfPlayers();
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean isGameOver () {
        return gameState == GameState.Game_Over;
    }

    public int[] getScores () {
        int[] scores = new int[state.getNumberOfPlayers()];
        for (int i = 0; i < state.getNumberOfPlayers(); i++) {
            scores[i] = state.getPlayerHands()[i].getHandScore();
        }

        return scores;
    }
}
