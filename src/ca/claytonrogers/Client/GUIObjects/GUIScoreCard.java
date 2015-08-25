package ca.claytonrogers.Client.GUIObjects;

import ca.claytonrogers.Common.Constants;
import ca.claytonrogers.Common.IntVector;
import ca.claytonrogers.Common.ScoreCard;

import java.awt.*;

/**
 * This is the GUI representation of the entire score card. It draws all the information from the
 * internal {@link ScoreCard} object.
 * <p>
 * Created by clayton on 2015-07-18.
 */
public class GUIScoreCard extends GUIObject {

    private static final int SCORE_SCREEN_TAB_SIZE = 80;
    private static final int SCORE_SCREEN_LINE_HEIGHT = 20;
    private static final IntVector SCORE_SCREEN_INITIAL_OFFSET = Constants.FIELD_OFFSET.add(new IntVector(0,40));

    private ScoreCard scoreCard;
    private String[] playerNames;

    public GUIScoreCard(String[] playerNames, ScoreCard scoreCard) {
        super(Constants.FIELD_OFFSET, Constants.NULL_SIZE, Type.None);
        // Since we never need to be able to click on the score screen,
        // we set it's size to null.
        if (playerNames.length > 4 || playerNames.length < 2) {
            throw new IllegalArgumentException("List of player names was unusual: " + playerNames.length);
        }
        this.scoreCard = scoreCard;
        this.playerNames = playerNames;
    }

    @Override
    protected void internalDraw(Graphics g) {

        IntVector drawLocation = SCORE_SCREEN_INITIAL_OFFSET;
        int numPlayers = scoreCard.getNumPlayers();

        // Draw the header
        g.drawString("Round", drawLocation.x, drawLocation.y);
        drawLocation = nextTab(drawLocation);
        for (String name : playerNames) {
            g.drawString(name, drawLocation.x, drawLocation.y);
            drawLocation = nextTab(drawLocation);
        }
        drawLocation = nextDrawLine(drawLocation);

        // Draw each line
        for (int round = 0; round < scoreCard.getNumberOfRoundsPlayed(); round++) {
            g.drawString("Round " + (round+1), drawLocation.x, drawLocation.y);
            drawLocation = nextTab(drawLocation);
            for (int player = 0; player < numPlayers; player++) {
                g.drawString(
                        String.valueOf(scoreCard.getScore(round,player)),
                        drawLocation.x,
                        drawLocation.y);
                drawLocation = nextTab(drawLocation);
            }

            drawLocation = nextDrawLine(drawLocation);
        }

        // Draw the totals after the lines
        g.drawString("Total:", drawLocation.x, drawLocation.y);
        drawLocation = nextTab(drawLocation);
        for (int player = 0; player < numPlayers; player++) {
            g.drawString(
                    String.valueOf(scoreCard.getTotal(player)),
                    drawLocation.x,
                    drawLocation.y);
            drawLocation = nextTab(drawLocation);
        }

        // Draw the header underline
        drawLocation = SCORE_SCREEN_INITIAL_OFFSET;
        g.drawLine(
                drawLocation.x,
                drawLocation.y,
                drawLocation.x + 400,
                drawLocation.y);

        // Draw the vertical lines
        for (int i = 0; i < numPlayers; i++) {
            g.drawLine(
                    drawLocation.x -5 + SCORE_SCREEN_TAB_SIZE + i * SCORE_SCREEN_TAB_SIZE,
                    drawLocation.y,
                    drawLocation.x -5 + SCORE_SCREEN_TAB_SIZE + i * SCORE_SCREEN_TAB_SIZE,
                    drawLocation.y + 500);
        }
    }

    private IntVector nextDrawLine (IntVector drawLocation) {
        return new IntVector( // Reset the x and increment the line
                SCORE_SCREEN_INITIAL_OFFSET.x,
                drawLocation.y + SCORE_SCREEN_LINE_HEIGHT
        );
    }
    private IntVector nextTab (IntVector drawLocation) {
        return drawLocation.add(new IntVector(SCORE_SCREEN_TAB_SIZE,0));
    }
}
