package ca.claytonrogers.Common;

/**
 * Represents what part of the game the game is in so that it knows what actions to expect next.
 * <p>
 * Created by clayton on 2015-07-09.
 */
public enum GameState {
    Waiting_for_draw_selection,
    Draw_card_selected,
    Discard_card_selected,
    Draw_card_discarded,
    Game_Over;

    @Override
    public String toString() {
        switch (this) {
            case Waiting_for_draw_selection:
                return "Select the draw or discard pile.";
            case Draw_card_discarded:
            case Discard_card_selected:
                return "Select a card from the hand.";
            case Draw_card_selected:
                return "Select the discard pile or hand.";
            case Game_Over:
                return "Game Over.";
        }

        System.out.println("Could not find a string value for the enum: " + this.name());
        return "";
    }
}
