package ca.claytonrogers.Common.Messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by clayton on 2015-07-05.
 */
public class RoundOver extends Message {

    private int[] score = new int[4];

    public RoundOver(DataInputStream in) throws IOException{
        super(MessageType.RoundOver);

        for (int i = 0; i < 4; i++) {
            score[i] = in.readByte();
        }
    }

    public RoundOver(int[] score) {
        super(MessageType.RoundOver);

        if (score.length <= 4 && score.length >= 2) {
            throw new IllegalStateException("RoundOver message created with wrong number of scores: " + score.length);
        }

        Arrays.fill(score, 0);
        System.arraycopy(score, 0, this.score, 0, score.length);
    }

    @Override
    public void send(DataOutputStream out) throws IOException {
        super.send(out);

        for (int i = 0; i < 4; i++) {
            out.writeByte(score[i]);
        }
        out.flush();
    }

    public int[] getScore() {
        return score;
    }
}
