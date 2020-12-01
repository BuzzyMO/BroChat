package client;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

/**
 * This class represent processing XML.
 *
 * @version   1.0 27 Nov 2020
 * @author    Oleksandr Lynnyk
 */
public class StAXparser {

    private final XMLStreamReader reader;
    private String                typeMsg;
    private String                receiverMsg;
    private String                senderMsg;
    private String                contentMsg;

    public StAXparser(InputStream in) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        reader = factory.createXMLStreamReader(in);
        parse();
    }
    public void parse() throws XMLStreamException {
        while(reader.hasNext()){
            int event = reader.next();
            if(event == XMLStreamConstants.START_ELEMENT){
                if(reader.getLocalName().equals("message")){
                    typeMsg = reader.getAttributeValue(null,"type");
                }
                if (reader.getLocalName().equals("content")){
                    receiverMsg = reader.getAttributeValue(null,"receiver");
                    senderMsg = reader.getAttributeValue(null,"sender");
                    contentMsg = reader.getElementText();
                }
            }
        }
    }


    public String getTypeMsg() {
        return typeMsg;
    }

    public String getReceiverMsg() {
        return receiverMsg;
    }

    public String getSenderMsg() {
        return senderMsg;
    }

    public String getContentMsg() {
        return contentMsg;
    }
}
