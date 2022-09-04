package za.co.rheeders.roundtrip;

public class EdgeLite implements Comparable{
    public DestinationLite vertexA;
    public DestinationLite vertexB;
    public final Double weight;

    public EdgeLite(DestinationLite vertexA, DestinationLite vertexB){
        this.vertexA = vertexA;
        this.vertexB = vertexB;
        this.weight = ChristofidesAlgorithm.distanceLite(vertexA, vertexB);
    }

    @Override
    public int compareTo(Object other) {
        EdgeLite edge = (EdgeLite) other;
        return Double.compare(this.weight, edge.weight);
    }
}
