package ca.claytonrogers.Common.Messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by clayton on 2015-07-05.
 */
public class VersionInformation extends Message {

    private int versionNumber = 0;

    public VersionInformation(DataInputStream reader) throws IOException {
        super(MessageType.VersionInformation);

        versionNumber = reader.readInt();
    }

    public VersionInformation(int versionNumber) {
        super(MessageType.VersionInformation);
        this.versionNumber = versionNumber;
    }

    @Override
    public void send(DataOutputStream writer) throws IOException {
        super.send(writer);
        writer.writeInt(versionNumber);
        writer.flush();
    }

    public int getVersionNumber() {
        return versionNumber;
    }
}
