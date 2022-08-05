package za.co.rheeders.roundtrip;

import java.util.ArrayList;

public class PossibleEdgeSet {
    private ArrayList<Edge> possibleEdgeSet;
    private Destination destinationAsKey;

    public PossibleEdgeSet(ArrayList<Edge> possibleEdgeSet, Destination destinationAsKey) {
        this.possibleEdgeSet = possibleEdgeSet;
        this.destinationAsKey = destinationAsKey;
    }

    public ArrayList<Edge> getPossibleEdgeSet() {
        return possibleEdgeSet;
    }

    public void setPossibleEdgeSet(ArrayList<Edge> possibleEdgeSet) {
        this.possibleEdgeSet = possibleEdgeSet;
    }

    public Destination getDestinationAsKey() {
        return destinationAsKey;
    }

    public void setDestinationAsKey(Destination destinationAsKey) {
        this.destinationAsKey = destinationAsKey;
    }
}
