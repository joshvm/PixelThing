package cats.pixelthing.core.data.impl;

import cats.pixelthing.core.data.Data;
import cats.pixelthing.core.data.EventID;

public class Modify extends Data {

    public static final byte USER = 0;
    public static final byte PIXEL = 1;

    public static final byte JOIN = 0;
    public static final byte LEAVE = 1;

    public Modify(final byte type, final byte mod, final long id){
        super(EventID.MODIFY, type, mod, id);
    }

    public Byte type(){
        return (Byte) args[0];
    }

    public Byte mod(){
        return (Byte) args[1];
    }

    public Long id(){
        return (Long) args[2];
    }
}
