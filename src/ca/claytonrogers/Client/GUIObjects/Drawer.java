package ca.claytonrogers.Client.GUIObjects;

import ca.claytonrogers.Common.Card;
import ca.claytonrogers.Common.Constants;
import ca.claytonrogers.Common.IntVector;

import java.awt.*;

/**
 * This is a general purpose drawing class since cards do not have GUI objects.
 * <p>
 * Created by clayton on 2015-07-11.
 */
class Drawer {

    public  static final IntVector CARD_SIZE = new IntVector(30,60);
    private static final int       CARD_CORNER_RADIUS = 3;

    public static void drawCard(Graphics g, Card card, IntVector location) {

        // Draw the background.
        g.setColor(Constants.CARD_BACKGROUND_COLOR);
        g.fillRoundRect(
                location.x,
                location.y,
                CARD_SIZE.x,
                CARD_SIZE.y,
                CARD_CORNER_RADIUS,
                CARD_CORNER_RADIUS);

        // Draw the outline
        g.setColor(Constants.CARD_FOREGROUND_COLOR);
        g.drawRoundRect(
                location.x,
                location.y,
                CARD_SIZE.x,
                CARD_SIZE.y,
                CARD_CORNER_RADIUS,
                CARD_CORNER_RADIUS);

        // Draw either the number or the back
        if (card.isFaceUp) {
            g.drawString(
                    card.faceValue(),
                    location.x+12,
                    location.y+20);
        } else {
            drawX(g, location);
        }
    }

    public static void drawX(Graphics g, IntVector location) {
        // Top left to bottom right
        g.drawLine(
                location.x + CARD_CORNER_RADIUS,
                location.y + CARD_CORNER_RADIUS,
                location.x + CARD_SIZE.x - CARD_CORNER_RADIUS,
                location.y + CARD_SIZE.y - CARD_CORNER_RADIUS);

        // Bottom left to top right
        g.drawLine(
                location.x + CARD_SIZE.x - CARD_CORNER_RADIUS,
                location.y + CARD_CORNER_RADIUS,
                location.x + CARD_CORNER_RADIUS,
                location.y + CARD_SIZE.y - CARD_CORNER_RADIUS);
    }
}
