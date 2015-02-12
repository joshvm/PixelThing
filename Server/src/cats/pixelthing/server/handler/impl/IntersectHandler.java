package cats.pixelthing.server.handler.impl;

import cats.pixelthing.core.connection.Connection;
import cats.pixelthing.core.data.impl.Intersect;
import cats.pixelthing.core.data.impl.Modify;
import cats.pixelthing.core.drawable.pixel.Pixel;
import cats.pixelthing.server.Server;
import cats.pixelthing.server.handler.DataHandler;
import cats.pixelthing.server.table.UserModel;
import cats.pixelthing.server.user.ServerUser;

public class IntersectHandler extends DataHandler<Intersect> {

    public void handle(final Connection con, final Intersect intersect){
        final ServerUser user = Server.connected.get(con);
        if(user == null)
            return;
        switch(intersect.type()){
            case Intersect.PIXEL:
                final Pixel pixel = Server.pixels.getByID(intersect.id());
                if(pixel == null)
                    break;
                if(pixel.intersects(user)){
                    user.pixelCount++;
                    Server.userTable.model.setValueAt(user.pixelCount, user, UserModel.Column.PIXELS);
                    Server.pixels.remove(pixel);
                    Server.connected.keySet().forEach(c -> c.send(new Modify(Modify.PIXEL, Modify.LEAVE, pixel.id)));
                }
                break;
        }
    }
}
