package cats.pixelthing.core.data.impl;

import cats.pixelthing.core.data.Data;
import cats.pixelthing.core.data.EventID;
import java.awt.Color;

public class InitUser extends Data {

    public static final byte SELF = 0;
    public static final byte OTHER = 1;

    public InitUser(final byte type, final long uid, final String name, final short x, final short y, final int rgb){
        super(EventID.INIT_USER, type, uid, name, x, y, rgb);
    }

    public Byte type(){
        return (Byte) args[0];
    }

    public Long uid(){
        return (Long) args[1];
    }

    public String name(){
        return (String) args[2];
    }

    public Short x(){
        return (Short) args[3];
    }

    public Short y(){
        return (Short) args[4];
    }

    public Integer rgb(){
        return (Integer) args[5];
    }

    public Color color(){
        return new Color(rgb());
    }

}
