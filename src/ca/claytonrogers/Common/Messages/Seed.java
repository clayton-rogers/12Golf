package ca.claytonrogers.Common.Messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
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
