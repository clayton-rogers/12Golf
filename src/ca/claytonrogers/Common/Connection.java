package ca.claytonrogers.Common;

import java.io.*;
import java.net.Socket;

/**
 * Created by clayton on 2015-07-04.
 */
public class Connection extends Thread{

    private DataInputStream reader;
    private DataOutputStream writer;
    private Socket socket;
    private volatile boolean isGood = false;

    public Connection (Socket socket) {
        this.socket = socket;

        try {
            reader = new DataInputStream(socket.getInputStream());
            writer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("There was a problem making the connection.");
            e.printStackTrace();
            isGood = false;
        }

        isGood = true;

        this.start();
    }

    @Override
    public void run() {
        super.run();


    }
}
