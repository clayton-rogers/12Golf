package ca.claytonrogers.Common;

import java.awt.*;

/**
 * Created by clayton on 2015-07-05.
 */
public class Hand extends GUIObject {

    Card[] cards = new Card[12];

    public Hand(IntVector location) {
        super(location, null);
    }

    @Override
    public boolean checkClicked(IntVector clickLocation) {
        for (Card card : cards) {
            if (card.checkClicked(clickLocation)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void draw(Graphics2D g) {
        // TODO
    }
}
