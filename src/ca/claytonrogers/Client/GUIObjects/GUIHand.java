package ca.claytonrogers.Client.GUIObjects;

import ca.claytonrogers.Common.Constants;
import ca.claytonrogers.Common.Hand;
import ca.claytonrogers.Common.IntVector;

import java.awt.*;

/**
 * Created by clayton on 2015-07-05.
 */
public class GUIHand extends GUIObject {

    private Hand hand;
    private int position = 0; // 0-3 for the different players

    public GUIHand(Hand hand, int position) {
        super(position == 0 ? Constants.BOTTOM_HAND_OFFSET : Constants.TOP_HAND_OFFSET,
                Constants.HAND_SIZE);
        this.hand = hand;
        this.position = position;
    }

    public int getClickedCard(IntVector clickLocation) {
        if (!this.checkClicked(clickLocation)) {
            throw new IllegalArgumentException("Called getClickedCard for a position outside of its bounds.");
        }

        IntVector relativePos = clickLocation.sub(location);

        // TODO find the card that was actually clicked
        switch (position) {
            case 0:
            case 2:
                // TODO

                break;
            case 1:
            case 3:
                throw new IllegalStateException("No side players allowed.");
                //break;
        }

        // Could not find a clicked card
        return -1;
    }

    @Override
    public void draw(Graphics2D g) {
        switch (position) {
            case 0:
                // TODO draw down position
                break;
            case 1:
                throw new IllegalStateException("Cannot be a player 1");
            case 2:
                // TODO draw top position
                break;
            case 3:
                throw new IllegalStateException("Cannot be a player 3");
        }
    }

    private void drawCard(Graphics2D g, int cardIndex) {
        // TODO draw card.
    }
}
