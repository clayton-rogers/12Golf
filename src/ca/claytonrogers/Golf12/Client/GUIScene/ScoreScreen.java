package ca.claytonrogers.Golf12.Client.GUIScene;

import ca.claytonrogers.Golf12.Client.GUIObjects.GUIButton;
import ca.claytonrogers.Golf12.Client.GUIObjects.GUIObject;
import ca.claytonrogers.Golf12.Client.GUIObjects.GUIScoreCard;
import ca.claytonrogers.Golf12.Common.Constants;
import ca.claytonrogers.Golf12.Common.IntVector;
import ca.claytonrogers.Golf12.Common.ScoreCard;

/**
 * This GUI scene displays the score screen for the game.
 * <p>
 * Created by clayton on 2015-08-16.
 */
public class ScoreScreen extends Scene<ScoreScreen.OptionalScores> {

    private static final IntVector NEXT_ROUND_BUTTON_LOCATION = Constants.FIELD_OFFSET.add(new IntVector(20,0));
    private static final IntVector NEXT_ROUND_BUTTON_SIZE = new IntVector(70, 15);
    private static final String    NEXT_ROUND_BUTTON_TEXT = "Next Round";
    private static final String    NEXT_ROUND_BACK_TEXT = "Go Back";

    private static final IntVector SAVE_BUTTON_LOCATION = NEXT_ROUND_BUTTON_LOCATION.add(new IntVector(0, 200));
    private static final IntVector SAVE_BUTTON_SIZE = new IntVector(70, 15);
    private static final String    SAVE_BUTTON_TEXT = "Save Scores";

    public static class OptionalScores {
        private boolean exists = false;
        private int[] scores = null;

        public OptionalScores(boolean exists, int[] scores) {
            this.exists = exists;
            this.scores = scores;
        }
    }

    private final ScoreCard scoreCard;
    private boolean isEndOfRound = true;
    private GUIButton nextRoundButton;
    private GUIButton saveButton;

    private GUIObject.GUIType NEXT_ROUND_BUTTON = new GUIObject.GUIType("NEXT_ROUND_BUTTON");
    private GUIObject.GUIType SAVE_BUTTON = new GUIObject.GUIType("SAVE_BUTTON");

    public ScoreScreen (String[] usernames) {
        nextRoundButton = new GUIButton(
                NEXT_ROUND_BUTTON_LOCATION,
                NEXT_ROUND_BUTTON_SIZE,
                NEXT_ROUND_BUTTON_TEXT,
                NEXT_ROUND_BUTTON
        );
        guiObjectList.add(nextRoundButton);

        saveButton = new GUIButton(
                SAVE_BUTTON_LOCATION,
                SAVE_BUTTON_SIZE,
                SAVE_BUTTON_TEXT,
                SAVE_BUTTON
        );
        guiObjectList.add(saveButton);

        scoreCard = new ScoreCard(usernames.length);
        GUIScoreCard scoreCardGUI = new GUIScoreCard(usernames, scoreCard);
        guiObjectList.add(scoreCardGUI);
    }

    @Override
    public void startScene(SceneChange<OptionalScores> sceneChange) {
        if (sceneChange.getPayload().exists) {
            isEndOfRound = true;
            scoreCard.add(sceneChange.getPayload().scores);
            nextRoundButton.setButtonText(NEXT_ROUND_BUTTON_TEXT);
        } else {
            isEndOfRound = false;
            nextRoundButton.setButtonText(NEXT_ROUND_BACK_TEXT);
        }
        saveButton.setVisibility(isEndOfRound);
    }

    @Override
    public void handleInputs() {
        GUIObject.GUIType clickType = getNextGoodClickLocation();

        if (clickType.is(NEXT_ROUND_BUTTON)) {
            if (isEndOfRound) {
                nextScene = new SceneChange<>(SceneType.Waiting, null);
            } else {
                nextScene = new SceneChange<>(SceneType.Game, null);
            }
            mouseClickList.poll(); // Since we have handled the click remove it.
        } else if (clickType.is(SAVE_BUTTON)) {
            nextScene = new SceneChange<> (SceneType.SaveScreen, scoreCard);
        } else if (!clickType.is(GUIObject.NONE_TYPE)) {
            System.out.println("Got a click in the score screen that wasn't expected: " + clickType);
        }
    }

    @Override
    public void processState() {
        // No state to process for the score screen.
    }
}
