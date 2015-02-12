package cats.pixelthing.core.data;

import cats.pixelthing.core.encode.utils.EncodeUtils;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;

public class Data {

    public final EventID id;
    protected final Object[] args;

    public Data(final EventID id, final Object... args){
        this.id = id;
        this.args = args;
    }

    public ByteBuffer buffer(){
        final int capacity = EncodeUtils.length(args) + 1;
        final ByteBuffer buffer = ByteBuffer.allocate(capacity);
        buffer.put(id.opcode);
        for(int i = 0; i < args.length; i++)
            id.encoders[i].encode(buffer, args[i]);
        return buffer;
    }

    public static <T extends Data> T decode(final ByteBuffer buffer){
        final EventID id = EventID.fromId(buffer.get());
        final Object[] args = new Object[id.argTypes.length];
        for(int i = 0; i < args.length; i++)
            args[i] = id.decoders[i].decode(buffer);
        try{
           final Constructor<T> constructor = id.dataClass.getConstructor(id.argTypes);
            return constructor.newInstance(args);
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
