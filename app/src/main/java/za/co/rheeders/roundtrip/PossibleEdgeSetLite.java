package za.co.rheeders.roundtrip;

import java.util.ArrayList;

public class PossibleEdgeSetLite {
    private ArrayList<EdgeLite> possibleEdgeSet;
    private DestinationLite destinationAsKey;

    public PossibleEdgeSetLite(ArrayList<EdgeLite> possibleEdgeSet, DestinationLite destinationAsKey) {
        this.possibleEdgeSet = possibleEdgeSet;
        this.destinationAsKey = destinationAsKey;
    }

    public ArrayList<EdgeLite> getPossibleEdgeSet() {
        return possibleEdgeSet;
    }

    public void setPossibleEdgeSet(ArrayList<EdgeLite> possibleEdgeSet) {
        this.possibleEdgeSet = possibleEdgeSet;
    }

    public DestinationLite getDestinationAsKey() {
        return destinationAsKey;
    }

    public void setDestinationAsKey(DestinationLite destinationAsKey) {
        this.destinationAsKey = destinationAsKey;
    }
}
