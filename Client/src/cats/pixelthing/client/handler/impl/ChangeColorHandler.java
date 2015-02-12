package cats.pixelthing.client.handler.impl;

import cats.pixelthing.client.Client;
import cats.pixelthing.client.handler.DataHandler;
import cats.pixelthing.core.data.impl.ChangeColor;
import cats.pixelthing.core.drawable.user.User;

public class ChangeColorHandler extends DataHandler<ChangeColor>{

    public void handle(final ChangeColor color){
        switch(color.type()){
            case ChangeColor.USER:
                final User user = Client.user.uid == color.id() ? Client.user : Client.users.getByUid(color.id());
                if(user == null)
                    break;
                user.color = color.color();
                if(Client.user.equals(user))
                    Client.chatAreaComponent.initUserLabel();
                break;
        }
    }
}
