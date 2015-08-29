package ca.claytonrogers.Golf12.Client.GUIScene;

import ca.claytonrogers.Golf12.Client.GUIObjects.GUIString;
import ca.claytonrogers.Golf12.Common.Connection;
import ca.claytonrogers.Golf12.Common.Constants;
import ca.claytonrogers.Golf12.Common.IntVector;
import ca.claytonrogers.Golf12.Common.Messages.Message;
import ca.claytonrogers.Golf12.Common.Messages.ReadyForNextRound;

/**
 * This scene displays only the text "Waiting for all players to be ready." and then waits
 * for a message from the other players.
 * <p>
 * Created by clayton on 2015-08-16.
 */
public class WaitingScreen extends Scene<SceneChange.NullPayloadType> {

    public static final IntVector WAITING_FOR_PLAYERS_LOCATION = Constants.FIELD_OFFSET.add(new IntVector(100, 100));

    private Connection serverConnection;

    public WaitingScreen(Connection serverConnection) {
        this.serverConnection = serverConnection;

        GUIString statusString = new GUIString(WAITING_FOR_PLAYERS_LOCATION);
        statusString.setString("Waiting for all players to be ready...");
        guiObjectList.add(statusString);
    }

    @Override
    public void startScene(SceneChange<SceneChange.NullPayloadType> sceneChange) {
        serverConnection.send(new ReadyForNextRound());
    }

    @Override
    public void handleInputs() {
        // TODO FUTURE for more than two players we will have to make sure we get one from everyone.
        Message message = serverConnection.getMessage();
        if (message != null) {
            if (message.getMessageType() == Message.MessageType.ReadyForNextRound) {
                nextScene = new SceneChange<SceneChange.NullPayloadType>(SceneType.Game, null);
            } else {
                System.out.println("Got an unexpected message on ");
            }
        }
    }

    @Override
    public void processState() {
        // Don't need to do anything.
    }
}
