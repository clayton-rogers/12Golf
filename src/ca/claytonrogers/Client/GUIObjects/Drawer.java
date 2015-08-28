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
            g.setFont(g.getFont().deriveFont(16.0F));
            // Value
            if (card.value == 10) {
                // 10 is the only two character card string, so we must move it over a
                // bit to keep it centered.
                g.drawString(
                        card.faceValue(),
                        location.x + 6,
                        location.y + 22);
            } else if (card.value == 13) {
                g.drawString(
                        card.faceValue(),
                        location.x + 10,
                        location.y + 22);
            } else if (card.value == 12) {
                g.drawString(
                        card.faceValue(),
                        location.x + 9,
                        location.y + 22);
            } else if (card.value == 1) {
                g.drawString(
                        card.faceValue(),
                        location.x + 10,
                        location.y + 22);
            } else {
                g.drawString(
                        card.faceValue(),
                        location.x + 11,
                        location.y + 22);
            }
            g.setFont(g.getFont().deriveFont(20.0F));
            // Suit
            g.drawString(
                    card.suit.toString(),
                    location.x + 7,
                    location.y + 47);
            g.setFont(g.getFont().deriveFont(12.0F));
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
