package ca.claytonrogers.Client.GUIScene;

import ca.claytonrogers.Client.GUIObjects.GUIStatusString;
import ca.claytonrogers.Common.Connection;
import ca.claytonrogers.Common.Messages.Message;
import ca.claytonrogers.Common.Messages.ReadyForNextRound;

/**
 * This scene displays only the text "Waiting for all players to be ready." and then waits
 * for a message from the other players.
 *
 * Created by clayton on 2015-08-16.
 */
public class WaitingScreen extends Scene<SceneChange.NullPayloadType> {

    private Connection serverConnection;

    public WaitingScreen(Connection serverConnection) {
        this.serverConnection = serverConnection;

        GUIStatusString statusString = new GUIStatusString();
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
