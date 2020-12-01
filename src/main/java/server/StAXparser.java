package server;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

/**
 * This class represent processing XML.
 *
 * @version   1.0 24 Nov 2020
 * @author    Oleksandr Lynnyk
 */
public class StAXparser {

    private final XMLStreamReader reader;
    private String                msgType;
    private String                contentReceiver;
    private String                contentSender;
    private String                contentText;

    public StAXparser(InputStream in) throws XMLStreamException{
        XMLInputFactory factory = XMLInputFactory.newInstance();
        reader = factory.createXMLStreamReader(in);
        parse();
    }

    public void parse() throws XMLStreamException {
        while(reader.hasNext()){
            int event = reader.next();
            if(event == XMLStreamConstants.START_ELEMENT){
                if(reader.getLocalName().equals("message")){
                    msgType = reader.getAttributeValue(null, "type");
                }
                if(reader.getLocalName().equals("content")){
                    contentReceiver = reader.getAttributeValue(null,"receiver");
                    contentSender = reader.getAttributeValue(null,"sender");
                    contentText = reader.getElementText();
                }
            }
        }
    }

    public String getMsgType(){ return msgType; }
    public String getContentReceiver(){
        return contentReceiver;
    }
    public String getContentSender(){
        return contentSender;
    }
    public String getContentText(){
        return  contentText;
    }
}
