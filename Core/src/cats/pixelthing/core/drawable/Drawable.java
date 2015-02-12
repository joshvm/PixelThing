package cats.pixelthing.core.drawable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;

public class Drawable extends Rectangle{

    public Color color;

    protected Drawable(final int x, final int y, final Dimension d){
        super(x, y, d.width, d.height);

        color = Color.BLACK;
    }

    protected Drawable(final Dimension d){
        this(0, 0, d);
    }

    public void draw(final Graphics2D g){
        final Paint paint = new GradientPaint(x, y, color, x+width, y+height, color.brighter());
        g.setPaint(paint);
        g.fillRect(x, y, width, height);
    }
}
