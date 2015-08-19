package ca.claytonrogers.Client.GUIScene;

import ca.claytonrogers.Client.GUIObjects.GUIButton;
import ca.claytonrogers.Client.GUIObjects.GUIObject;
import ca.claytonrogers.Client.GUIObjects.GUIScoreCard;
import ca.claytonrogers.Common.Constants;
import ca.claytonrogers.Common.ScoreCard;

/**
 * This GUI scene displays the score screen for the game.
 * <p>
 * Created by clayton on 2015-08-16.
 */
public class ScoreScreen extends Scene<ScoreScreen.OptionalScores> {

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

    public ScoreScreen (String[] usernames) {
        nextRoundButton = new GUIButton(
                Constants.NEXT_ROUND_BUTTON_LOCATION,
                Constants.NEXT_ROUND_BUTTON_SIZE,
                Constants.NEXT_ROUND_BUTTON_TEXT,
                GUIObject.Type.NextRoundButton
        );
        guiObjectList.add(nextRoundButton);

        scoreCard = new ScoreCard(usernames.length);
        GUIScoreCard scoreCardGUI = new GUIScoreCard(usernames, scoreCard);
        guiObjectList.add(scoreCardGUI);
    }

    @Override
    public void startScene(SceneChange<OptionalScores> sceneChange) {
        if (sceneChange.getPayload().exists) {
            scoreCard.add(sceneChange.getPayload().scores);
            isEndOfRound = true;
            nextRoundButton.setButtonText(Constants.NEXT_ROUND_BUTTON_TEXT);
        } else {
            isEndOfRound = false;
            nextRoundButton.setButtonText(Constants.NEXT_ROUND_BACK_TEXT);
        }
    }

    @Override
    public void handleInputs() {
        GUIObject.Type clickType = getNextGoodClickLocation();

        switch (clickType) {
            case None:
                break;
            case NextRoundButton:
                if (isEndOfRound) {
                    nextScene = new SceneChange<>(SceneType.Waiting, null);
                } else {
                    nextScene = new SceneChange<>(SceneType.Game, null);
                }
                mouseClickList.poll(); // Since we have handled the click remove it.
                break;
            default:
                System.out.println("Got a click in the score screen that wasn't expected: " + clickType);
        }
    }

    @Override
    public void processState() {
        // No state to process for the score screen.
    }
}
