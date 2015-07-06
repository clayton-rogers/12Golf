package ca.claytonrogers.Client.GUIObjects;

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

    public GUIDeck(IntVector location, Deck deck) {
        super(location, Constants.DECK_SIZE);
        this.deck = deck;
    }

    @Override
    public void draw(Graphics g) {
        // TODO
    }
}
