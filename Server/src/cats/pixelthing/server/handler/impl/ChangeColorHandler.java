package cats.pixelthing.server.handler.impl;

import cats.pixelthing.core.connection.Connection;
import cats.pixelthing.core.data.impl.ChangeColor;
import cats.pixelthing.server.Server;
import cats.pixelthing.server.handler.DataHandler;
import cats.pixelthing.server.user.ServerUser;

public class ChangeColorHandler extends DataHandler<ChangeColor> {

    public void handle(final Connection con, final ChangeColor col){
        final ServerUser user = Server.connected.get(con);
        if(user == null)
            return;
        if(col.type() != ChangeColor.USER)
            return;
        user.color = col.color();
        final ChangeColor color = new ChangeColor(ChangeColor.USER, user.uid, user.color.getRGB());
        Server.connected.keySet().forEach(c -> c.send(color));
    }
}
