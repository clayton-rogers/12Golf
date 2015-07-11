package ca.claytonrogers.Common.Messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by clayton on 2015-07-05.
 */
public class DrawCardDiscarded extends Message {
    public DrawCardDiscarded() {
        super(MessageType.DrawCardDiscarded);
    }
}
