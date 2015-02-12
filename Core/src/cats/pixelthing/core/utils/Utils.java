package cats.pixelthing.core.utils;

import java.awt.Color;
import java.util.Random;

public final class Utils {

    public static final Random RAND = new Random();

    private Utils(){}

    public static int rand(final int min, final int max){
        return min + RAND.nextInt(max - min + 1);
    }

    public static Color color(){
        return new Color(rand(0, 255), rand(0, 255), rand(0, 255));
    }
}
