package ca.claytonrogers.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clayton on 2015-07-18.
 */
public class ScoreCard {

    private final int numPlayers;
    List<int[]> scores = new ArrayList<>(Constants.NUMBER_OF_ROUNDS);

    public ScoreCard(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public void add(int[] scores) {
        if (scores.length != numPlayers) {
            throw new IllegalArgumentException("Called add scores with an incorrect number of scores.");
        }
        this.scores.add(scores);
    }

    public int getScore (int round, int player) {
        if (round >= getNumberOfRoundsPlayed() ||
                round < 0) {
            throw new IllegalArgumentException("Tried to get the score from an illegal round: " + round);
        }
        if (player >= numPlayers ||
                player < 0) {
            throw new IllegalArgumentException("Tried to get the score from an illegal player: " + player);
        }

        int[] roundScore = scores.get(round);
        return roundScore[player];
    }

    public int getNumberOfRoundsPlayed () {
        return scores.size();
    }

    public int getNumPlayers () {
        return numPlayers;
    }
}
