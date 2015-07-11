package ca.claytonrogers.Common.Messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by clayton on 2015-07-09.
 */
public class VersionInformationAuthenticated extends Message {

    private int playerNumber = 0;
    private int totalPlayers = 0;

    public VersionInformationAuthenticated(DataInputStream in) throws IOException{
        super(MessageType.VersionInformationAuthenticated);

        playerNumber = in.readByte();
        totalPlayers = in.readByte();
    }

    public VersionInformationAuthenticated(int playerNumber, int totalPlayers) {
        super(MessageType.VersionInformationAuthenticated);

        this.playerNumber = playerNumber;
        this.totalPlayers = totalPlayers;
    }

    @Override
    public void send(DataOutputStream out) throws IOException {
        super.send(out);

        out.writeByte(playerNumber);
        out.writeByte(totalPlayers);
        out.flush();
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }
}
