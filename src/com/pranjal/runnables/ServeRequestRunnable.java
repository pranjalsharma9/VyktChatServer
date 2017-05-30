package com.pranjal.runnables;

import com.pranjal.data.Data;
import com.pranjal.models.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Pranjal on 31-05-2017.
 * The Runnable class to serve client requests.
 */
public class ServeRequestRunnable implements Runnable {

    // Constants
    private static final int SO_TIMEOUT = 0;

    // The socket connection with the client.
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ServeRequestRunnable (Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run () {
        try {
            socket.setSoTimeout(SO_TIMEOUT);
            out = new ObjectOutputStream(
                    new BufferedOutputStream(
                            socket.getOutputStream()
                    )
            );
            out.flush();
            in = new ObjectInputStream(
                    new BufferedInputStream(
                            socket.getInputStream()
                    )
            );
            Message message;
            while (true) {
                message = (Message) in.readObject();
                serveRequest(message);
                if (message.type == Message.MessageType.DISCONNECTION_REQUEST) {
                    break;
                }
            }
        } catch (IOException ioe) {
            System.out.println("IOException caught");
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Invalid user message");
            cnfe.printStackTrace();
        } finally {
            //Closing all connection finally to free resources.
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void serveRequest (Message message) throws IOException {
        switch (message.type) {
            case CONNECTION_REQUEST :
                Data.connectClient(message.sender);
                break;
            case SEND_REQUEST :
                Data.addMessage(message);
                break;
            case RECEIVE_REQUEST :
                for (Message messageToSend : Data.getMessages(message.sender)) {
                    out.writeObject(messageToSend);
                }
                out.writeObject(new Message(0L, null, null,
                        Message.MessageType.TERMINATOR, null, null));
                break;
            case GET_CLIENT_LIST_REQUEST :
                String[] clients = Data.getClientList();
                StringBuilder builder = new StringBuilder(clients[0]);
                for (int i = 1; i < clients.length; i++) {
                    builder.append(";").append(clients[i]);
                }
                out.writeObject(new Message(0L, null, null,
                        Message.MessageType.CLIENT_LIST_RESPONSE, builder.toString(), null));
                break;
            case DISCONNECTION_REQUEST :
                Data.disconnectClient(message.sender);
                break;
        }
    }

}
