package cats.pixelthing.client.handler.impl;

import cats.pixelthing.client.Client;
import cats.pixelthing.client.handler.DataHandler;
import cats.pixelthing.core.data.impl.Modify;
import cats.pixelthing.core.drawable.pixel.Pixel;

public class ModifyHandler extends DataHandler<Modify> {

    public void handle(final Modify u){
        switch(u.type()){
            case Modify.USER:
                switch(u.mod()){
                    case Modify.JOIN:
                        if(Client.users.getByUid(u.id()) == null && Client.user.uid != u.id())
                            Client.users.add(new cats.pixelthing.core.drawable.user.User(u.id()));
                        break;
                    case Modify.LEAVE:
                        Client.users.remove(Client.users.getByUid(u.id()));
                        break;
                }
                break;
            case Modify.PIXEL:
                switch(u.mod()){
                    case Modify.JOIN:
                        if(Client.pixels.getByID(u.id()) == null)
                            Client.pixels.add(new Pixel(u.id()));
                        break;
                    case Modify.LEAVE:
                        Client.pixels.remove(Client.pixels.getByID(u.id()));
                        break;
                }
                break;
        }

    }
}
