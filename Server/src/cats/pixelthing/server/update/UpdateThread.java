package cats.pixelthing.server.update;

import cats.pixelthing.core.Constants;
import cats.pixelthing.core.data.impl.InitPixel;
import cats.pixelthing.core.data.impl.Location;
import cats.pixelthing.core.data.impl.Modify;
import cats.pixelthing.core.drawable.pixel.Pixel;
import cats.pixelthing.core.utils.Utils;
import cats.pixelthing.server.Server;

public class UpdateThread extends Thread implements Runnable{

    private static final long DELAY = 35L;

    private static final long UPDATE_DELAY = 600L;
    private static final long PIXEL_DELAY = 50L;

    private long lastPixelTime;
    private long lastUpdateTime;

    public UpdateThread(){
        setPriority(MAX_PRIORITY);

        lastPixelTime = System.currentTimeMillis();
        lastUpdateTime = System.currentTimeMillis();
    }

    public void run(){
        while(true){
            updatePixels();
            update();
            try{
                Thread.sleep(DELAY);
            }catch(Exception ex){}
        }
    }

    private void update(){
        if(System.currentTimeMillis() - lastUpdateTime >= UPDATE_DELAY){
            lastUpdateTime = System.currentTimeMillis();
            Server.connected.keySet().forEach(
                    c ->                            Server.pixels.forEach(p -> c.send(new Location(Location.PIXEL, p.id, (short) p.x, (short) p.y)))
            );
        }
    }

    private void updatePixels(){
        if(System.currentTimeMillis() - lastPixelTime >= PIXEL_DELAY){
            lastPixelTime = System.currentTimeMillis();
            final Pixel pixel = new Pixel(lastPixelTime);
            pixel.x = Utils.rand(0, Constants.CANVAS_SIZE.width - Constants.PIXEL_SIZE.width);
            pixel.y = Utils.rand(0, Constants.CANVAS_SIZE.height - Constants.PIXEL_SIZE.height);
            pixel.color = Utils.color();
            Server.pixels.add(pixel);
            Server.connected.keySet().forEach(
                    c -> {
                        c.send(new Modify(Modify.PIXEL, Modify.JOIN, pixel.id));
                        c.send(new InitPixel(pixel.id, (short) pixel.x, (short) pixel.y, pixel.color.getRGB()));
                    }
            );
        }
        Server.pixels.forEach(
                p -> {
                    p.move();
                    if(p.x < 0 || p.x > Constants.CANVAS_SIZE.width + Constants.PIXEL_SIZE.width){
                        Server.pixels.remove(p);
                        Server.connected.keySet().forEach(u -> u.send(new Modify(Modify.PIXEL, Modify.LEAVE, p.id)));
                        return;
                    }
                    if(p.y < 0 || p.y > Constants.CANVAS_SIZE.height + Constants.PIXEL_SIZE.height){
                        Server.pixels.remove(p);
                        Server.connected.keySet().forEach(u -> u.send(new Modify(Modify.PIXEL, Modify.LEAVE, p.id)));
                        return;
                    }
                }
        );
    }
}
