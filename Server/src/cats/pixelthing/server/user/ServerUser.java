package cats.pixelthing.server.user;

import cats.pixelthing.core.connection.Connection;
import cats.pixelthing.core.data.Data;
import cats.pixelthing.core.decode.Decoders;
import cats.pixelthing.core.encode.Encoder;
import cats.pixelthing.core.encode.Encoders;
import cats.pixelthing.core.encode.utils.EncodeUtils;
import cats.pixelthing.core.drawable.user.User;
import java.awt.Color;
import java.nio.ByteBuffer;

public class ServerUser extends User{

    public enum Access {
        PLAYER("Player"),
        MODERATOR("Mod"),
        ADMINISTRATOR("Admin"),
        OWNER("Owner");

        public final String title;

        private Access(final String title){
            this.title = title;
        }

    }

    private static final Class[] CLASSES = {long.class, String.class, String.class, short.class, short.class, long.class, boolean.class, boolean.class, int.class, byte.class};
    private static final Encoder[] ENCODERS = Encoders.get(CLASSES);

    public String pass;
    public Connection connection;
    public boolean muted;
    public boolean banned;
    public Access access;

    public ServerUser(final long uid){
        super(uid);

        access = Access.PLAYER;
    }

    public String name(){
        if(access == Access.PLAYER)
            return name;
        return String.format("%s-%s", name, access.title);
    }

    public synchronized boolean send(final Data data){
        return connection != null && connection.isConnected() && connection.send(data);
    }

    public ByteBuffer buffer(){
        final Object[] args = {uid, name, pass, (short)x, (short)y, pixelCount, muted, banned, color.getRGB(), (byte)access.ordinal()};
        final int length = EncodeUtils.length(args);
        final ByteBuffer buffer = ByteBuffer.allocate(length);
        for(int i = 0; i < ENCODERS.length; i++)
            ENCODERS[i].encode(buffer, args[i]);
        return buffer;
    }

    public static ServerUser decode(final ByteBuffer input){
        final ServerUser user = new ServerUser(input.getLong());
        user.name = Decoders.get(String.class).decode(input);
        user.pass = Decoders.get(String.class).decode(input);
        user.x = Decoders.get(short.class).decode(input);
        user.y = Decoders.get(short.class).decode(input);
        user.pixelCount = Decoders.get(long.class).decode(input);
        user.muted = Decoders.get(boolean.class).decode(input);
        user.banned = Decoders.get(boolean.class).decode(input);
        user.color = new Color(Decoders.get(int.class).decode(input));
        user.access = Access.values()[Decoders.get(byte.class).decode(input)];
        return user;
    }

}
