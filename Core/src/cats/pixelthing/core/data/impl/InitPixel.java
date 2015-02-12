package cats.pixelthing.core.data.impl;

import cats.pixelthing.core.data.Data;
import cats.pixelthing.core.data.EventID;
import java.awt.Color;

public class InitPixel extends Data{

    public InitPixel(final long id, final short x, final short y, final int rgb){
        super(EventID.INIT_PIXEL, id, x, y, rgb);
    }

    public Long id(){
        return (Long) args[0];
    }

    public Short x(){
        return (Short) args[1];
    }

    public Short y(){
        return (Short) args[2];
    }

    public Integer rgb(){
        return (Integer) args[3];
    }

    public Color color(){
        return new Color(rgb());
    }
}
