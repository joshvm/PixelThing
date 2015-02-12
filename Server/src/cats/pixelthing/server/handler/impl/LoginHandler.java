package cats.pixelthing.server.handler.impl;

import cats.pixelthing.core.Constants;
import cats.pixelthing.core.connection.Connection;
import cats.pixelthing.core.data.impl.InitPixel;
import cats.pixelthing.core.data.impl.InitUser;
import cats.pixelthing.core.data.impl.Login;
import cats.pixelthing.core.data.impl.Message;
import cats.pixelthing.core.data.impl.Modify;
import cats.pixelthing.server.Server;
import cats.pixelthing.server.format.MsgFormat;
import cats.pixelthing.server.handler.DataHandler;
import cats.pixelthing.server.table.UserModel;
import cats.pixelthing.server.user.ServerUser;

public class LoginHandler extends DataHandler<Login>{

    public void handle(final Connection con, final Login data){
        final ServerUser user = Server.users.getByName(data.name());
        if(user == null){
            con.send(new Message(Message.POPUP, String.format("%s doesn't exist", data.name())));
            return;
        }
        if(user.connection != null){
            con.send(new Message(Message.POPUP, String.format("%s is already logged in", user.name)));
            return;
        }
        if(!user.pass.equals(data.pass())){
            con.send(new Message(Message.POPUP, "Passwords do not match"));
            return;
        }
        if(user.banned){
            con.send(new Message(Message.POPUP, String.format("This account has been banned")));
            return;
        }
        user.connection = con;
        Server.connected.put(con, user);
        Server.userTable.model.setValueAt(user.connection, user, UserModel.Column.CONNECTED);
        user.send(new InitUser(InitUser.SELF, user.uid, user.name, (short) user.x, (short) user.y, user.color.getRGB()));
        user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "Welcome to %s, %s", Constants.TITLE, user.name)));
        Server.pixels.forEach(
                p -> {
                    user.send(new Modify(Modify.PIXEL, Modify.JOIN, p.id));
                    user.send(new InitPixel(p.id, (short)p.x, (short)p.y, p.color.getRGB()));
                }
        );
        Server.users.stream().filter(u -> !u.equals(user) && u.connection != null).forEach(
                u -> {
                    u.send(new Modify(Modify.JOIN, Modify.USER, user.uid));
                    u.send(new InitUser(InitUser.OTHER, user.uid, user.name, (short) user.x, (short) user.y, user.color.getRGB()));
                    u.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "%s has just logged in", user.name)));

                    user.send(new Modify(Modify.JOIN, Modify.USER, u.uid));
                    user.send(new InitUser(InitUser.OTHER, u.uid, u.name, (short) u.x, (short) u.y, u.color.getRGB()));
                }
        );
    }
}
