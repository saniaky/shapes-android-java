package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import java.util.List;

import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    private final Canvas canvas;

    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas;
        this.paint = paint;
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        int defaultColor = paint.getColor();

        paint.setColor(c.getColor());
        c.getShape().accept(this);

        // reset color
        paint.setColor(defaultColor);
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        Style defaultStyle = paint.getStyle();

        paint.setStyle(Style.FILL_AND_STROKE);
        f.getShape().accept(this);

        // reset style
        paint.setStyle(defaultStyle);
        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        for (Shape shape : g.getShapes()) {
            shape.accept(this);
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
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onOutline(Outline o) {
        Style shapeOutline = paint.getStyle();

        paint.setStyle(Style.STROKE);
        o.getShape().accept(this);

        // reset style
        paint.setStyle(shapeOutline);
        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {
        List<? extends Point> points = s.getPoints();
        final int N = points.size();
        final float[] pts = new float[N * 4];

        int idx = 0;
        for (int i = 0; i < N - 1; i++) {
            pts[idx++] = points.get(i).getX();
            pts[idx++] = points.get(i).getY();
            pts[idx++] = points.get(i + 1).getX();
            pts[idx++] = points.get(i + 1).getY();
        }

        // draw last line
        pts[idx++] = points.get(N - 1).getX();
        pts[idx++] = points.get(N - 1).getY();
        pts[idx++] = points.get(0).getX();
        pts[idx] = points.get(0).getY();

        canvas.drawLines(pts, paint);

        return null;
    }
}
