package ca.claytonrogers.Common.Messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * This message is sent to tell the other clients what the username of this client is.
 * <p>
 * Created by clayton on 2015-07-05.
 */
public class Username extends Message {

    private String username = "";
    private int playerNumber;

    public Username(DataInputStream in) throws IOException {
        super(MessageType.Username);

        username = in.readUTF();
        playerNumber = in.readInt();
    }

    public Username(String username, int playerNumber) {
        super(MessageType.Username);

        this.username = username;
        this.playerNumber = playerNumber;
    }

    @Override
    public void send(DataOutputStream out) throws IOException {
        super.send(out);

        out.writeUTF(username);
        out.writeInt(playerNumber);
        out.flush();
    }

    public String getUsername() {
        return username;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
