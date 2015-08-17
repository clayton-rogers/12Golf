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
        VersionInformationAuthenticated,
        VersionInformationMismatch, // Includes the version of both the server and client
        Username,            // Includes the username
        DrawCardClicked,
        DiscardCardClicked,
        HandSelection,        // Informs which card was selected from the hand
        ReadyForNextRound,
        Seed,
        PlayerInfo
    }

    private final MessageType messageType;
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
            case VersionInformationAuthenticated:
                return new VersionInformationAuthenticated();
            case Username:
                return new Username(in);
            case DrawCardClicked:
                return new DrawCardClicked();
            case DiscardCardClicked:
                return new DiscardCardClicked();
            case HandSelection:
                return new HandSelection(in);
            case ReadyForNextRound:
                return new ReadyForNextRound();
            case Seed:
                return new Seed(in);
            case PlayerInfo:
                return new PlayerInfo(in);
        }

        throw new IllegalStateException("Could not determine the message type:" + messageType);
    }

    public void send (DataOutputStream out) throws IOException {
        out.writeByte(messageType.ordinal());
        out.flush();
    }

    public MessageType getMessageType() {
        return messageType;
    }
}
