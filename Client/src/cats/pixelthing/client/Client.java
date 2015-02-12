package cats.pixelthing.client;

import cats.pixelthing.client.comp.CanvasComponent;
import cats.pixelthing.client.comp.ChatAreaComponent;
import cats.pixelthing.client.comp.RegisterLoginComponent;
import cats.pixelthing.client.handler.DataHandler;
import cats.pixelthing.client.handler.DataHandlers;
import cats.pixelthing.core.Constants;
import cats.pixelthing.core.connection.Connection;
import cats.pixelthing.core.connection.event.ConnectionListener;
import cats.pixelthing.core.data.Data;
import cats.pixelthing.core.drawable.pixel.PixelList;
import cats.pixelthing.core.drawable.user.User;
import cats.pixelthing.core.drawable.user.UserList;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class Client extends JFrame {

    private static class Listener implements ConnectionListener {

        public void dataReceived(final Connection con, final Data data){
            final DataHandler handler = DataHandlers.get(data.id);
            if(handler == null)
                return;
            try{
                handler.handle(data);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        public void connectionClosed(final Connection con){
            connection = null;
        }
    }

    private static final Listener LISTENER = new Listener();

    private static Client instance;

    public static Connection connection;
    public static User user;

    public static UserList<User> users;
    public static PixelList pixels;

    public static RegisterLoginComponent registerLoginComponent;
    public static CanvasComponent canvasComponent;
    public static ChatAreaComponent chatAreaComponent;

    static{
        initConnection();

        users = new UserList<>();
        pixels = new PixelList();
    }

    public Client(){
        super(Constants.TITLE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationByPlatform(true);
        setLayout(new BorderLayout());

        registerLoginComponent = new RegisterLoginComponent();
        canvasComponent = new CanvasComponent();
        chatAreaComponent = new ChatAreaComponent();

        add(registerLoginComponent, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    public static void loadGameScreen(){
        chatAreaComponent.initUserLabel();
        instance.remove(registerLoginComponent);
        instance.add(canvasComponent, BorderLayout.CENTER);
        instance.add(chatAreaComponent, BorderLayout.SOUTH);
        instance.revalidate();
        instance.pack();

        canvasComponent.start();
    }

    public static void initConnection(){
        connection = Connection.create();
        connection.listeners.add(LISTENER);
    }

    public static boolean checkConnection(){
        if(connected())
            return true;
        final int v = JOptionPane.showConfirmDialog(null, "You're not connected, do you want to try re-connecting?");
        if(v == JOptionPane.OK_OPTION)
            initConnection();
        return checkConnection();
    }

    public static void send(final Data data){
        checkConnection();
        if(!connection.send(data))
            checkConnection();
    }

    public static boolean connected(){
        return connection != null && connection.isConnected();
    }

    public static void main(String[] args){
        instance = new Client();
    }
}
