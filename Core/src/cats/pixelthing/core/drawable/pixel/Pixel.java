package cats.pixelthing.core.drawable.pixel;

import cats.pixelthing.core.Constants;
import cats.pixelthing.core.drawable.Drawable;

public class Pixel extends Drawable{

    public final long id;

    public Pixel(final long id){
        super(Constants.PIXEL_SIZE);
        this.id = id;
    }
    
    public void move(){
        if(id % 8 == 0){ //up
            y -= Constants.PIXEL_SPEED;
        }else if(id % 7 == 0){ //right
            x += Constants.PIXEL_SPEED;
        }else if(id % 6 == 0){ //down
            y += Constants.PIXEL_SPEED;
        }else if(id % 5 == 0){ //left
            x -= Constants.PIXEL_SPEED;
        }else if(id % 4 == 0){ //up left
            y -= Constants.PIXEL_SPEED;
            x -= Constants.PIXEL_SPEED;
        }else if(id % 3 == 0){ //up right
            y -= Constants.PIXEL_SPEED;
            x += Constants.PIXEL_SPEED;
        }else if(id % 2 == 0){ //down left
            y += Constants.PIXEL_SPEED;
            x -= Constants.PIXEL_SPEED;
        }else if(id % 1 == 0){ //down right
            y += Constants.PIXEL_SPEED;
            x += Constants.PIXEL_SPEED;
        }
    }
}
