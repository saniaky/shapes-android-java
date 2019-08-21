package edu.luc.etl.cs313.android.shapes.model;

/**
 * A point, implemented as a location without a shape.
 * <p>
 * A Point is a Location without a Shape.
 * You should implement it using a Circle with radius 0
 * as its Shape and override any methods as needed.
 */
public class Point extends Location {

    // TODO your job

    public Point(final int x, final int y) {
        // HINT: use a circle with radius 0 as the shape!
        super(-1, -1, new Circle(0));
        assert x >= 0;
        assert y >= 0;
    }

    @Override
    public <Result> Result accept(Visitor<Result> v) {
        return super.accept(v);
    }

}
