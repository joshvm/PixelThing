package cats.pixelthing.server.handler;

import cats.pixelthing.core.connection.Connection;
import cats.pixelthing.core.data.Data;

public abstract class DataHandler<T extends Data> {

    public abstract void handle(final Connection connection, final T data);
}
