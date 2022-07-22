package za.co.rheeders.roundtrip;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Edge implements Comparable{
    private Destination vertexA;
    private Destination vertexB;
    private final Double weight;
    private final Double gradient;
    private final Double intercept;
    private boolean removed = false;

    public Edge(Destination vertexA, Destination vertexB){
        this.vertexA = vertexA;
        this.vertexB = vertexB;
        this.weight = distance(vertexA, vertexB);
        this.gradient = calculateGradient();
        this.intercept = calculateIntercept();
    }

    @Override
    public int compareTo(Object other) {
        Edge edge = (Edge) other;
        return Double.compare(this.weight, edge.weight);
    }

    public Destination getVertexA() {
        return vertexA;
    }

    public Destination getVertexB() {
        return vertexB;
    }

    public Double getWeight() {
        return weight;
    }

    public Double getGradient() {
        return gradient;
    }

    public Double getIntercept() {
        return intercept;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    private static Double distance(Destination destination1, Destination destination2) {

        Double lat1 = destination1.getLatitude();
        Double lon1 = destination1.getLongitude();
        Double lat2 = destination2.getLatitude();
        Double lon2 = destination2.getLongitude();

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        Double dlon = lon2 - lon1;
        Double dlat = lat2 - lat1;
        Double a = pow(Math.sin(dlat / 2.0), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * pow(Math.sin(dlon / 2.0), 2);

        Double c = 2.0 * Math.asin(sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        Double r = 6371.0;

        // calculate the result
        return (c * r);
    }

    private Double calculateGradient() {
        return (vertexB.getLatitude() - vertexA.getLatitude()) / (vertexB.getLongitude() - vertexA.getLongitude());
    }

    private Double calculateIntercept() {
        return (vertexA.getLatitude() - (gradient * vertexA.getLongitude()));
    }
}
