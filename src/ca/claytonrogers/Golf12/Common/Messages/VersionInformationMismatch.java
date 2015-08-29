package ca.claytonrogers.Golf12.Common.Messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * This message is sent by the server to the client when it receives a version information message
 * from the client that has different version information than the server has.
 * <p>
 * Created by clayton on 2015-07-05.
 */
public class VersionInformationMismatch extends Message {

    private int clientVersionNumber = 0;
    private int serverVersionNumber = 0;

    public VersionInformationMismatch(DataInputStream in) throws IOException {
        super(MessageType.VersionInformationMismatch);

        clientVersionNumber = in.readInt();
        serverVersionNumber = in.readInt();
    }

    public VersionInformationMismatch(int clientVersionNumber, int serverVersionNumber) {
        super(MessageType.VersionInformationMismatch);

        this.clientVersionNumber = clientVersionNumber;
        this.serverVersionNumber = serverVersionNumber;
    }

    @Override
    public void send(DataOutputStream out) throws IOException {
        super.send(out);

        out.writeInt(clientVersionNumber);
        out.writeInt(serverVersionNumber);
        out.flush();
    }

    public int getClientVersionNumber() {
        return clientVersionNumber;
    }

    public int getServerVersionNumber() {
        return serverVersionNumber;
    }
}
