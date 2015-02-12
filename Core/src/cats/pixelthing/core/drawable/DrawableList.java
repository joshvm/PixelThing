package cats.pixelthing.core.drawable;

import java.awt.Graphics2D;
import java.util.concurrent.CopyOnWriteArrayList;

public class DrawableList<T extends Drawable> extends CopyOnWriteArrayList<T>{

    public void draw(final Graphics2D g){
        stream().forEach(u -> u.draw(g));
    }
}
