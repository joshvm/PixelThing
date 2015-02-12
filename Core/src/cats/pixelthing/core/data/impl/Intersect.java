package cats.pixelthing.core.data.impl;

import cats.pixelthing.core.data.Data;
import cats.pixelthing.core.data.EventID;

public class Intersect extends Data {

    public static final byte PIXEL = 0;

    public Intersect(final byte type, final long id){
        super(EventID.INTERSECT, type, id);
    }

    public Byte type(){
        return (Byte) args[0];
    }

    public Long id(){
        return (Long) args[1];
    }
}
