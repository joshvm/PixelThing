package cats.pixelthing.client.handler;

import cats.pixelthing.core.data.Data;

public abstract class DataHandler<T extends Data> {

    public abstract void handle(final T data);
}
