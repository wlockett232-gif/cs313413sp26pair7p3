package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import edu.luc.etl.cs313.android.shapes.model.*;

import java.util.List;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    // TODO entirely your job (except onCircle)

    private final Canvas canvas;

    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas; // FIXME
        this.paint = paint; // FIXME
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor sc) {
        final int oldColor = paint.getColor();
        paint.setColor(sc.getColor());
        sc.getShape().accept(this);
        paint.setColor(oldColor);
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        final Style oldStyle = paint.getStyle();
        paint.setStyle(Style.FILL_AND_STROKE);
        f.getShape().accept(this);
        paint.setStyle(oldStyle);
        return null;
    }
    @Override
    public Void onGroup(final Group group) {
        for (final Shape s : group.getShapes()) {
            s.accept(this);
        }
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        canvas.translate(l.getX(), l.getY());
        l.getShape().accept(this);
        canvas.translate(-l.getX(), -l.getY());
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle rect) {
        canvas.drawRect(0, 0, rect.getWidth(), rect.getHeight(), paint);
        return null;
    }

    @Override
    public Void onOutline(final Outline outline) {
        final Style oldStyle = paint.getStyle();
        paint.setStyle(Style.STROKE);
        outline.getShape().accept(this);
        paint.setStyle(oldStyle);
        return null;
    }

    @Override
    public Void onPolygon(final Polygon poly) {
        final List<? extends Point> points = poly.getPoints();
        if (points != null && points.size() > 1) {
            final float[] pts = new float[points.size() * 4];
            int i = 0;
            for (int j = 0; j < points.size() - 1; j++) {
                pts[i++] = points.get(j).getX();
                pts[i++] = points.get(j).getY();
                pts[i++] = points.get(j + 1).getX();
                pts[i++] = points.get(j + 1).getY();
            }

            pts[i++] = points.get(points.size() - 1).getX();
            pts[i++] = points.get(points.size() - 1).getY();
            pts[i++] = points.get(0).getX();
            pts[i++] = points.get(0).getY();

            canvas.drawLines(pts, paint);
        }
        return null;
    }
}
