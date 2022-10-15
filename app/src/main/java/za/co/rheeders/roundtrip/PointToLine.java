package za.co.rheeders.roundtrip;

import static java.lang.Math.sqrt;

public class PointToLine implements Comparable{
    private Destination point;
    private Destination lineVertexA;
    private Destination lineVertexB;
    private Destination pointVertexA;
    private Destination pointVertexB;
    private Double distancePointToLine;

    public PointToLine(Destination point, Destination lineVertexA, Destination lineVertexB, Destination pointVertexA, Destination pointVertexB){
        this.point = point;
        this.lineVertexA = lineVertexA;
        this.lineVertexB = lineVertexB;
        this.pointVertexA = pointVertexA;
        this.pointVertexB = pointVertexB;
        this.distancePointToLine = distancePointToLine(point, lineVertexA, lineVertexB);
    }

    @Override
    public int compareTo(Object other) {
        PointToLine pointToLine = (PointToLine) other;
        return Double.compare(this.distancePointToLine, pointToLine.distancePointToLine);
    }

    public Destination getPoint() {
        return point;
    }

    public void setPoint(Destination point) {
        this.point = point;
    }

    public Destination getLineVertexA() {
        return lineVertexA;
    }

    public void setLineVertexA(Destination lineVertexA) {
        this.lineVertexA = lineVertexA;
    }

    public Destination getLineVertexB() {
        return lineVertexB;
    }

    public void setLineVertexB(Destination lineVertexB) {
        this.lineVertexB = lineVertexB;
    }

    public Destination getPointVertexA() {
        return pointVertexA;
    }

    public void setPointVertexA(Destination pointVertexA) {
        this.pointVertexA = pointVertexA;
    }

    public Destination getPointVertexB() {
        return pointVertexB;
    }

    public void setPointVertexB(Destination pointVertexB) {
        this.pointVertexB = pointVertexB;
    }

    public Double getDistancePointToLine() {
        return distancePointToLine;
    }

    public void setDistancePointToLine(Double distancePointToLine) {
        this.distancePointToLine = distancePointToLine;
    }

    private static Double distancePointToLine(Destination point, Destination segmentPointA, Destination segmentPointB) {

        Double factor;
        Double dotProduct;
        Double len_sq;

        Double x0 = point.getLongitude();
        Double y0 = point.getLatitude();
        Double x1 = segmentPointA.getLongitude();
        Double y1 = segmentPointA.getLatitude();
        Double x2 = segmentPointB.getLongitude();
        Double y2 = segmentPointB.getLatitude();

        Double A = x0 - x1;
        Double B = y0 - y1;
        Double C = x2 - x1;
        Double D = y2 - y1;

        dotProduct = A * C + B * D;
        len_sq = C * C + D * D;
        factor = -1.0;

        if (len_sq != 0)
            factor = dotProduct/ len_sq;

        Double yy;
        Double xx;

        if (factor < 0) {
            xx = x1;
            yy = y1;
        } else if (factor > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + factor * C;
            yy = y1 + factor * D;
        }

        Double dx = x0 - xx;
        Double dy = y0 - yy;

        return sqrt(dx * dx + dy * dy);
    }
}
