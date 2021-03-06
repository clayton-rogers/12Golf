package ca.claytonrogers.Golf12.Common.Messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * This message is sent from the client to the server so that the server can verify the client has
 * the correct version information. The server responds with either a version authenticated or
 * mismatch message.
 * <p>
 * Created by clayton on 2015-07-05.
 */
public class VersionInformation extends Message {

    private int versionNumber = 0;

    public VersionInformation(DataInputStream in) throws IOException {
        super(MessageType.VersionInformation);

        versionNumber = in.readInt();
    }

    public VersionInformation(int versionNumber) {
        super(MessageType.VersionInformation);

        this.versionNumber = versionNumber;
    }

    @Override
    public void send(DataOutputStream out) throws IOException {
        super.send(out);

        out.writeInt(versionNumber);
        out.flush();
    }

    public int getVersionNumber() {
        return versionNumber;
    }
}
