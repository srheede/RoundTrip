package za.co.rheeders.roundtrip;

public class Improvement implements Comparable {
    private Edge edgeA;
    private Edge edgeB;
    private Edge newEdge;
    private Destination center;
    private Double weightImprovement;

    public Improvement(Edge edgeA, Edge edgeB, Destination center){
        this.edgeA = edgeA;
        this.edgeB = edgeB;
        this.center = center;

        createEdge();
    }

    @Override
    public int compareTo(Object other) {
        Improvement improvement = (Improvement) other;
        return Double.compare(improvement.weightImprovement, this.weightImprovement);
    }

    private void createEdge() {
        if (edgeA.compareTo(edgeB) == 0){
            System.out.println("here");
        }
        if (edgeA.getVertexA().equals(center)){
            if(edgeB.getVertexA().equals(center)){
                newEdge = new Edge(edgeA.getVertexB(), edgeB.getVertexB());
            } else {
                newEdge = new Edge(edgeA.getVertexB(), edgeB.getVertexA());
            }
        } else {
            if(edgeB.getVertexA().equals(center)){
                newEdge = new Edge(edgeA.getVertexA(), edgeB.getVertexB());
            } else {
                newEdge = new Edge(edgeA.getVertexA(), edgeB.getVertexA());
            }
        }
        weightImprovement = (edgeA.getWeight() + edgeB.getWeight()) - newEdge.getWeight();
    }

    public Edge getEdgeA() {
        return edgeA;
    }

    public Edge getEdgeB() {
        return edgeB;
    }

    public Edge getNewEdge() {
        return newEdge;
    }

    public Destination getCenter() {
        return center;
    }

    public Double getWeightImprovement() {
        return weightImprovement;
    }
}
