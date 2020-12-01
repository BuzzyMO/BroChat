package server;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * This class provides processing request
 * from client and send response.
 *
 * @version   1.0 24 Nov 2020
 * @author    Oleksandr Lynnyk
 */
public class Server extends Thread{
    private final Socket        incoming;
    private PrintWriter         out;
    private String              nickname;
    private static final Logger log = Logger.getLogger(Server.class.getName());

    public Server(Socket incomingSocket){
        incoming = incomingSocket;
        start();
    }

    @Override
    public void run(){
        try(BufferedReader in = new BufferedReader(new InputStreamReader(incoming.getInputStream()))){
            out = new PrintWriter(new OutputStreamWriter(incoming.getOutputStream()),true);
            while(true) {
                String request = in.readLine();
                StAXparser parser = new StAXparser(new ByteArrayInputStream(request.getBytes()));
                sendResponse(parser, request);
                if(parser.getMsgType().equals("close")){
                    break;
                }
            }
        } catch (IOException | XMLStreamException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void sendResponse(StAXparser parser, String request){
        switch (parser.getMsgType()) {
            case "init":
                nickname = parser.getContentSender();
                for (Server s : Main.connectionsList) {
                    if (s.nickname != null) {
                        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                                "<message type=\"init\"><content receiver=\"01\" sender=\"" + s.nickname + "\">" + "" + "</content></message>");
                        s.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                                "<message type=\"init\"><content receiver=\"01\" sender=\"" + nickname + "\">" + "" + "</content></message>");
                    }
                }
                break;

            case "common":
                for (Server s : Main.connectionsList) {
                    s.out.println(request);
                }
                break;

            case "private":
                out.println(request);
                for (Server s : Main.connectionsList) {
                    if (s.nickname.equals(parser.getContentReceiver())) {
                        s.out.println(request);
                    }
                }
                break;

            default:
                log.info("Message type is not defined");
                break;
        }
    }

    private void closeConnection(){
        try {
            out.close();
            incoming.close();
            log.info("Disconnected: "+incoming.getInetAddress().getHostAddress());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Main.connectionsList.remove(this);
        }
    }
}
