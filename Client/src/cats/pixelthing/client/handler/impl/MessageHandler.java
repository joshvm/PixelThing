package cats.pixelthing.client.handler.impl;

import cats.pixelthing.client.Client;
import cats.pixelthing.client.handler.DataHandler;
import cats.pixelthing.core.data.impl.Message;
import javax.swing.JOptionPane;

public class MessageHandler extends DataHandler<Message> {

    public void handle(final Message message){
        switch(message.type()){
            case Message.POPUP:
                JOptionPane.showMessageDialog(null, message.msg());
                break;
            case Message.PUBLIC_CHAT:
                Client.chatAreaComponent.pushMessage(message);
                break;
        }
    }
}
