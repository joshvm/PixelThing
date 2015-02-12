package cats.pixelthing.core.encode.utils;

public final class EncodeUtils {

    private EncodeUtils(){}

    public static int length(final Object... args){
        int capacity = 0;
        for(final Object o : args){
            if(o instanceof Byte || o instanceof Boolean)
                capacity += 1;
            else if(o instanceof Short)
                capacity += 2;
            else if(o instanceof Integer)
                capacity += 4;
            else if(o instanceof Long)
                capacity += 8;
            else if(o instanceof String)
                capacity += 2 + ((String) o).length();
        }
        return capacity;
    }
}
