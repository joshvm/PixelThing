package cats.pixelthing.core.drawable.pixel;

import cats.pixelthing.core.drawable.DrawableList;

public class PixelList extends DrawableList<Pixel> {

    public Pixel getByID(final long id){
        return stream().filter(p -> p.id == id).findFirst().orElse(null);
    }
}
