package ca.claytonrogers.Common.Messages;

import ca.claytonrogers.Common.State;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by clayton on 2015-07-05.
 */
public class StateUpdate extends Message {

    private State state;

    public StateUpdate(DataInputStream in) throws IOException {
        super(MessageType.StateUpdate);

        state = State.read(in);
    }

    public StateUpdate(State state) {
        super(MessageType.StateUpdate);

        this.state = state;
    }

    @Override
    public void send(DataOutputStream out) throws IOException {
        super.send(out);

        State.write(out, state);
        out.flush();
    }

    public State getState() {
        return state;
    }
}
