package ca.claytonrogers.Common.Messages;

/**
 * This message is sent by the server when it receives a version information message from the client
 * which contains the same version number that the server has.
 * <p>
 * Created by clayton on 2015-07-09.
 */
public class VersionInformationAuthenticated extends Message {
    public VersionInformationAuthenticated() {
        super(MessageType.VersionInformationAuthenticated);
    }
}
