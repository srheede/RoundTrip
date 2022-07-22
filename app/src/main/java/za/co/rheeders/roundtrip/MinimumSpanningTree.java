package za.co.rheeders.roundtrip;

import java.util.ArrayList;

public class MinimumSpanningTree {
    private ArrayList<Edge> Edges;

    public MinimumSpanningTree(ArrayList<Destination> destinations) {
        Edges = createAllPermutationsOfEdgesFromDestinations(destinations);
        Edges = removeIntersectingEdges();
//        sortEdgesByWeight();
//        Edges = createMinimumSpanningTree();
    }

    public ArrayList<Edge> getEdges() {
        return Edges;
    }

    private ArrayList<Edge> createAllPermutationsOfEdgesFromDestinations(ArrayList<Destination> destinations) {
        ArrayList<Edge> Edges = new ArrayList<>();
        ArrayList<Destination> destinationsSublist = (ArrayList<Destination>) destinations.clone();

        for (Destination destinationA : destinations){
            destinationsSublist.remove(destinationA);

            for (Destination destinationB : destinationsSublist) {
                Edges.add(new Edge(destinationA, destinationB));
            }
        }

        return Edges;
    }

    private ArrayList<Edge> removeIntersectingEdges() {
        ArrayList<Edge> intersectingEdgesRemoved = (ArrayList<Edge>) Edges.clone();
        ArrayList<Edge> newEdges = new ArrayList<>();

        for (Edge edgeA : Edges){
            for (Edge edgeB : Edges)
            {
                if (!edgeA.equals(edgeB)) {
                    if (isIntersecting(edgeA, edgeB)) {
                        if (edgeA.getWeight() > edgeB.getWeight()) {
                            intersectingEdgesRemoved.remove(edgeA);
                        } else {
                            intersectingEdgesRemoved.remove(edgeB);
                        }
                    }
                }
            }
        }

        return intersectingEdgesRemoved;
    }

    private boolean isIntersecting(Edge edgeA, Edge edgeB) {
        Destination intersectingVertex = getIntersectingVertex(edgeA, edgeB);
        if (intersectingVertex == null){
            return false;
        }
        Double I = intersectingVertex.getLongitude();
        Double AA = edgeA.getVertexA().getLongitude();
        Double AB = edgeA.getVertexB().getLongitude();
        Double BA = edgeB.getVertexA().getLongitude();
        Double BB = edgeB.getVertexB().getLongitude();

        if (AA.equals(I) || AB.equals(I) || BA.equals(I) || BB.equals(I)){
            return false;
        }

        if (((AA < I && I < AB) || (AB < I && I < AA)) && ((BA < I && I < BB) || (BB < I && I < BA))){
            return true;
        }

        return false;
    }

    private Destination getIntersectingVertex(Edge edgeA, Edge edgeB) {
        if (edgeA.getGradient().equals(edgeB.getGradient())){
            return null;
        }

        Double longitude = edgeB.getIntercept() / (edgeA.getGradient() - edgeB.getGradient()) - edgeA.getIntercept() / (edgeA.getGradient() - edgeB.getGradient());
        Double latitude = (edgeA.getGradient() * longitude) + edgeA.getIntercept();
        return new Destination(latitude, longitude);
    }

//    private void sortEdgesByWeight() {
//        Collections.sort(Edges, new Comparator<Edge>() {
//            @Override
//            public int compare(Edge edge, Edge t1) {
//                return Double.compare(edge.getWeight(), t1.getWeight());
//            }
//        });
//    }

//    private ArrayList<Edge> createMinimumSpanningTree() {
//        ArrayList<Destination> destinations = (ArrayList<Destination>) MainActivity.destinations.clone();
//        ArrayList<Destination> visitedDestinations = new ArrayList<>();
//        ArrayList<Edge> minimumSpanningTree = new ArrayList<>();
//        while(destinations.size() > 1){
//            for(Edge edge : Edges){
//                if (destinations.contains(edge.getVertexA()) && destinations.contains(edge.getVertexB())) {
//                    if (visitedDestinations.contains(edge.getVertexA()) && visitedDestinations.contains(edge.getVertexB())) {
//                        if (!isConnected(minimumSpanningTree, edge, destinations)) {
//                            minimumSpanningTree.add(edge);
//                            if (visitedDestinations.contains(edge.getVertexA())) {
//                                destinations.remove(edge.getVertexA());
//                            } else {
//                                visitedDestinations.add(edge.getVertexA());
//
//                            }
//                            if (visitedDestinations.contains(edge.getVertexB())) {
//                                destinations.remove(edge.getVertexB());
//                            } else {
//                                visitedDestinations.add(edge.getVertexB());
//                            }
//                        } else {
//                            System.out.println("CONTAINS BOTH: " + edge.getVertexA().getLatLong() + " && " + edge.getVertexB().getLatLong());
//                        }
//                    } else {
//                        minimumSpanningTree.add(edge);
//                        if (visitedDestinations.contains(edge.getVertexA())) {
//                            destinations.remove(edge.getVertexA());
//                        } else {
//                            visitedDestinations.add(edge.getVertexA());
//
//                        }
//                        if (visitedDestinations.contains(edge.getVertexB())) {
//                            destinations.remove(edge.getVertexB());
//                        } else {
//                            visitedDestinations.add(edge.getVertexB());
//                        }
//                    }
//                }
//            }
//            System.out.println("SIZE: " + destinations.size());
//        }
//        return minimumSpanningTree;
//    }
//
//    private boolean isConnected(ArrayList<Edge> minimumSpanningTree, Edge edge, ArrayList<Destination> destinations) {
//
//        return true;
//    }
}