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
public class ScoreScreen extends Scene<int[]> {

    private final ScoreCard scoreCard;

    public ScoreScreen (String[] usernames) {
        GUIButton nextRoundButton = new GUIButton(
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
    public void startScene(SceneChange<int[]> sceneChange) {
        int[] scores = sceneChange.getPayload();
        scoreCard.add(scores);
    }

    @Override
    public void handleInputs() {
        GUIObject.Type clickType = getNextGoodClickLocation();

        switch (clickType) {
            case None:
                break;
            case NextRoundButton:
                nextScene = new SceneChange<>(SceneType.Waiting, null);
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
