package cats.pixelthing.server.handler.impl;

import cats.pixelthing.core.Constants;
import cats.pixelthing.core.connection.Connection;
import cats.pixelthing.core.data.impl.Location;
import cats.pixelthing.core.data.impl.Message;
import cats.pixelthing.server.Server;
import cats.pixelthing.server.format.MsgFormat;
import cats.pixelthing.server.handler.DataHandler;
import cats.pixelthing.server.table.UserModel;
import cats.pixelthing.server.user.ServerUser;

public class LocationHandler extends DataHandler<Location> {

    public void handle(final Connection con, final Location loc){
        final ServerUser user = Server.connected.get(con);
        if(user == null)
            return;
        if(!Constants.CANVAS_BOUNDS.contains(loc.point())){
            user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.format("Server", "You can't move out of bounds")));
            return;
        }
        if(user.x != loc.x() && Math.abs(user.x - loc.x()) != Constants.USER_SPEED){
            user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.format("Server", "You can't move that quickly!")));
            return;
        }
        if(user.y != loc.y() && Math.abs(user.y - loc.y()) != Constants.USER_SPEED){
            user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.format("Server", "You can't move that quickly!")));
            return;
        }
        user.x = loc.x();
        user.y = loc.y();
        Server.userTable.model.setValueAt(user.x, user, UserModel.Column.X);
        Server.userTable.model.setValueAt(user.y, user, UserModel.Column.Y);
        final Location location = new Location(Location.USER, user.uid, (short)user.x, (short)user.y);
        Server.users.forEach(u -> u.send(location));
    }
}
