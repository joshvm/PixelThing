package cats.pixelthing.client.handler;

import cats.pixelthing.client.handler.impl.ChangeColorHandler;
import cats.pixelthing.client.handler.impl.InitPixelHandler;
import cats.pixelthing.client.handler.impl.InitUserHandler;
import cats.pixelthing.client.handler.impl.LocationHandler;
import cats.pixelthing.client.handler.impl.MessageHandler;
import cats.pixelthing.client.handler.impl.ModifyHandler;
import cats.pixelthing.core.data.EventID;
import java.util.HashMap;
import java.util.Map;

public final class DataHandlers {

    private static final Map<EventID, DataHandler> MAP = new HashMap<>();

    static{
        MAP.put(EventID.MESSAGE, new MessageHandler());
        MAP.put(EventID.INIT_USER, new InitUserHandler());
        MAP.put(EventID.INIT_PIXEL, new InitPixelHandler());
        MAP.put(EventID.LOCATION, new LocationHandler());
        MAP.put(EventID.MODIFY, new ModifyHandler());
        MAP.put(EventID.CHANGE_COLOR, new ChangeColorHandler());
    }

    private DataHandlers(){}

    public static DataHandler get(final EventID id){
        return MAP.get(id);
    }
}
