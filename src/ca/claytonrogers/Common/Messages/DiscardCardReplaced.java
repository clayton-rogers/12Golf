package ca.claytonrogers.Common.Messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by clayton on 2015-07-05.
 */
public class DiscardCardReplaced extends Message {

    private int cardReplacedIndex = 0;

    public DiscardCardReplaced(DataInputStream in) throws IOException {
        super(MessageType.DiscardCardReplaced);

        cardReplacedIndex = in.readByte();
    }

    public DiscardCardReplaced(int cardReplacedIndex) {
        super(MessageType.DiscardCardReplaced);

        this.cardReplacedIndex = cardReplacedIndex;
    }

    @Override
    public void send(DataOutputStream out) throws IOException {
        super.send(out);

        out.writeByte(cardReplacedIndex);
        out.flush();
    }

    public int getCardReplacedIndex() {
        return cardReplacedIndex;
    }
}
