package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * This class represents connection,
 * communication with server.
 *
 * @version   1.0 26 Nov 2020
 * @author    Oleksandr Lynnyk
 */
public class Model {

    private static final int PORT = 8089;
    private PrintWriter      out;
    private BufferedReader   in;
    private String           nickname;

    public Model(){
        nickname = "anonymous";
        try {
            InetAddress address = InetAddress.getByName("localhost");
            Socket socket = new Socket(address,PORT);
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendMsg(String msgType, String msgContent, String receiver) throws FieldIsNotFilledException {
        if(nickname.equals("anonymous"))
            throw new FieldIsNotFilledException("Nickname not entered");
        if(!msgContent.isEmpty()){
            String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<message type=\""+msgType+"\"><content receiver=\""+receiver+"\" sender=\""+nickname+"\">"+msgContent+"</content></message>";
            out.println(request);
        }
    }

    public String readMsg(){
        String response = "";
        try {
            response = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<message type=\"init\"><content receiver=\"01\" sender=\""+nickname+"\">"+""+"</content></message>");
    }

    public void closeConnection(){
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<message type=\"close\"></message>");
        out.close();
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
