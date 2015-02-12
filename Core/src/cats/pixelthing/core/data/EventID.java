package cats.pixelthing.core.data;

import cats.pixelthing.core.data.impl.ChangeColor;
import cats.pixelthing.core.data.impl.InitPixel;
import cats.pixelthing.core.data.impl.InitUser;
import cats.pixelthing.core.data.impl.Intersect;
import cats.pixelthing.core.data.impl.Location;
import cats.pixelthing.core.data.impl.Login;
import cats.pixelthing.core.data.impl.Message;
import cats.pixelthing.core.data.impl.Modify;
import cats.pixelthing.core.data.impl.Register;
import cats.pixelthing.core.decode.Decoder;
import cats.pixelthing.core.decode.Decoders;
import cats.pixelthing.core.encode.Encoder;
import cats.pixelthing.core.encode.Encoders;

public enum EventID {

    REGISTER(Register.class, String.class, String.class),
    LOGIN(Login.class, String.class, String.class),
    MODIFY(Modify.class, byte.class, byte.class, long.class),
    INIT_USER(InitUser.class, byte.class, long.class, String.class, short.class, short.class, int.class),
    INIT_PIXEL(InitPixel.class, long.class, short.class, short.class, int.class),
    MESSAGE(Message.class, byte.class, String.class),
    LOCATION(Location.class, byte.class, long.class, short.class, short.class),
    INTERSECT(Intersect.class, byte.class, long.class),
    CHANGE_COLOR(ChangeColor.class, byte.class, long.class, int.class);

    public final byte opcode;
    public final Class dataClass;
    public final Class[] argTypes;
    public final Encoder[] encoders;
    public final Decoder[] decoders;

    private EventID(final Class dataClass, final Class... argTypes){
        this.opcode = (byte) ordinal();
        this.dataClass = dataClass;
        this.argTypes = argTypes;

        encoders = Encoders.get(argTypes);
        decoders = Decoders.get(argTypes);
    }

    public static EventID fromId(final byte id){
        for(final EventID e : values())
            if(e.opcode == id)
                return e;
        return null;
    }
}
