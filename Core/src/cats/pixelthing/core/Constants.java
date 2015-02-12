package cats.pixelthing.core;

import java.awt.Dimension;
import java.awt.Rectangle;

public final class Constants {

    public static final int PORT = 4595;

    public static final Dimension CANVAS_SIZE = new Dimension(700, 500);
    public static final Rectangle CANVAS_BOUNDS = new Rectangle(CANVAS_SIZE);

    public static final String TITLE = "Cats PixelThing";
    public static final int USER_SPEED = 5;
    public static final int PIXEL_SPEED = 2;

    public static final Dimension USER_SIZE = new Dimension(30, 30);
    public static final Dimension PIXEL_SIZE = new Dimension(5, 5);

    public static final String COMMAND_START_SEQUENCE = "/";

    private Constants(){}
}
