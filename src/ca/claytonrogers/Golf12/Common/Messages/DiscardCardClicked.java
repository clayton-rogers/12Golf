package ca.claytonrogers.Golf12.Common.Messages;

/**
 * This message is sent when the discard pile has be seleted.
 * <p>
 * Created by clayton on 2015-07-10.
 */
public class DiscardCardClicked extends Message {
    public DiscardCardClicked() {
        super(MessageType.DiscardCardClicked);
    }
}
