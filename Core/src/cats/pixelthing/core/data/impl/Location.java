package cats.pixelthing.core.data.impl;

import cats.pixelthing.core.data.Data;
import cats.pixelthing.core.data.EventID;
import java.awt.Point;

public class Location extends Data {

    public static final byte PIXEL = 0;
    public static final byte USER = 1;

    public Location(final byte type, final long id, final short x, final short y){
        super(EventID.LOCATION, type, id, x, y);
    }

    public Byte type(){
        return (Byte) args[0];
    }

    public Long id(){
        return (Long) args[1];
    }

    public Short x(){
        return (Short) args[2];
    }

    public Short y(){
        return (Short) args[3];
    }

    public Point point(){
        return new Point(x(), y());
    }
}
