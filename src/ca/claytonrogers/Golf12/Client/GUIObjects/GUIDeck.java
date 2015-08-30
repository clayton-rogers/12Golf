package ca.claytonrogers.Golf12.Client.GUIObjects;

import ca.claytonrogers.Golf12.Common.Card;
import ca.claytonrogers.Golf12.Common.Deck;
import ca.claytonrogers.Golf12.Common.IntVector;

import java.awt.*;

/**
 * This is the GUI representation of a deck. It handles whether the deck is face up and the drawing
 * and placement of the deck.
 * <p>
 * Created by clayton on 2015-07-05.
 */
public class GUIDeck extends GUIObject {

    private static final IntVector DECK_SIZE = Drawer.CARD_SIZE.add(new IntVector(6,6)); // The 6 is to allow for the height of the deck
    private static final IntVector DECK_OFFSET = new IntVector(-1, 1);

    private final Deck deck;
    private boolean isFaceUp = false;

    public GUIDeck(IntVector location, Deck deck, GUIType type) {
        super(location, DECK_SIZE, type);
        this.deck = deck;
    }

    @Override
    protected void internalDraw(Graphics g) {
        IntVector offsetLocation = new IntVector(location);


        // This will give about 6 cards for a full draw deck, after deal
        int numberCardsToDraw = (int) (deck.size() * 6.0/30);

        if (deck.size() == 0) {
            Drawer.drawX(g, location);
        } else {
            Card bottomCard = new Card(-1, true, Card.Suit.clubs); // Card with -1 is blank.
            Card topCard = new Card(deck.peek().value, isFaceUp, deck.peek().suit);

            // Draw all the bottom cards
            for (int i = 0; i < numberCardsToDraw; i++) {
                Drawer.drawCard(g, bottomCard, offsetLocation);
                offsetLocation = offsetLocation.sub(DECK_OFFSET); // The offset location should work like a drop shadow
            }
            // And the top face card
            Drawer.drawCard(g, topCard, offsetLocation);
        }
    }

    public void setIsFaceUp(boolean isFaceUp) {
        this.isFaceUp = isFaceUp;
    }
}
