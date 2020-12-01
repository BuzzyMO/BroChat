package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * This class represent controller
 * between client data and javafx form.
 *
 * @version   1.0 27 Nov 2020
 * @author    Oleksandr Lynnyk
 */
public class Controller implements Initializable {

    @FXML private TextField        nicknameField;
    @FXML private ComboBox<String> receiversBox;
    @FXML private Button           signInBtn;
    @FXML private Button           sendButton;
    @FXML private TextField        textMessage;
    @FXML private TextArea         chatArea;
    private Model                  model;
    private StAXparser             parser;
    private ObservableList<String> receivers;
    private Thread                 readThread;
    private static final Logger    log = Logger.getLogger(Controller.class.getName());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = new Model();
        configChatArea();
        receivers = FXCollections.observableArrayList("General");
        receiversBox.setItems(receivers);
        receiversBox.setValue("General");
        readThread = new Thread(() -> {
            while (!readThread.isInterrupted()) {
                String response = model.readMsg();
                try {
                    parser = new StAXparser(new ByteArrayInputStream(response.getBytes()));
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
                outputToView();
            }
        });
        readThread.start();
        signInBtn.setOnAction(event -> {
            if(Pattern.matches("^\\S+", nicknameField.getText())){
                model.setNickname(nicknameField.getText());
                chatArea.appendText("[SYSTEM]: You are logged in"+ "\n");
            } else {
                chatArea.appendText("[SYSTEM]: Enter correct nickname, without space at the beginning!"+ "\n");
            }
        });
        sendButton.setOnAction(event -> {
            try{
                if(receiversBox.getValue().equals("General")){
                    model.sendMsg("common",textMessage.getText(), receiversBox.getValue());
                } else {
                    model.sendMsg("private",textMessage.getText(), receiversBox.getValue());
                }
            } catch (FieldIsNotFilledException ex){
                log.info(ex.getMessage());
                chatArea.appendText("[SYSTEM]: Enter nickname"+ "\n");
            }
        });
    }

    private void outputToView(){
        switch (parser.getTypeMsg()){
            case "init":
                if(!receivers.contains(parser.getSenderMsg()))
                    receivers.add(parser.getSenderMsg());
                break;

            case "common":
                chatArea.appendText( parser.getSenderMsg()+": "+parser.getContentMsg()+ "\n");
                break;

            case "private":
                chatArea.appendText("[PRIVATE]" + parser.getSenderMsg() + "->" + parser.getReceiverMsg()+": "+parser.getContentMsg()+ "\n");
                break;

            default:
                log.info("Message type is not defined");
                break;
        }
    }

    private void configChatArea(){
        chatArea.setPrefRowCount(26);
        chatArea.setWrapText(true);
        chatArea.setEditable(false);
    }

    public void close() {
        readThread.interrupt();
        model.closeConnection();
    }
}
