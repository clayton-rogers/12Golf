package ca.claytonrogers.Golf12.Common.Messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * This message is sent by the first client to all others so that they all have the same seed. The
 * seed is used by the clients to generate the order of cards in the rounds. This is used instead
 * of actually sending the initial state of the game to all of the clients.
 * <p>
 * Created by clayton on 2015-08-13.
 */
public class Seed extends Message {

    private long seed;

    public Seed(DataInputStream in) throws IOException {
        super(MessageType.Seed);

        seed = in.readLong();
    }

    public Seed(long seed) {
        super(MessageType.Seed);

        this.seed = seed;
    }

    @Override
    public void send(DataOutputStream out) throws IOException {
        super.send(out);

        out.writeLong(seed);
        out.flush();
    }

    public long getSeed() {
        return seed;
    }
}
