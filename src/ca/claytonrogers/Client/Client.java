package ca.claytonrogers.Client;

/**
 * The main entry into the client program. Only starts the application.
 * <p>
 * Created by clayton on 2015-07-04.
 */
class Client {
    public static void main (String[] args) {
        Application application = new Application();
        application.run(); // Run the application on the foreground thread.
    }
}
