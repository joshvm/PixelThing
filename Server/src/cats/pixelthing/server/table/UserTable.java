package cats.pixelthing.server.table;

import cats.pixelthing.core.data.impl.Message;
import cats.pixelthing.server.Server;
import cats.pixelthing.server.data.UserManager;
import cats.pixelthing.server.format.MsgFormat;
import cats.pixelthing.server.user.ServerUser;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class UserTable extends JPanel {

    public final UserModel model;

    private final JTable table;

    public UserTable(){
        super(new BorderLayout());

        model = new UserModel();

        table = new JTable(model);
        table.addMouseListener(
                new MouseAdapter(){
                    public void mousePressed(final MouseEvent me){
                        final Point p = me.getPoint();
                        final int r = table.rowAtPoint(p);
                        final int c = table.columnAtPoint(p);
                        if((r | c) < 0)
                            return;
                        final ServerUser user = model.get(r);
                        final JPopupMenu popup = new JPopupMenu();
                        final JMenuItem infoItem = new JMenuItem(String.format("%s (%d)", user.name, user.uid));
                        infoItem.setEnabled(false);
                        popup.add(infoItem);
                        popup.addSeparator();
                        final UserModel.Column col = UserModel.Column.values()[c];
                        switch(col){
                            case CONNECTED:
                                final JMenuItem kickItem = new JMenuItem("Kick");
                                kickItem.addActionListener(
                                        e -> {
                                            if(user.connection == null)
                                                return;
                                            user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "You have been kicked by %s", Server.NAME)));
                                            user.connection.close();
                                        }
                                );
                                popup.add(kickItem);
                                break;
                            case NAME:
                                final JMenuItem deleteItem = new JMenuItem("Delete");
                                deleteItem.addActionListener(
                                        e -> {
                                            if(user.connection != null){
                                                user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.format(Server.NAME, "Your account is going to be deleted")));
                                                user.connection.close();
                                            }
                                            Server.users.remove(user);
                                            UserManager.saveUsers();
                                            Server.userTable.model.remove(user);
                                        }
                                );
                                popup.add(deleteItem);
                                break;
                            case ACCESS:
                                Arrays.stream(ServerUser.Access.values()).forEach(
                                        access -> {
                                            final JMenuItem item = new JMenuItem(access.title);
                                            item.addActionListener(
                                                    e -> {
                                                        user.access = access;
                                                        Server.userTable.model.setValueAt(user.access, user, UserModel.Column.ACCESS);
                                                        user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "Your rank has changed to %s by %s", access.title, Server.NAME)));
                                                        UserManager.saveUsers();
                                                    }
                                            );
                                            popup.add(item);
                                        }
                                );
                                break;
                            case PIXELS:
                                final JMenuItem setItem = new JMenuItem("Set");
                                setItem.addActionListener(
                                        e -> {
                                            try{
                                                final long val = Long.parseLong(JOptionPane.showInputDialog(null, "Enter pixel amount"));
                                                user.pixelCount = val;
                                                Server.userTable.model.setValueAt(user.pixelCount, user, col);
                                                user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "You now have %,d pixels", val)));
                                            }catch(Exception ex){}
                                        }
                                );
                                final JMenuItem addItem = new JMenuItem("Add");
                                addItem.addActionListener(
                                        e -> {
                                            try{
                                                final long val = Long.parseLong(JOptionPane.showInputDialog(null, "Enter pixel amount to add"));
                                                user.pixelCount += val;
                                                Server.userTable.model.setValueAt(user.pixelCount, user, col);
                                                user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "%,d pixels added; You now have %,d pixels", val, user.pixelCount)));
                                            }catch(Exception ex){}
                                        }
                                );
                                final JMenuItem takeItem = new JMenuItem("Take");
                                takeItem.addActionListener(
                                        e -> {
                                            try{
                                                final long val = Long.parseLong(JOptionPane.showInputDialog(null, "Enter pixel amount to take"));
                                                user.pixelCount -= val;
                                                Server.userTable.model.setValueAt(user.pixelCount, user, col);
                                                user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "%,d pixels removed; You now have %,d pixels", val, user.pixelCount)));
                                            }catch(Exception ex){}
                                        }
                                );
                                popup.add(setItem);
                                popup.add(addItem);
                                popup.add(takeItem);
                                break;
                            case MUTED:
                                final JMenuItem muteItem = new JMenuItem(user.muted ? "Un-Mute" : "Mute");
                                muteItem.addActionListener(
                                        e -> {
                                            user.muted = !user.muted;
                                            Server.userTable.model.setValueAt(user.muted, user, UserModel.Column.MUTED);
                                            user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "You have been %sd by %s", muteItem.getText(), Server.NAME)));
                                            UserManager.saveUsers();
                                        }
                                );
                                popup.add(muteItem);
                                break;
                            case BANNED:
                                final JMenuItem banItem = new JMenuItem(user.banned ? "Un-Ban" : "Ban");
                                banItem.addActionListener(
                                        e -> {
                                            user.banned = !user.banned;
                                            Server.userTable.model.setValueAt(user.banned, user, UserModel.Column.BANNED);
                                            if (user.banned && user.connection != null)
                                                user.connection.close();
                                            user.send(new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(Server.NAME, "You have been %sned by %s", banItem.getText(), Server.NAME)));
                                            UserManager.saveUsers();
                                        }
                                );
                                popup.add(banItem);
                                break;
                        }
                        popup.show(table, p.x, p.y);
                    }
                }
        );

        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
