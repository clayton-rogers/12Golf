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

    private final Hand hand;
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
    // TODO FUTURE check where the player is sitting and move the hands around to place the player at the bottom
    public int getClickedCard(IntVector clickLocation) {
        if (!this.checkClicked(clickLocation)) {
            throw new IllegalArgumentException("Called getClickedCard for a position outside of its bounds.");
        }

        switch (position) {
            case 0:
            case 2:
                return getHorizontalClickedCard(clickLocation);
            case 1:
            case 3:
                //return getVerticalClickedCard();
                throw new IllegalStateException("No side players allowed.");
        }

        // Could not find a clicked card
        return -1;
    }

    private int getHorizontalClickedCard(IntVector clickLocation) {

        IntVector relativePos = clickLocation.sub(location);
        final int handWidth = Constants.NUMBER_OF_CARDS/2;

        for (int x = 0; x < handWidth; x++) {
            for (int y = 0; y < 2; y++) {
                if (
                        relativePos.x > x*(Constants.CARD_SIZE.x+Constants.SPACE_BETWEEN_CARDS) &&
                        relativePos.x < (x*(Constants.CARD_SIZE.x+Constants.SPACE_BETWEEN_CARDS)+Constants.CARD_SIZE.x) &&
                        relativePos.y > y*(Constants.CARD_SIZE.y+Constants.SPACE_BETWEEN_CARDS) &&
                        relativePos.y < (y*(Constants.CARD_SIZE.y+Constants.SPACE_BETWEEN_CARDS)+Constants.CARD_SIZE.y)
                        ) {
                    return x + y*handWidth;
                }
            }
        }

        // Click must have gone through one of the cracks
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
        for (int row = 0; row < 2; row ++) {
            for (int i = 0; i < handWidth; i++) {
                Card card = hand.getCard(i + row*handWidth);
                Drawer.drawCard(g, card, cardLocation);

                // Increment the card location to the right
                cardLocation = cardLocation.add(new IntVector(Constants.SPACE_BETWEEN_CARDS + Constants.CARD_SIZE.x,0));
            }
            // Increment the card location down a row
            cardLocation = location.add(new IntVector(0,Constants.SPACE_BETWEEN_CARDS + Constants.CARD_SIZE.y));
        }

        // Finally, draw the score next to the hand.
        String score = "Score: " + hand.getHandScore();
        IntVector scoreLocation = location.add(
                new IntVector(
                        handWidth*(Constants.SPACE_BETWEEN_CARDS + Constants.CARD_SIZE.x),
                        2 * (Constants.SPACE_BETWEEN_CARDS)));
        g.drawString(score, scoreLocation.x, scoreLocation.y);
    }

    private void drawVerticalHand(Graphics g) {
        // TODO FUTURE
        throw new IllegalStateException("Cannot draw vertical hands at this time.");
    }
}
