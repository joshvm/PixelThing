package cats.pixelthing.core.encode;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public final class Encoders {

    private static final Map<Class, Encoder> MAP = new HashMap<>();

    static{
        MAP.put(byte.class, (Encoder<Byte>) ByteBuffer::put);
        MAP.put(short.class, (Encoder<Short>) ByteBuffer::putShort);
        MAP.put(int.class, (Encoder<Integer>) ByteBuffer::putInt);
        MAP.put(long.class, (Encoder<Long>) ByteBuffer::putLong);
        MAP.put(String.class, (Encoder<String>) (buf, str) -> {
            buf.putShort((short)str.length());
            for(final char c : str.toCharArray())
                buf.put((byte)c);
        });
        MAP.put(boolean.class, (Encoder<Boolean>) (buf, bool) -> buf.put((byte)(bool ? 1 : 0)));
    }

    private Encoders(){}

    public static <T> Encoder<T> get(final Class<T> clazz){
        return (Encoder<T>) MAP.get(clazz);
    }

    public static Encoder[] get(final Class[] classes){
        final Encoder[] encoders = new Encoder[classes.length];
        for(int i = 0; i < encoders.length; i++)
            encoders[i] = get(classes[i]);
        return encoders;
    }
}
