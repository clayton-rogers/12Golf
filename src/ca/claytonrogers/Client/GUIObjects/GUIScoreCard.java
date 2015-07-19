package ca.claytonrogers.Client.GUIObjects;

import ca.claytonrogers.Common.Constants;
import ca.claytonrogers.Common.IntVector;
import ca.claytonrogers.Common.ScoreCard;

import java.awt.*;

/**
 * Created by clayton on 2015-07-18.
 */
public class GUIScoreCard extends GUIObject {

    public ScoreCard scoreCard;
    private String[] playerNames;

    public GUIScoreCard(String[] playerNames) {
        super(Constants.FIELD_OFFSET, Constants.NULL_SIZE, Type.None);
        // Since we never need to be able to click on the score screen,
        // we set it's size to null.
        if (playerNames.length > 4 || playerNames.length < 2) {
            throw new IllegalArgumentException("List of player names was unusual: " + playerNames.length);
        }
        scoreCard = new ScoreCard(playerNames.length);
        this.playerNames = playerNames;
    }

    @Override
    protected void internalDraw(Graphics g) {

        IntVector drawLocation = Constants.SCORE_SCREEN_INITIAL_OFFSET;
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
            g.drawString("Round " + round, drawLocation.x, drawLocation.y);
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

        // Draw the header underline
        drawLocation = Constants.SCORE_SCREEN_INITIAL_OFFSET;
        g.drawLine(
                drawLocation.x,
                drawLocation.y,
                drawLocation.x + 500,
                drawLocation.y);

        // Draw the vertical lines
        for (int i = 0; i < numPlayers; i++) {
            g.drawLine(
                    drawLocation.x + Constants.SCORE_SCREEN_TAB_SIZE + i * Constants.SCORE_SCREEN_TAB_SIZE,
                    drawLocation.y,
                    drawLocation.x + Constants.SCORE_SCREEN_TAB_SIZE + i * Constants.SCORE_SCREEN_TAB_SIZE,
                    drawLocation.y + 500);
        }
    }

    private IntVector nextDrawLine (IntVector drawLocation) {
        return new IntVector( // Reset the x and increment the line
                Constants.SCORE_SCREEN_INITIAL_OFFSET.x,
                drawLocation.y + Constants.SCORE_SCREEN_LINE_HEIGHT
        );
    }
    private IntVector nextTab (IntVector drawLocation) {
        return drawLocation.add(new IntVector(Constants.SCORE_SCREEN_TAB_SIZE,0));
    }
}
