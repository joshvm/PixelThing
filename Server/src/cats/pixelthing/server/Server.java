package cats.pixelthing.server;

import cats.pixelthing.core.Constants;
import cats.pixelthing.core.connection.Connection;
import cats.pixelthing.core.connection.event.ConnectionListener;
import cats.pixelthing.core.data.Data;
import cats.pixelthing.core.data.impl.Message;
import cats.pixelthing.core.data.impl.Modify;
import cats.pixelthing.core.drawable.pixel.PixelList;
import cats.pixelthing.core.drawable.user.UserList;
import cats.pixelthing.server.data.UserManager;
import cats.pixelthing.server.format.MsgFormat;
import cats.pixelthing.server.handler.DataHandler;
import cats.pixelthing.server.handler.DataHandlers;
import cats.pixelthing.server.table.UserModel;
import cats.pixelthing.server.table.UserTable;
import cats.pixelthing.server.update.UpdateThread;
import cats.pixelthing.server.user.ServerUser;
import java.awt.BorderLayout;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Server extends JFrame implements Runnable, ConnectionListener{

    public static final String NAME = "SERVER";

    public static UserList<ServerUser> users;
    public static Map<Connection, ServerUser> connected;
    public static PixelList pixels;

    static{
        pixels = new PixelList();

        connected = new HashMap<>();

        userTable = new UserTable();

        users = new UserList<>();
        UserManager.loadUsers();

        Runtime.getRuntime().addShutdownHook(new Thread(UserManager::saveUsers));
    }

    public static UserTable userTable;

    public Server(){
        super("Server");
        setLayout(new BorderLayout());
        setLocationByPlatform(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 500);

        final JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> UserManager.saveUsers());

        add(saveButton, BorderLayout.NORTH);
        add(userTable, BorderLayout.CENTER);

        setVisible(true);
    }

    public void run(){
        try{
            final ServerSocket server = new ServerSocket(Constants.PORT);
            while(server.isBound()){
                try{
                    final Socket socket = server.accept();
                    final Connection connection = Connection.create(socket);
                    connection.listeners.add(this);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void connectionClosed(final Connection connection){
        final ServerUser user = connected.remove(connection);
        if(user == null)
            return;
        user.connection = null;
        userTable.model.setValueAt(user.connection, user, UserModel.Column.CONNECTED);
        final Modify leave = new Modify(Modify.USER, Modify.LEAVE, user.uid);
        connected.keySet().forEach(u -> u.send(leave));
        final Message msg = new Message(Message.PUBLIC_CHAT, MsgFormat.formatF(NAME, "%s has just left %s", user.name, Constants.TITLE));
        connected.keySet().forEach(u -> u.send(msg));
    }

    public void dataReceived(final Connection connection, final Data data){
        final DataHandler handler = DataHandlers.get(data.id);
        if(handler == null)
            return;
        try{
            handler.handle(connection, data);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void start(){
        final Thread t = new Thread(this);
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
        new UpdateThread().start();
    }

    public static void main(String args[]){
        new Server().start();
    }

}
