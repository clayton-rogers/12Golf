package ca.claytonrogers.Common.Messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by clayton on 2015-07-05.
 */
public abstract class Message {
    enum MessageType {
        VersionInformation,  // Includes the version number
        VersionInformationMismatch, // Includes the version of both the server and client
        Username,            // Includes the username
        StateUpdate,         // Includes the entire state
        YourTurn,
        DrawCardClicked,
        DrawCardDiscarded,    // Includes the card that was flipped in place
        DrawCardReplaced,     // Includes the hand card that was replaced
        DiscardCardReplaced,  // Includes the hand card that was replaced
        TurnComplete,
        LastTurn,
        RoundOver,            // Includes the score
        ReadyForNextRound,
        GameOver
    }

    private MessageType messageType;
    Message (MessageType messageType) {
        this.messageType = messageType;
    }

    public static Message parse(DataInputStream reader) throws IOException {
        int messageNumber = reader.readByte();
        MessageType messageType = MessageType.values()[messageNumber];

        switch (messageType) {
            case LastTurn:
                return new LastTurn();

        }

        throw new IllegalStateException("Could not determine the message type.");
    }

    public void send (DataOutputStream writer) throws IOException {
        writer.writeByte(messageType.ordinal());
        writer.flush();
    }

    public MessageType getMessageType() {
        return messageType;
    }
}
