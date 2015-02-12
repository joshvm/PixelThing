package cats.pixelthing.server.handler.impl;

import cats.pixelthing.core.connection.Connection;
import cats.pixelthing.core.data.impl.Message;
import cats.pixelthing.core.data.impl.Register;
import cats.pixelthing.server.Server;
import cats.pixelthing.server.data.UserManager;
import cats.pixelthing.server.handler.DataHandler;
import cats.pixelthing.server.user.ServerUser;
import cats.pixelthing.server.validator.Validators;

public class RegisterHandler extends DataHandler<Register> {

    public void handle(final Connection con, final Register data){
        if(Server.users.getByName(data.name()) != null){
            con.send(new Message(Message.POPUP, String.format("%s is already registered", data.name())));
            return;
        }
        String out = Validators.validate(Validators.NAME_VALIDATOR, data.name());
        if(!out.isEmpty()){
            con.send(new Message(Message.POPUP, out));
            return;
        }
        out = Validators.validate(Validators.PASS_VALIDATOR, data.pass());
        if(!out.isEmpty()){
            con.send(new Message(Message.POPUP, out));
            return;
        }
        final ServerUser user = new ServerUser(System.currentTimeMillis());
        user.name = data.name();
        user.pass = data.pass();
        Server.users.add(user);
        Server.userTable.model.add(user);
        UserManager.saveUsers();
        con.send(new Message(Message.POPUP, String.format("Successfully registered %s", data.name())));
    }
}
