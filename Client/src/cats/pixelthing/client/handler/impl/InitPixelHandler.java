package cats.pixelthing.client.handler.impl;

import cats.pixelthing.client.Client;
import cats.pixelthing.client.handler.DataHandler;
import cats.pixelthing.core.data.impl.InitPixel;
import cats.pixelthing.core.drawable.pixel.Pixel;

public class InitPixelHandler extends DataHandler<InitPixel> {

    public void handle(final InitPixel init){
        final Pixel pixel = Client.pixels.getByID(init.id());
        if(pixel == null)
            return;
        pixel.x = init.x();
        pixel.y = init.y();
        pixel.color = init.color();
    }
}
