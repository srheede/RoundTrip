package za.co.rheeders.roundtrip;

public class Point2D {
    public Double x;
    public Double y;

    public Point2D(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public static boolean areColinear(final Point2D p1, final Point2D p2, final Point2D p3) {
        return Math.abs(p1.x * (p2.y - p3.y) + p2.x * (p3.y - p1.y) + p3.x * (p1.y - p2.y)) == 0.0;
    }

    public Double distanceSquaredTo(final Point2D p) {
        final Double DX = x - p.x;
        final Double DY = y - p.y;

        return DX * DX + DY * DY;
    }

    public Double distanceTo(final Point2D p) {
        return Math.sqrt(distanceSquaredTo(p));
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y: " + y;
    }
}
