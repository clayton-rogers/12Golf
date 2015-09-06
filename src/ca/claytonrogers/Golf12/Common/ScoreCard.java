package ca.claytonrogers.Golf12.Common;

import ca.claytonrogers.Golf12.Client.GUIScene.Scene;
import ca.claytonrogers.Golf12.Common.FileOps.SaveFile;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the internal data representation of the score screen. It keeps track of round numbers
 * and totals and can be queried for its information.
 * <p>
 * Created by clayton on 2015-07-18.
 */
public class ScoreCard {

    public static class SaveVersionMismatchException extends Exception {
        public SaveVersionMismatchException(String message) {
            super(message);
        }
    }
    private static final int SAVE_VERSION = 1;

    private final int numPlayers;
    private final List<int[]> scores = new ArrayList<>(Constants.NUMBER_OF_ROUNDS);

    public ScoreCard (int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public ScoreCard (SaveFile saveFile) throws SaveVersionMismatchException{
        numPlayers = 2;
//        // TODO parse the save game into the
//        String[] pieces = saveGame.split(",");
//        int index = 0;
//
//        if (Integer.parseInt(pieces[index]) != SAVE_VERSION) {
//            throw new SaveVersionMismatchException("Save version: " + Integer.parseInt(pieces[index]) + " Expected version: " + SAVE_VERSION);
//        }
//        index++;
//
//        numPlayers = Integer.parseInt(pieces[index]);
//        index++;
//
//        int numberOfRounds = Integer.parseInt(pieces[index]);
//        index++;
//        // TODO
    }

    public void save (SaveFile saveFile) {
        // TODO
    }

    public void add (int[] scores) {
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

    public int getTotal(int playerNumber) {
        if (playerNumber >= numPlayers ||
                playerNumber < 0) {
            throw new IllegalArgumentException("Tried to get the total score for a player that does not exist: " + playerNumber);
        }

        int totalScore = 0;
        for (int[] score : scores) {
            totalScore += score[playerNumber];
        }

        return totalScore;
    }
}
