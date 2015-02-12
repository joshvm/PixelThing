package cats.pixelthing.server.data;

import cats.pixelthing.server.Server;
import cats.pixelthing.server.user.ServerUser;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

public final class UserManager {

    private static final File FILE = new File("./Server/data/saved_users.dat");

    private UserManager(){}

    public static void loadUsers(){
        if(!FILE.exists())
            return;
        try{
            final DataInputStream input = new DataInputStream(new FileInputStream(FILE));
            final int count = input.readInt();
            for(int i = 0; i < count; i++){
                final int length = input.readInt();
                final byte[] bytes = new byte[length];
                input.readFully(bytes);
                final ByteBuffer buffer = ByteBuffer.wrap(bytes);
                final ServerUser user = ServerUser.decode(buffer);
                Server.users.add(user);
                Server.userTable.model.add(user);
            }
            input.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void saveUsers(){
        try{
            if(!FILE.exists())
                FILE.createNewFile();
            final DataOutputStream output = new DataOutputStream(new FileOutputStream(FILE, false));
            output.writeInt(Server.users.size());
            for(final ServerUser user : Server.users){
                final ByteBuffer buffer = user.buffer();
                final byte[] bytes = buffer.array();
                output.writeInt(bytes.length);
                output.write(bytes);
            }
            output.flush();
            output.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
