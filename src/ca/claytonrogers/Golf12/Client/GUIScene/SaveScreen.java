package ca.claytonrogers.Golf12.Client.GUIScene;

import ca.claytonrogers.Golf12.Client.GUIObjects.GUIButton;
import ca.claytonrogers.Golf12.Client.GUIObjects.GUIObject;
import ca.claytonrogers.Golf12.Client.GUIObjects.GUIString;
import ca.claytonrogers.Golf12.Common.Constants;
import ca.claytonrogers.Golf12.Common.FileOps.SaveFile;
import ca.claytonrogers.Golf12.Common.IntVector;
import ca.claytonrogers.Golf12.Common.ScoreCard;

import java.util.Collections;

/**
 * Created by clayton on 2015-08-30.
 */
public class SaveScreen extends Scene<SaveScreen.OptionalScoreCard> {

    private static final String SAVE_FILENAME_BASE = "12Golf";
    private static final String SAVE_FILENAME_EXT = ".sav";
    private static final String SAVE_BUTTON_TEXT = "SAVE";
    private static final String LOAD_BUTTON_TEXT = "LOAD";
    private static final IntVector LABEL_LOCATION = Constants.FIELD_OFFSET.add(new IntVector(0,12));
    private static final IntVector SAVE_BUTTON_LOCATION = Constants.FIELD_OFFSET.add(new IntVector(100,0));
    private static final IntVector LOAD_BUTTON_LOCATION = SAVE_BUTTON_LOCATION.add(new IntVector(75, 0));
    private static final IntVector BUTTON_SIZE = new IntVector(45,15);

    private static final int SAVE_SLOTS = 5;

    private SaveFile[] saveFiles = new SaveFile[SAVE_SLOTS];
    private ScoreCard scoreCardToBeSaved;
    private ScoreCard[] scoreCards = new ScoreCard[SAVE_SLOTS];
    private GUIButton[] saveButtons = new GUIButton[SAVE_SLOTS];
    private GUIButton[] loadButtons = new GUIButton[SAVE_SLOTS];
    private GUIString[] labels      = new GUIString[SAVE_SLOTS];

    private GUIObject.GUIType[] SAVE_BUTTON_KEYS =  new GUIObject.GUIType[SAVE_SLOTS];
    private GUIObject.GUIType[] LOAD_BUTTON_KEYS =  new GUIObject.GUIType[SAVE_SLOTS];

    public static class OptionalScoreCard {
        private boolean exists = false;
        private ScoreCard scoreCard = null;

        public OptionalScoreCard(boolean exists, ScoreCard scoreCard) {
            this.exists = exists;
            this.scoreCard = scoreCard;
        }
    }

    public SaveScreen() {
        for (int i = 0; i < SAVE_SLOTS; i++) {
            labels[i] = new GUIString(LABEL_LOCATION.add(new IntVector(0,40*i)));
            labels[i].setString("Save slot " + String.valueOf(i));

            SAVE_BUTTON_KEYS[i] = new GUIObject.GUIType("BUTTON_KEY" + String.valueOf(i) + "_SAVE");
            saveButtons[i] = new GUIButton(
                    SAVE_BUTTON_LOCATION.add(new IntVector(0,40*i)),
                    BUTTON_SIZE,
                    SAVE_BUTTON_TEXT,
                    SAVE_BUTTON_KEYS[i]
            );

            LOAD_BUTTON_KEYS[i] = new GUIObject.GUIType("BUTTON_KEY" + String.valueOf(i) + "_LOAD");
            loadButtons[i] = new GUIButton(
                    LOAD_BUTTON_LOCATION.add(new IntVector(0, 40*i)),
                    BUTTON_SIZE,
                    LOAD_BUTTON_TEXT,
                    LOAD_BUTTON_KEYS[i]
            );
        }

        Collections.addAll(guiObjectList, labels);
        Collections.addAll(guiObjectList, saveButtons);
        Collections.addAll(guiObjectList, loadButtons);


        for (int i = 0; i < SAVE_SLOTS; i++) {
            saveFiles[i] = new SaveFile(SAVE_FILENAME_BASE + String.valueOf(i) + SAVE_FILENAME_EXT);
        }
    }

    @Override
    public void startScene(SceneChange<OptionalScoreCard> sceneChange) {
        for (int i = 0; i < SAVE_SLOTS; i++) {
            SaveFile file = saveFiles[i];
            String saveGame = file.getStringValue("GAME", "DOES_NOT_EXISTS");
            if (saveGame.equals("DOES_NOT_EXISTS")) {
                loadButtons[i].setVisibility(false);
                continue;
            } else {
                loadButtons[i].setVisibility(true);
            }

            try {
                scoreCards[i] = new ScoreCard(file);
            } catch (ScoreCard.SaveVersionMismatchException e) {
                scoreCards[i] = null;
                loadButtons[i].setVisibility(false);
            }
        }

        OptionalScoreCard card = sceneChange.getPayload();
        if (card.exists) {
            scoreCardToBeSaved = card.scoreCard;
        } else {
            // TODO we didn't get a score card to save so maybe don't do anything?
        }
    }

    @Override
    public void handleInputs() {
        // TODO
    }

    @Override
    public void processState() {
        // Don't need to do any processing
    }
}
