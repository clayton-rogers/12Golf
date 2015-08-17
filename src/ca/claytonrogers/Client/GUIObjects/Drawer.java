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

    public static void drawCard(Graphics g, Card card, IntVector location) {

        // Draw the background.
        g.setColor(Constants.CARD_BACKGROUND_COLOR);
        g.fillRoundRect(
                location.x,
                location.y,
                Constants.CARD_SIZE.x,
                Constants.CARD_SIZE.y,
                Constants.CARD_CORNER_RADIUS,
                Constants.CARD_CORNER_RADIUS);

        // Draw the outline
        g.setColor(Constants.CARD_FOREGROUND_COLOR);
        g.drawRoundRect(
                location.x,
                location.y,
                Constants.CARD_SIZE.x,
                Constants.CARD_SIZE.y,
                Constants.CARD_CORNER_RADIUS,
                Constants.CARD_CORNER_RADIUS);

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
                location.x + Constants.CARD_CORNER_RADIUS,
                location.y + Constants.CARD_CORNER_RADIUS,
                location.x + Constants.CARD_SIZE.x - Constants.CARD_CORNER_RADIUS,
                location.y + Constants.CARD_SIZE.y - Constants.CARD_CORNER_RADIUS);

        // Bottom left to top right
        g.drawLine(
                location.x + Constants.CARD_SIZE.x - Constants.CARD_CORNER_RADIUS,
                location.y + Constants.CARD_CORNER_RADIUS,
                location.x + Constants.CARD_CORNER_RADIUS,
                location.y + Constants.CARD_SIZE.y - Constants.CARD_CORNER_RADIUS);
    }
}
