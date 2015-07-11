package ca.claytonrogers.Common.Messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by clayton on 2015-07-05.
 */
public class DrawCardReplaced extends Message {

    private int cardReplacedIndex = 0;

    public DrawCardReplaced(DataInputStream in) throws IOException {
        super(MessageType.DrawCardReplaced);

        cardReplacedIndex = in.readByte();
    }

    public DrawCardReplaced(int cardReplacedIndex) {
        super(MessageType.DrawCardReplaced);

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
