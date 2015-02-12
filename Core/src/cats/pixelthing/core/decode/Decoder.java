package cats.pixelthing.core.decode;

import java.nio.ByteBuffer;

public interface Decoder<T> {

    public T decode(final ByteBuffer buffer);
}
