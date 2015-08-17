package ca.claytonrogers.Common.Messages;

/**
 * The message is sent when the draw pile is selected.
 * <p>
 * Created by clayton on 2015-07-05.
 */
public class DrawCardClicked extends Message {
    public DrawCardClicked() {
        super(MessageType.DrawCardClicked);
    }
}
