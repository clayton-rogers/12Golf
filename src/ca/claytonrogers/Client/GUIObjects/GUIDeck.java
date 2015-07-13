package ca.claytonrogers.Client.GUIObjects;

import ca.claytonrogers.Common.Card;
import ca.claytonrogers.Common.Constants;
import ca.claytonrogers.Common.Deck;
import ca.claytonrogers.Common.IntVector;

import java.awt.*;

/**
 * Created by clayton on 2015-07-05.
 */
public class GUIDeck extends GUIObject {

    private Deck deck;
    private boolean isFaceUp = false;

    public GUIDeck(IntVector location, Deck deck, Type type) {
        super(location, Constants.DECK_SIZE, type);
        this.deck = deck;
    }

    @Override
    protected void internalDraw(Graphics g) {
        IntVector offsetLocation = new IntVector(location);
        offsetLocation = offsetLocation.add(Constants.DECK_OFFSET); // The offset location should work like a drop shadow

        Card bottomCard = new Card(-1, true); // Card with -1 is blank.
        Card topCard = new Card(deck.peek().value,isFaceUp);

        Drawer.drawCard(g, bottomCard, offsetLocation);
        Drawer.drawCard(g, topCard, location);
    }

    public void setIsFaceUp(boolean isFaceUp) {
        this.isFaceUp = isFaceUp;
    }
}
