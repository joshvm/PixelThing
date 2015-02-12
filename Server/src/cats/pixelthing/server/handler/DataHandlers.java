package cats.pixelthing.server.handler;

import cats.pixelthing.core.data.EventID;
import cats.pixelthing.server.handler.impl.ChangeColorHandler;
import cats.pixelthing.server.handler.impl.IntersectHandler;
import cats.pixelthing.server.handler.impl.LocationHandler;
import cats.pixelthing.server.handler.impl.LoginHandler;
import cats.pixelthing.server.handler.impl.MessageHandler;
import cats.pixelthing.server.handler.impl.RegisterHandler;
import java.util.HashMap;
import java.util.Map;

public class DataHandlers {

    private static final Map<EventID, DataHandler> MAP = new HashMap<>();

    static{
        MAP.put(EventID.REGISTER, new RegisterHandler());
        MAP.put(EventID.LOGIN, new LoginHandler());
        MAP.put(EventID.MESSAGE, new MessageHandler());
        MAP.put(EventID.LOCATION, new LocationHandler());
        MAP.put(EventID.CHANGE_COLOR, new ChangeColorHandler());
        MAP.put(EventID.INTERSECT, new IntersectHandler());
    }

    private DataHandlers(){}

    public static DataHandler get(final EventID id){
        return MAP.get(id);
    }
}
