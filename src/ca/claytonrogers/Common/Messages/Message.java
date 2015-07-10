package ca.claytonrogers.Common.Messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by clayton on 2015-07-05.
 */
public abstract class Message {
    public enum MessageType {
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

    public static Message parse(DataInputStream in) throws IOException {
        int messageNumber = in.readByte();
        MessageType messageType = MessageType.values()[messageNumber];

        switch (messageType) {
            case VersionInformation:
                return new VersionInformation(in);
            case VersionInformationMismatch:
                return new VersionInformationMismatch(in);
            case Username:
                return new Username(in);
            case StateUpdate:
                return new StateUpdate(in);
            case YourTurn:
                return new YourTurn();
            case DrawCardClicked:
                return new DrawCardClicked();
            case DrawCardDiscarded:
                return new DrawCardDiscarded(in);
            case DrawCardReplaced:
                return new DrawCardReplaced(in);
            case DiscardCardReplaced:
                return new DiscardCardReplaced(in);
            case TurnComplete:
                return new TurnComplete();
            case LastTurn:
                return new LastTurn();
            case RoundOver:
                return new RoundOver(in);
            case ReadyForNextRound:
                return new ReadyForNextRound();
            case GameOver:
                return new GameOver();
        }

        throw new IllegalStateException("Could not determine the message type.");
    }

    public void send (DataOutputStream out) throws IOException {
        out.writeByte(messageType.ordinal());
        out.flush();
    }

    public MessageType getMessageType() {
        return messageType;
    }
}
