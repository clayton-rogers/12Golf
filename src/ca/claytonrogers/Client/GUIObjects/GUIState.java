package ca.claytonrogers.Client.GUIObjects;

import ca.claytonrogers.Common.Constants;
import ca.claytonrogers.Common.Hand;
import ca.claytonrogers.Common.State;

import java.awt.*;

/**
 * Created by clayton on 2015-07-05.
 */
public class GUIState extends GUIObject {

    private State state;
    private GUIHand[] hands;
    private GUIDeck drawPile;
    private GUIDeck discardPile;

    public GUIState(State state) {
        super(Constants.NULL_LOCATION, Constants.NULL_LOCATION);

        this.state = state;

        Hand[] tempHands = state.getPlayerHands();
        int numberOFPlayers = tempHands.length;
        hands = new GUIHand[numberOFPlayers];
        for (int i = 0; i < 4; i++) {
            hands[i] = new GUIHand(tempHands[i], i);
        }

        drawPile = new GUIDeck(Constants.DRAW_PILE_LOCATION, state.getDrawPile());
        discardPile = new GUIDeck(Constants.DISCARD_PILE_LOCATION, state.getDiscardPile());
    }

    @Override
    public void draw(Graphics g) {
        for (GUIHand hand : hands) {
            hand.draw(g);
        }
        drawPile.draw(g);
        discardPile.draw(g);
    }
}
