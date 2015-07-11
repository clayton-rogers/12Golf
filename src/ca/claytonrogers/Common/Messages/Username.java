package ca.claytonrogers.Common.Messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by clayton on 2015-07-05.
 */
public class Username extends Message {

    private String username = "";

    public Username(DataInputStream in) throws IOException {
        super(MessageType.Username);

        username = in.readUTF();
    }

    public Username(String username) {
        super(MessageType.Username);

        this.username = username;
    }

    @Override
    public void send(DataOutputStream out) throws IOException {
        super.send(out);

        out.writeUTF(username);
        out.flush();
    }

    public String getUsername() {
        return username;
    }
}
