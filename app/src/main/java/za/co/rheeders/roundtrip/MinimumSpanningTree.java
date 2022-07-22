package za.co.rheeders.roundtrip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MinimumSpanningTree {
    private ArrayList<Edge> Edges = new ArrayList<>();

    public MinimumSpanningTree(ArrayList<Destination> destinations) {
        createAllPermutationsOfEdgesFromDestinations(destinations);
        sortEdgesByWeight();
        removeIntersectingEdges();
//        Edges = createMinimumSpanningTree();
    }

    public ArrayList<Edge> getEdges() {
        return Edges;
    }

    private void createAllPermutationsOfEdgesFromDestinations(ArrayList<Destination> destinations) {
        ArrayList<Destination> destinationsSublist = (ArrayList<Destination>) destinations.clone();

        for (Destination destinationA : destinations){
            destinationsSublist.remove(destinationA);

            for (Destination destinationB : destinationsSublist) {
                if (!destinationA.equals(destinationB)) {
                    Edges.add(new Edge(destinationA, destinationB));
                }
            }
        }
    }

    private void removeIntersectingEdges() {
        ArrayList<Edge> removeIntersectingEdges = new ArrayList<>();
        boolean breakPoint = false;

        for (Edge edgeA : Edges){
            if(breakPoint){
                break;
            }
            for (Edge edgeB : Edges)
            {
                if (!(edgeA.equals(edgeB) || edgeA.isRemoved() || edgeB.isRemoved())) {
                    if (isIntersecting(edgeA, edgeB)) {
                        if (edgeA.getWeight() > edgeB.getWeight()) {
//                            if((edgeA.getVertexA().getPlaceName().equals("4") || edgeA.getVertexB().getPlaceName().equals("4")) && (edgeA.getVertexA().getPlaceName().equals("20") || edgeA.getVertexB().getPlaceName().equals("20"))){
//                                System.out.println("PLACE NAME: " + edgeB.getVertexA().getPlaceName());
//                                System.out.println("PLACE NAME: " + edgeB.getVertexB().getPlaceName());
//                                breakPoint = true;
//                                break;
//                            }
                            removeIntersectingEdges.add(edgeA);
                            edgeA.setRemoved(true);
                        } else {
//                            if((edgeB.getVertexA().getPlaceName().equals("4") || edgeB.getVertexB().getPlaceName().equals("4")) && (edgeB.getVertexA().getPlaceName().equals("20") || edgeB.getVertexB().getPlaceName().equals("20"))){
//                                System.out.println("PLACE NAME: " + edgeA.getVertexA().getPlaceName());
//                                System.out.println("PLACE NAME: " + edgeA.getVertexB().getPlaceName());
//                                breakPoint = true;
//                                break;
//                            }
                            removeIntersectingEdges.add(edgeB);
                            edgeB.setRemoved(true);
                        }
                    }
                } else {
                    System.out.println("yes");
                }
            }
        }

        Edges.removeAll(removeIntersectingEdges);
    }

    private boolean isIntersecting(Edge edgeA, Edge edgeB) {
        if (edgeA.getVertexA().equals(edgeB.getVertexA()) || edgeA.getVertexA().equals(edgeB.getVertexB()) || edgeA.getVertexB().equals(edgeB.getVertexA()) || edgeA.getVertexB().equals(edgeB.getVertexB())){
            return false;
        }
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

    private void sortEdgesByWeight() {
        Collections.sort(Edges, new Comparator<Edge>() {
            @Override
            public int compare(Edge edge, Edge t1) {
                return Double.compare(t1.getWeight(), edge.getWeight());
            }
        });
    }

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