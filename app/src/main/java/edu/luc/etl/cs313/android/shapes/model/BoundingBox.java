package edu.luc.etl.cs313.android.shapes.model;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onFill(final Fill f) {
        return f.getShape().accept(this);
    }

    @Override
    public Location onGroup(final Group g) {
        int minX = -1, maxX = -1;
        int minY = -1, maxY = -1;

        for (Shape shape : g.getShapes()) {
            Location shapeLocation = shape.accept(this);
            int x = shapeLocation.getX();
            int y = shapeLocation.getY();

            Rectangle rectangle = (Rectangle) shapeLocation.getShape();
            int width = rectangle.getWidth();
            int height = rectangle.getHeight();

            if (minX == -1 || x < minX) minX = x;
            if (maxX == -1 || x + width > maxX) maxX = x + width;
            if (minY == -1 || y < minY) minY = y;
            if (maxY == -1 || y + height > maxY) maxY = y + height;
        }

        return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));
    }

    @Override
    public Location onLocation(final Location l) {
        Shape shape = l.getShape();
        Location location = shape.accept(this);
        int x = l.getX() + location.getX();
        int y = l.getY() + location.getY();
        return new Location(x, y, location.getShape());
    }

    @Override
    public Location onRectangle(final Rectangle r) {
        return new Location(0, 0, new Rectangle(r.getWidth(), r.getHeight()));
    }

    @Override
    public Location onStrokeColor(final StrokeColor c) {
        return c.getShape().accept(this);
    }

    @Override
    public Location onOutline(final Outline o) {
        return o.shape.accept(this);
    }

    @Override
    public Location onPolygon(final Polygon s) {
        int minX = -1, maxX = -1;
        int minY = -1, maxY = -1;
        for (Shape shape : s.getShapes()) {
            Location shapeLocation = shape.accept(this);
            int x = shapeLocation.getX();
            int y = shapeLocation.getY();
            if (minX == -1 || x < minX) minX = x;
            if (maxX == -1 || x > maxX) maxX = x;
            if (minY == -1 || y < minY) minY = y;
            if (maxY == -1 || y > maxY) maxY = y;
        }
        return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));
    }

}
