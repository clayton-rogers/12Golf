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

        int numberOfPlayers = in.readByte();
        state = new State(numberOfPlayers);
        // TODO read in into state
    }

    public StateUpdate(State state) {
        super(MessageType.StateUpdate);

        // TODO change this to copy constructor
        this.state = state;
    }

    @Override
    public void send(DataOutputStream out) throws IOException {
        super.send(out);

        // TODO send the state here
    }

    public State getState() {
        return state;
    }
}
