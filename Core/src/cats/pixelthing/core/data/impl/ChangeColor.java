package cats.pixelthing.core.data.impl;

import cats.pixelthing.core.data.Data;
import cats.pixelthing.core.data.EventID;
import java.awt.Color;

public class ChangeColor extends Data{

    public static final byte PIXEL = 0;
    public static final byte USER = 1;

    public ChangeColor(final byte type, final long id, final int rgb){
        super(EventID.CHANGE_COLOR, type, id, rgb);
    }

    public Byte type(){
        return (Byte) args[0];
    }

    public Long id(){
        return (Long) args[1];
    }

    public int rgb(){
        return (Integer) args[2];
    }

    public Color color(){
        return new Color(rgb());
    }
}
