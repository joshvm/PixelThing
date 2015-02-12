package cats.pixelthing.core.data.impl;

import cats.pixelthing.core.data.Data;
import cats.pixelthing.core.data.EventID;

public class Message extends Data {

    public static final byte POPUP = 0;
    public static final byte PUBLIC_CHAT = 1;
    public static final byte COMMAND = 2;

    public Message(final byte type, final String msg){
        super(EventID.MESSAGE, type, msg);
    }

    public Byte type(){
        return (Byte) args[0];
    }

    public String msg(){
        return ((String) args[1]).trim();
    }
}
