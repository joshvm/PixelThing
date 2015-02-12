package cats.pixelthing.server.format;

import cats.pixelthing.server.user.ServerUser;

public final class MsgFormat {

    private MsgFormat(){}

    public static String format(final String name, final String msg){
        return String.format("[%s]: %s", name, msg);
    }

    public static String formatF(final String name, final String format, final Object... args){
        return format(name, String.format(format, args));
    }

    public static String format(final String name, final ServerUser.Access access, final String msg){
        if(access == ServerUser.Access.PLAYER)
            return format(name, msg);
        return format(String.format("%s-%s", name, access.title), msg);
    }

    public static String formatF(final String name, final ServerUser.Access access, final String format, final Object... args){
        return format(name, access, String.format(format, args));
    }
}
