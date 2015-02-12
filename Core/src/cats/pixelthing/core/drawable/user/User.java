package cats.pixelthing.core.drawable.user;

import cats.pixelthing.core.Constants;
import cats.pixelthing.core.drawable.Drawable;
import java.awt.Color;
import java.awt.Graphics2D;

public class User extends Drawable{

    public final long uid;
    public String name;
    public long pixelCount;

    public User(final long uid){
        super(Constants.USER_SIZE);
        this.uid = uid;

        name = "";

        color = Color.BLACK;
    }

    public void draw(final Graphics2D g){
        super.draw(g);
        g.setColor(color);
        g.drawString(name, x, y-5);
    }

    public boolean equals(final Object o){
        if(o == null)
            return false;
        if(o == this)
            return true;
        if(!(o instanceof User))
            return false;
        return uid == ((User) o).uid;
    }
}
