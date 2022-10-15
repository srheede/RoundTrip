package za.co.rheeders.roundtrip;

import java.util.ArrayList;
import java.util.Collections;

public class EdgeSegment {
    private Destination endVertexA;
    private Destination endVertexB;
    private ArrayList<Destination> destinations;
    private Double hypotheticalNextEdgeWeight;
    private EdgeSegment addedSegment;

    public EdgeSegment(ArrayList<Destination> destinations) {
        this.destinations = destinations;
        this.endVertexA = destinations.get(0);
        this.endVertexB = destinations.get(destinations.size() - 1);
        System.out.println("shit");

    }

    public EdgeSegment(Edge edge, ArrayList<Destination> destinationsA, ArrayList<Destination> destinationsB) {

        if (destinationsA.stream().anyMatch(destination -> destination.equals(edge.getVertexA()))){
            if (destinationsA.get(0).equals(edge.getVertexA()) && destinationsB.get(0).equals(edge.getVertexB())){
                Collections.reverse(destinationsB);
                destinations = destinationsB;
                destinations.addAll(destinationsA);
            } else if (destinationsA.get(0).equals(edge.getVertexA()) && destinationsB.get(destinationsB.size() -1).equals(edge.getVertexB())){
                destinations = destinationsB;
                destinations.addAll(destinationsA);
            } else if (destinationsA.get(destinationsA.size() - 1).equals(edge.getVertexA()) && destinationsB.get(0).equals(edge.getVertexB())){
                destinations = destinationsA;
                destinations.addAll(destinationsB);
            } else if (destinationsA.get(destinationsA.size() -1).equals(edge.getVertexA()) && destinationsB.get(destinationsB.size() -1).equals(edge.getVertexB())){
                destinations = destinationsA;
                Collections.reverse(destinationsB);
                destinations.addAll(destinationsB);
            } else {
                System.out.println("shit");
            }
        } else {
            if (destinationsA.get(0).equals(edge.getVertexB()) && destinationsB.get(0).equals(edge.getVertexA())){
                Collections.reverse(destinationsB);
                destinations = destinationsB;
                destinations.addAll(destinationsA);
            } else if (destinationsA.get(0).equals(edge.getVertexB()) && destinationsB.get(destinationsB.size() -1).equals(edge.getVertexA())){
                destinations = destinationsB;
                destinations.addAll(destinationsA);
            } else if (destinationsA.get(destinationsA.size() - 1).equals(edge.getVertexB()) && destinationsB.get(0).equals(edge.getVertexA())){
                destinations = destinationsA;
                destinations.addAll(destinationsB);
            } else if (destinationsA.get(destinationsA.size() -1).equals(edge.getVertexB()) && destinationsB.get(destinationsB.size() -1).equals(edge.getVertexA())){
                destinations = destinationsA;
                Collections.reverse(destinationsB);
                destinations.addAll(destinationsB);
            } else {
                System.out.println("shit");
            }
        }

        for(Destination destination : destinations){
            destination.setEdgeSegment(this);
        }
        this.endVertexA = destinations.get(0);
        this.endVertexB = destinations.get(destinations.size() - 1);
        System.out.println("shit");

    }

    public EdgeSegment(Edge edge) {
        destinations = new ArrayList<>();
        destinations.add(edge.getVertexA());
        destinations.add(edge.getVertexB());
        edge.getVertexA().setEdgeSegment(this);
        edge.getVertexB().setEdgeSegment(this);
        this.endVertexA = destinations.get(0);
        this.endVertexB = destinations.get(destinations.size() - 1);
        System.out.println("shit");

    }

    public EdgeSegment(ArrayList<Destination> shortestBorder, EdgeSegment addedSegment) {
        this.destinations = shortestBorder;
        this.endVertexA = destinations.get(0);
        this.endVertexB = destinations.get(destinations.size() - 1);
        this.addedSegment = addedSegment;
        System.out.println("shit");

    }

    public void add(Edge edge){
        if (endVertexB.equals(edge.getVertexA())){
            Algorithm.sortedLatitude.remove(destinations.get(destinations.size() - 1));
            destinations.add(edge.getVertexB());
            edge.getVertexB().setEdgeSegment(this);
        } else if (endVertexB.equals(edge.getVertexB())){
            Algorithm.sortedLatitude.remove(destinations.get(destinations.size() - 1));
            destinations.add(edge.getVertexA());
            edge.getVertexA().setEdgeSegment(this);
        } else if (endVertexA.equals(edge.getVertexA())){
            Algorithm.sortedLatitude.remove(destinations.get(0));
            destinations.add(0, edge.getVertexB());
            edge.getVertexB().setEdgeSegment(this);
        } else if (endVertexA.equals(edge.getVertexB())){
            Algorithm.sortedLatitude.remove(destinations.get(0));
            destinations.add(0, edge.getVertexA());
            edge.getVertexA().setEdgeSegment(this);
        } else {
            System.out.println("here");
        }
        this.endVertexA = destinations.get(0);
        this.endVertexB = destinations.get(destinations.size() - 1);
        System.out.println("shit");

    }

    public Destination getEndVertexA() {
        return endVertexA;
    }

    public void setEndVertexA(Destination endVertexA) {
        this.endVertexA = endVertexA;
    }

    public Destination getEndVertexB() {
        return endVertexB;
    }

    public void setEndVertexB(Destination endVertexB) {
        this.endVertexB = endVertexB;
    }

    public ArrayList<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(ArrayList<Destination> destinations) {
        this.destinations = destinations;
    }

    public Double getHypotheticalNextEdgeWeight() {
        return hypotheticalNextEdgeWeight;
    }

    public void setHypotheticalNextEdgeWeight(Double hypotheticalNextEdgeWeight) {
        this.hypotheticalNextEdgeWeight = hypotheticalNextEdgeWeight;
    }

    public EdgeSegment getAddedSegment() {
        return addedSegment;
    }

    public void setAddedSegment(EdgeSegment addedSegment) {
        this.addedSegment = addedSegment;
    }
}
