package ca.claytonrogers.Golf12.Client.GUIObjects;

import ca.claytonrogers.Golf12.Common.Constants;
import ca.claytonrogers.Golf12.Common.IntVector;
import ca.claytonrogers.Golf12.Common.ScoreCard;

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
        super(Constants.FIELD_OFFSET, Constants.NULL_SIZE, NONE_TYPE);
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
            int roundWinnerID = -1;
            int roundWinnerScore = 9999;  // The maximum round score is <120 (for now) thus we must
                                          // start with something higher than that.
            boolean isTie = false;
            for (int player = 0; player < numPlayers; player++) {
                int score = scoreCard.getScore(round, player);
                if (score < roundWinnerScore) {
                    roundWinnerID = player;
                    roundWinnerScore = score;
                    isTie = false;
                } else if (score == roundWinnerScore) {
                    isTie = true;
                }
            }
            if (isTie) {
                roundWinnerID = -1;
            }
            for (int player = 0; player < numPlayers; player++) {
                if (roundWinnerID == player) {
                    Font boldFont = g.getFont().deriveFont(Font.BOLD);
                    g.setFont(boldFont);
                }
                g.drawString(
                        String.valueOf(scoreCard.getScore(round,player)),
                        drawLocation.x,
                        drawLocation.y);
                if (roundWinnerID == player) {
                    Font font = g.getFont().deriveFont(Font.PLAIN);
                    g.setFont(font);
                }
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
