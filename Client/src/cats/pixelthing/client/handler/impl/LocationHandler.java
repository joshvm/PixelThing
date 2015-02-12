package cats.pixelthing.client.handler.impl;

import cats.pixelthing.client.Client;
import cats.pixelthing.client.handler.DataHandler;
import cats.pixelthing.core.data.impl.Location;
import cats.pixelthing.core.drawable.pixel.Pixel;
import cats.pixelthing.core.drawable.user.User;

public class LocationHandler extends DataHandler<Location> {

    public void handle(final Location loc){
        switch(loc.type()){
            case Location.USER:
                final User user = Client.user.uid == loc.id() ? Client.user : Client.users.getByUid(loc.id());
                if(user == null)
                    break;
                user.x = loc.x();
                user.y = loc.y();
                break;
            case Location.PIXEL:
                final Pixel pixel = Client.pixels.getByID(loc.id());
                if(pixel == null)
                    break;
                pixel.x = loc.x();
                pixel.y = loc.y();
                break;
        }
    }
}
