package cats.pixelthing.core.drawable.user;

import cats.pixelthing.core.drawable.DrawableList;

public class UserList<T extends User> extends DrawableList<T> {

    public T getByUid(final long uid){
        return stream().filter(u -> u.uid == uid).findFirst().orElse(null);
    }

    public T getByName(final String name){
        return stream().filter(u -> u.name.equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
