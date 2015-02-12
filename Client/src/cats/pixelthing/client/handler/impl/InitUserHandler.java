package cats.pixelthing.client.handler.impl;

import cats.pixelthing.client.Client;
import cats.pixelthing.client.handler.DataHandler;
import cats.pixelthing.core.data.impl.InitUser;
import cats.pixelthing.core.drawable.user.User;

public class InitUserHandler extends DataHandler<InitUser> {

    public void handle(final InitUser init){
        switch(init.type()){
            case InitUser.SELF:
                Client.user = new User(init.uid());
                Client.user.name = init.name();
                Client.user.x = init.x();
                Client.user.y = init.y();
                Client.user.color = init.color();
                Client.loadGameScreen();
                break;
            case InitUser.OTHER:
                final User user = Client.users.getByUid(init.uid());
                if(user == null)
                    break;
                user.name = init.name();
                user.x = init.x();
                user.y = init.y();
                user.color = init.color();
                break;
        }
    }
}
