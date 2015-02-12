package cats.pixelthing.core.encode;

import java.nio.ByteBuffer;

public interface Encoder<T> {

    public void encode(final ByteBuffer buffer, final T obj);
}
