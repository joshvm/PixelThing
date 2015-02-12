package cats.pixelthing.server.handler.impl;

import cats.pixelthing.core.Constants;
import cats.pixelthing.core.connection.Connection;
import cats.pixelthing.core.data.impl.Location;
import cats.pixelthing.core.data.impl.Message;
import cats.pixelthing.server.Server;
import cats.pixelthing.server.data.UserManager;
import cats.pixelthing.server.format.MsgFormat;
import cats.pixelthing.server.handler.DataHandler;
import cats.pixelthing.server.table.UserModel;
import cats.pixelthing.server.user.ServerUser;

public class MessageHandler extends DataHandler<Message> {

    public void handle(final Connection con, final Message message){
        final ServerUser user = Server.connected.get(con);
        if(user == null)
            return;
        switch(message.type()){
            case Message.PUBLIC_CHAT:
                if(user.muted){
                    con.send(new Message(Message.PUBLIC_CHAT, MsgFormat.format(Server.NAME, "You are muted and can't talk")));
                    return;
                }
                final Message m = new Message(Message.PUBLIC_CHAT, MsgFormat.format(user.name, user.access, message.msg()));
                Server.users.forEach(u -> u.send(m));
                break;
            case Message.COMMAND:
                if(!message.msg().startsWith(Constants.COMMAND_START_SEQUENCE) || message.msg().equals(Constants.COMMAND_START_SEQUENCE)){
                    user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.format(Server.NAME, "Invalid command syntax")));
                    return;
                }
                final String string = message.msg().substring(Constants.COMMAND_START_SEQUENCE.length());
                final int i = string.indexOf(' ');
                final String commandName = i == -1 ? string : string.substring(0, i+1);
                final String arg = string.substring(commandName.length()).trim();
                switch(commandName.trim()){
                    case "help":
                    case "commands":
                        if(!arg.isEmpty()){
                            user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.format(Server.NAME, "This command takes no arguments")));
                            break;
                        }
                        user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "%s#1 > Prints the player's name with the most pixels", Constants.COMMAND_START_SEQUENCE)));
                        break;
                    case "#1":
                        if(!arg.isEmpty()){
                            user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.format(Server.NAME, "This command takes no arguments")));
                            break;
                        }
                        ServerUser maxUser = Server.users.get(0);
                        if(maxUser == null)
                            break;
                        for(final ServerUser u : Server.users)
                            if(u.pixelCount > maxUser.pixelCount)
                                maxUser = u;
                        final Message highest = new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "%s has the most pixels: %,d", maxUser.name(), maxUser.pixelCount));
                        Server.connected.keySet().forEach(c -> c.send(highest));
                        break;
                    case "pixels":
                        if(!arg.isEmpty()){
                            user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.format(Server.NAME, "This command takes no arguments")));
                            break;
                        }
                        user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "You have %,d pixels", user.pixelCount)));
                        break;
                    case "mute":
                        if(user.access.ordinal() < ServerUser.Access.MODERATOR.ordinal()){
                            user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.format(Server.NAME, "You do not have sufficient to mute")));
                            break;
                        }
                        final ServerUser muteTarget = Server.users.getByName(arg);
                        if(muteTarget == null){
                            user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "%s doesn't exist", arg)));
                            break;
                        }
                        muteTarget.muted = true;
                        Server.userTable.model.setValueAt(muteTarget.muted, muteTarget, UserModel.Column.MUTED);
                        muteTarget.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "You have been muted by %s", user.name)));
                        UserManager.saveUsers();
                        user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "You have muted %s", muteTarget.name)));
                        break;
                    case "ban":
                        if(user.access.ordinal() < ServerUser.Access.ADMINISTRATOR.ordinal()){
                            user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.format(Server.NAME, "You do not have sufficient to mute")));
                            break;
                        }
                        final ServerUser banTarget = Server.users.getByName(arg);
                        if(banTarget == null){
                            user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "%s doesn't exist", arg)));
                            break;
                        }
                        banTarget.banned = true;
                        Server.userTable.model.setValueAt(banTarget.banned, banTarget, UserModel.Column.BANNED);
                        banTarget.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "You have been banned by %s", user.name)));
                        UserManager.saveUsers();
                        user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "You have banned %s", banTarget.name)));
                        banTarget.connection.close();
                        break;
                    case "unmute":
                        if(user.access.ordinal() < ServerUser.Access.MODERATOR.ordinal()){
                            user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.format(Server.NAME, "You do not have sufficient to mute")));
                            break;
                        }
                        final ServerUser unmuteTarget = Server.users.getByName(arg);
                        if(unmuteTarget == null){
                            user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "%s doesn't exist", arg)));
                            break;
                        }
                        unmuteTarget.muted = false;
                        Server.userTable.model.setValueAt(unmuteTarget.muted, unmuteTarget, UserModel.Column.MUTED);
                        unmuteTarget.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "You have been unmuted by %s", user.name)));
                        UserManager.saveUsers();
                        user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "You have unmuted %s", unmuteTarget.name)));
                        break;
                    case "unban":
                        if(user.access.ordinal() < ServerUser.Access.ADMINISTRATOR.ordinal()){
                            user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.format(Server.NAME, "You do not have sufficient to mute")));
                            break;
                        }
                        final ServerUser unbanTarget = Server.users.getByName(arg);
                        if(unbanTarget == null){
                            user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "%s doesn't exist", arg)));
                            break;
                        }
                        unbanTarget.banned = false;
                        Server.userTable.model.setValueAt(unbanTarget.banned, unbanTarget, UserModel.Column.BANNED);
                        unbanTarget.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "You have been unbanned by %s", user.name)));
                        UserManager.saveUsers();
                        user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "You have unbanned %s", unbanTarget.name)));
                        break;
                    case "save":
                        if(user.access != ServerUser.Access.OWNER)
                            break;
                        UserManager.saveUsers();
                        user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.format(Server.NAME, "Saved server")));
                        break;
                    case "teleto":
                        final ServerUser teleToTarget = Server.users.getByName(arg);
                        if(teleToTarget == null || teleToTarget.connection == null){
                            user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "%s doesn't exist or is offline", arg)));
                            break;
                        }
                        user.setLocation(teleToTarget.getLocation());
                        Server.userTable.model.setValueAt(user.x, user, UserModel.Column.X);
                        Server.userTable.model.setValueAt(user.y, user, UserModel.Column.Y);
                        Server.connected.keySet().forEach(c -> c.send(new Location(Location.USER, user.uid, (short) user.x, (short) user.y)));
                        teleToTarget.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "%s has just teleported to you", user.name)));
                        break;
                    case "teletome":
                        final ServerUser teleToMeTarget = Server.users.getByName(arg);
                        if(teleToMeTarget == null || teleToMeTarget.connection == null){
                            user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "%s doesn't exist or is offline", arg)));
                            break;
                        }
                        teleToMeTarget.setLocation(user.getLocation());
                        Server.userTable.model.setValueAt(teleToMeTarget.x, teleToMeTarget, UserModel.Column.X);
                        Server.userTable.model.setValueAt(teleToMeTarget.y, teleToMeTarget, UserModel.Column.Y);
                        Server.connected.keySet().forEach(c -> c.send(new Location(Location.USER, teleToMeTarget.uid, (short) teleToMeTarget.x, (short) teleToMeTarget.y)));
                        teleToMeTarget.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "You have been teleported by %s", user.name)));
                        break;
                    default:
                        user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "No such command: %s", commandName)));
                }
                break;
        }
    }
}
