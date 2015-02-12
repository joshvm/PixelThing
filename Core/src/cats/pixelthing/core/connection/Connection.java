package cats.pixelthing.core.connection;

import cats.pixelthing.core.Constants;
import cats.pixelthing.core.connection.event.ConnectionListener;
import cats.pixelthing.core.data.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class Connection extends Thread implements Runnable{

    private final Socket socket;
    private final DataOutputStream output;
    private final DataInputStream input;

    public final List<ConnectionListener> listeners;

    public Connection(final Socket socket) throws IOException{
        this.socket = socket;

        listeners = new LinkedList<>();

        output = new DataOutputStream(socket.getOutputStream());
        output.flush();

        input = new DataInputStream(socket.getInputStream());

        start();
    }

    public Connection() throws IOException{
        this(new Socket(InetAddress.getLocalHost(), Constants.PORT));
    }

    public boolean isConnected(){
        return socket.isConnected();
    }

    private void fireDataReceived(final Data data){
        listeners.forEach(l -> l.dataReceived(Connection.this, data));
    }

    private void fireConnectionClosed(){
        listeners.forEach(l -> l.connectionClosed(Connection.this));
    }

    public boolean close(){
        try{
            output.close();
            input.close();
            socket.close();
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    public synchronized boolean send(final Data data){
        if(!isConnected())
            return false;
        try{
            final ByteBuffer buffer = data.buffer();
            final byte[] bytes = buffer.array();
            output.writeInt(bytes.length);
            output.write(bytes);
            output.flush();
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    public void run(){
        while(socket.isConnected()){
            try{
                final int length = input.readInt();
                final byte[] bytes = new byte[length];
                input.readFully(bytes);
                final ByteBuffer buffer = ByteBuffer.wrap(bytes);
                final Data data = Data.decode(buffer);
                fireDataReceived(data);
            }catch(Exception ex){
                close();
                break;
            }
        }
        fireConnectionClosed();
    }

    public static Connection create(final Socket socket){
        try{
            return new Connection(socket);
        }catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static Connection create(){
        try{
            return new Connection();
        }catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }
}
