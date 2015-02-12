package cats.pixelthing.core.connection.event;

import cats.pixelthing.core.connection.Connection;
import cats.pixelthing.core.data.Data;

public interface ConnectionListener {

    public void dataReceived(final Connection connection, final Data data);

    public void connectionClosed(final Connection connection);
}
