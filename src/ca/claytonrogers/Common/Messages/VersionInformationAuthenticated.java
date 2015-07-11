package ca.claytonrogers.Common.Messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by clayton on 2015-07-09.
 */
public class VersionInformationAuthenticated extends Message {

    private int playerNumber = 0;

    public VersionInformationAuthenticated(DataInputStream in) throws IOException{
        super(MessageType.VersionInformationAuthenticated);

        playerNumber = in.readInt();
    }

    public VersionInformationAuthenticated(int playerNumber) {
        super(MessageType.VersionInformationAuthenticated);

        this.playerNumber = playerNumber;
    }

    @Override
    public void send(DataOutputStream out) throws IOException {
        super.send(out);

        out.writeInt(playerNumber);
        out.flush();
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
}
