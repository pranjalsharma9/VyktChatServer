package com.pranjal;

import com.pranjal.runnables.ServeRequestRunnable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Pranjal on 16-06-2016.
 * This is the Main class that is responsible for the creation of server-client connections
 * and assigning resources for connections.
 */
public class Main {
    
    // THREAD POOL related stuff
    // thread pool size
    private static final int THREAD_POOL_SIZE = 10;
    // thread pool
    public static ExecutorService threadPool;

    // SERVER SOCKET related stuff
    // The port number that accepts connections on the server
    private static final int SERVER_PORT_NUMBER = 11111;


    // The main function.
    // Execution starts here.
    // Server resources are granted.
    public static void main(String[] args) {

        // Set up the thread pool
        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // The server socket that waits for clients to connect
        // Opened in a try-catch block
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT_NUMBER)) {
            // looping continuously to accept connections from clients
            while (true) {
                // try-catch to catch IOException cause by the accept() method
                try {
                    // getting a client connection, creating a runnable and granting it a thread.
                    Socket clientConnectionSocket = serverSocket.accept();
                    System.out.println("received a request");
                    ServeRequestRunnable serveClientRunnable = new ServeRequestRunnable(clientConnectionSocket);
                    threadPool.submit(serveClientRunnable);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}
