package ca.claytonrogers.Common.Messages;

/**
 * This message is sent when a client is ready for the next round. When a client receives one of
 * these from every other client, then it is safe to proceed to the next round.
 * <p>
 * Created by clayton on 2015-07-05.
 */
public class ReadyForNextRound extends Message {
    public ReadyForNextRound() {
        super(MessageType.ReadyForNextRound);
    }
}
