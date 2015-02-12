package cats.pixelthing.core.decode;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public final class Decoders {

    private static final Map<Class, Decoder> MAP = new HashMap<>();

    static{
        MAP.put(byte.class, (Decoder<Byte>) ByteBuffer::get);
        MAP.put(short.class, (Decoder<Short>) ByteBuffer::getShort);
        MAP.put(int.class, (Decoder<Integer>) ByteBuffer::getInt);
        MAP.put(long.class, (Decoder<Long>) ByteBuffer::getLong);
        MAP.put(String.class, (Decoder<String>) buf -> {
            final short length = buf.getShort();
            final StringBuilder builder = new StringBuilder(length);
            for(int i = 0; i < length; i++)
                builder.append((char) buf.get());
            return builder.toString();
        });
        MAP.put(boolean.class, (Decoder<Boolean>) buf -> buf.get() == 1);
    }

    private Decoders(){}

    public static <T> Decoder<T> get(final Class<T> clazz){
        return (Decoder<T>) MAP.get(clazz);
    }

    public static Decoder[] get(final Class[] classes){
        final Decoder[] decoders = new Decoder[classes.length];
        for(int i = 0; i < decoders.length; i++)
            decoders[i] = get(classes[i]);
        return decoders;
    }
}
