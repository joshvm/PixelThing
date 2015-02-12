package cats.pixelthing.client.comp;

import cats.pixelthing.client.Client;
import cats.pixelthing.core.Constants;
import cats.pixelthing.core.data.impl.Intersect;
import cats.pixelthing.core.data.impl.Location;
import cats.pixelthing.core.drawable.pixel.Pixel;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class CanvasComponent extends Canvas implements Runnable, KeyListener{

    public static final int LEFT = KeyEvent.VK_LEFT;
    public static final int RIGHT = KeyEvent.VK_RIGHT;
    public static final int UP = KeyEvent.VK_UP;
    public static final int DOWN = KeyEvent.VK_DOWN;

    private static final Paint PAINT = new GradientPaint(0,0, Color.DARK_GRAY, Constants.CANVAS_SIZE.width, Constants.CANVAS_SIZE.height, Color.WHITE);

    private static final long DELAY = 35L;

    private boolean left, right, up, down;

    private BufferStrategy buffer;

    public CanvasComponent(){
        setPreferredSize(Constants.CANVAS_SIZE);
        addKeyListener(this);
    }

    public void process(final KeyEvent e){
        e.setSource(this);
        dispatchEvent(e);
    }

    private void draw(){
        final Graphics2D g = (Graphics2D) buffer.getDrawGraphics();
        g.clearRect(0, 0, Constants.CANVAS_SIZE.width, Constants.CANVAS_SIZE.height);
        g.setPaint(PAINT);
        g.fillRect(0, 0, Constants.CANVAS_SIZE.width, Constants.CANVAS_SIZE.height);
        if(Client.connected()){
            Client.users.draw(g);
            Client.user.draw(g);
            Client.pixels.draw(g);
        }else{
            g.setFont(g.getFont().deriveFont(40F));
            g.setColor(Color.RED);
            g.drawString("You are not logged in", 50, 50);
            g.drawString("Restart the client", 50, 100);
        }
        buffer.show();
    }

    private void update(){
        if(!Client.connected())
            return;
        Client.pixels.forEach(Pixel::move);
        final int dx = Client.user.x + (left ? -Constants.USER_SPEED : right ? Constants.USER_SPEED : 0);
        final int dy = Client.user.y + (up ? -Constants.USER_SPEED : down ? Constants.USER_SPEED : 0);
        boolean validMove = !((dx == Client.user.x && dy == Client.user.y) || (dx < 0 || dx + Constants.USER_SIZE.width > Constants.CANVAS_SIZE.width) || (dy < 0 || dy + Constants.USER_SIZE.height > Constants.CANVAS_BOUNDS.height));
        if(validMove)
            Client.send(new Location(Location.USER, Client.user.uid, (short)dx, (short)dy));
        final Rectangle bounds = new Rectangle(dx, dy, Client.user.width, Client.user.height);
        Client.pixels.stream().filter(
                p -> p.intersects(bounds)).forEach(
                p -> Client.send(new Intersect(Intersect.PIXEL, p.id)));
    }

    public void start(){
        createBufferStrategy(2);
        buffer = getBufferStrategy();

        final Thread t = new Thread(this);
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    public void run(){
        while(true){
            update();
            draw();
            try{
                Thread.sleep(DELAY);
            }catch(Exception ex){}
        }
    }

    public void keyPressed(final KeyEvent e){
        switch(e.getKeyCode()){
            case LEFT:
                left = !(right = false);
                break;
            case RIGHT:
                right = !(left = false);
                break;
            case UP:
                up = !(down = false);
                break;
            case DOWN:
                down = !(up = false);
                break;
        }
    }

    public void keyReleased(final KeyEvent e){
        switch(e.getKeyCode()){
            case LEFT:
                left = false;
                break;
            case RIGHT:
                right = false;
                break;
            case UP:
                up = false;
                break;
            case DOWN:
                down = false;
                break;
        }
    }

    public void keyTyped(final KeyEvent e){}
}
