package ca.claytonrogers.Client.GUIObjects;

import ca.claytonrogers.Common.Card;
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
        super(
                getHandLocation(position),
                Constants.HAND_SIZE,
                Type.Hand);
        this.hand = hand;
        this.position = position;
    }

    private static IntVector getHandLocation(int position) {
        return Constants.HAND_LOCATIONS[position];
    }

    public int getClickedCard(IntVector clickLocation) {
        if (!this.checkClicked(clickLocation)) {
            throw new IllegalArgumentException("Called getClickedCard for a position outside of its bounds.");
        }

        // TODO check where the player is sitting and move the hands around to place the player at the bottom


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
    protected void internalDraw(Graphics g) {
        if (position == 0 || position == 2) {
            drawHorizontalHand(g);
        } else {
            drawVerticalHand(g);
        }
    }

    private void drawHorizontalHand(Graphics g) {
        final int handWidth = Constants.NUMBER_OF_CARDS/2;
        IntVector cardLocation = location;
        for (int i = 0; i < handWidth; i++) {
            for (int row = 0; row < 2; row ++) {
                Card card = hand.getCard(i + row*handWidth);
                Drawer.drawCard(g, card, cardLocation);

                // Increment the card location to the right
                cardLocation = cardLocation.add(new IntVector(Constants.SPACE_BETWEEN_CARDS + Constants.CARD_SIZE.x,0));
            }
            // Increment the card location down a row
            cardLocation = location.add(new IntVector(0,Constants.SPACE_BETWEEN_CARDS + Constants.CARD_SIZE.y));
        }
    }

    private void drawVerticalHand(Graphics g) {
        // TODO FUTURE
        throw new IllegalStateException("Cannot draw vertical hands at this time.");
    }
}
