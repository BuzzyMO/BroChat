package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * This class represent connection server
 * with incoming socket.
 *
 * @version   1.0 24 Nov 2020
 * @author    Oleksandr Lynnyk
 */
public class Main {

    private static final int         PORT = 8089;
    public static LinkedList<Server> connectionsList = new LinkedList<>();
    private static final Logger      log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args){
        try(ServerSocket serverSocket = new ServerSocket(PORT))
        {
            log.info("Server is working...");
            while(true){
                Socket incoming = serverSocket.accept();
                log.info("Connected: " + incoming.getInetAddress().getHostAddress());
                Server connection = new Server(incoming);
                connectionsList.add(connection);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
