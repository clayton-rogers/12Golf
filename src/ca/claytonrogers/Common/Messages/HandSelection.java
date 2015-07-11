package ca.claytonrogers.Common.Messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by clayton on 2015-07-11.
 */
public class HandSelection extends Message {

    private int cardSelectionIndex = 0;

    public HandSelection(DataInputStream in) throws IOException {
        super(MessageType.HandSelection);

        cardSelectionIndex = in.readByte();
    }

    public HandSelection(int cardSelectionIndex) {
        super(MessageType.HandSelection);

        this.cardSelectionIndex = cardSelectionIndex;
    }

    @Override
    public void send(DataOutputStream out) throws IOException {
        super.send(out);

        out.writeByte(cardSelectionIndex);
        out.flush();
    }

    public int getCardSelectionIndex() {
        return cardSelectionIndex;
    }
}
