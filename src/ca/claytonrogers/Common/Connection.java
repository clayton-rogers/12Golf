package ca.claytonrogers.Common;

import ca.claytonrogers.Common.Messages.Message;

import java.io.*;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by clayton on 2015-07-04.
 */
public class Connection extends Thread implements Closeable {

    private DataInputStream reader;
    private DataOutputStream writer;
    private Socket socket;
    private boolean isGood = false;
    private Queue<Message> messageQueue = new ConcurrentLinkedQueue<>();

    public Connection (Socket socket) {
        this.socket = socket;

        try {
            reader = new DataInputStream(socket.getInputStream());
            writer = new DataOutputStream(socket.getOutputStream());
            isGood = true;
        } catch (IOException e) {
            System.out.println("There was a problem making the connection.");
            e.printStackTrace();
            isGood = false;
        }
        
        this.start();
    }

    @Override
    public void run() {
        super.run();

        try {
            while (isGood) {
                Message message = Message.parse(reader);
                messageQueue.add(message);
                if (Constants.NET_DEBUG) {
                    System.out.println("Received message type: " + message.getMessageType());
                }
            }
        } catch (IOException e) {
            System.out.println("There was a problem on the message reading thread: " + e);
        }
    }

    public void send (Message message) {
        if (!isGood) {
            System.out.println("Tried sending a message when not good.");
            return;
        }

        try {
            message.send(writer);
            if (Constants.NET_DEBUG) {
                System.out.println("Sending message type: " + message.getMessageType());
            }
        } catch (IOException e) {
            isGood = false;
            System.out.println("There was an issue sending a message: " + e);
        }
    }

    public boolean isGood() {
        return isGood;
    }

    public Message getMessage () {
        return messageQueue.poll();
    }

    public Message waitForNextMessage() {
        Message message = null;

        try {
            while (message == null) {
                message = getMessage();
                Thread.sleep(10L);
            }
        } catch (InterruptedException e) {
            System.out.println("Connection was interrupted while waiting for next message.");
            isGood = false;
        }

        return message;
    }

    @Override
    public void close() throws IOException {
        writer.close();
        reader.close();
        socket.close();
    }
}
