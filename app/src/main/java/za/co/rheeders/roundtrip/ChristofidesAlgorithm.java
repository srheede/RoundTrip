package za.co.rheeders.roundtrip;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChristofidesAlgorithm {
    private ArrayList<DestinationLite> destinationsLite = new ArrayList<>();
    private ArrayList<Edge> Edges = new ArrayList<>();
    private ArrayList<EdgeLite> EdgesLite = new ArrayList<>();
    private ArrayList<ArrayList<Destination>> MSTParents = new ArrayList<>();
    private ArrayList<ArrayList<DestinationLite>> MSTParentsLite = new ArrayList<>();
    private ArrayList<Destination> visitedMatchingDestinations = new ArrayList<>();
    private int count = 0;


    public ChristofidesAlgorithm() {
        minimumSpanningTree_LowMemoryUsage();
        addMinimumCostPerfectMatching();
//        createEulerianTour();
        takeShortcuts();
    }

    private void createEulerianTour() {
        for (Destination destination : MainActivity.destinations) {
            destination.edgeDegrees = 0;
            destination.edgeDegrees += Edges.stream().filter(edge -> edge.getVertexA().equals(destination)).count();
            destination.edgeDegrees += Edges.stream().filter(edge -> edge.getVertexB().equals(destination)).count();
            if (destination.edgeDegrees == 1){
                findAffectedEdges(destination);
            }
        }
    }

    private void findAffectedEdges(Destination destination) {
        System.out.println(++count);
        Optional<Edge> optionalEdge = Edges.stream().filter(edge -> edge.getVertexA().equals(destination) || edge.getVertexB().equals(destination)).findFirst();
        ArrayList<Edge> affectedEdges = new ArrayList<>();
        Edge edgeB = optionalEdge.get();
        if(edgeB.getVertexA().equals(destination)) {
            affectedEdges = Edges.stream().filter(edge -> edge.getVertexA().equals(edgeB.getVertexB()) || edge.getVertexB().equals(edgeB.getVertexB())).collect(Collectors
                    .toCollection(ArrayList::new));
        } else {
            affectedEdges = Edges.stream().filter(edge -> edge.getVertexA().equals(edgeB.getVertexA()) || edge.getVertexB().equals(edgeB.getVertexA())).collect(Collectors
                    .toCollection(ArrayList::new));
        }
        affectedEdges.remove(edgeB);

        reconnectAffectedEdges(affectedEdges.get(0), edgeB, affectedEdges.get(1));
    }

    private void reconnectAffectedEdges(Edge edgeA, Edge edgeB, Edge edgeC) {
        Edges.remove(edgeA);
        Edges.remove(edgeC);
        Edge edgeA2 = null;
        Edge edgeC2 = null;

        if (edgeA.getVertexA().equals(edgeB.getVertexA())) {
            edgeA2 = new Edge(edgeA.getVertexB(), edgeB.getVertexB());
        } else if (edgeA.getVertexB().equals(edgeB.getVertexB())) {
            edgeA2 = new Edge(edgeA.getVertexA(), edgeB.getVertexA());
        } else if (edgeA.getVertexA().equals(edgeB.getVertexB())) {
            edgeA2 = new Edge(edgeA.getVertexB(), edgeB.getVertexA());
        } else {
            edgeA2 = new Edge(edgeA.getVertexA(), edgeB.getVertexB());
        }

        if (edgeC.getVertexA().equals(edgeB.getVertexA())) {
            edgeC2 = new Edge(edgeC.getVertexB(), edgeB.getVertexB());
        } else if (edgeC.getVertexB().equals(edgeB.getVertexB())) {
            edgeC2 = new Edge(edgeC.getVertexA(), edgeB.getVertexA());
        } else if (edgeC.getVertexA().equals(edgeB.getVertexB())) {
            edgeC2 = new Edge(edgeC.getVertexB(), edgeB.getVertexA());
        } else {
            edgeC2 = new Edge(edgeC.getVertexA(), edgeB.getVertexB());
        }

        if ((edgeA.getWeight() + edgeC2.getWeight()) < (edgeA2.getWeight() + edgeC.getWeight())){
            Edges.add(edgeA);
            Edges.add(edgeC2);
        } else {
            Edges.add(edgeA2);
            Edges.add(edgeC);
        }
    }

    private void takeShortcuts() {
        for (Destination destination : MainActivity.destinations) {
            destination.edgeDegrees = 0;
            destination.edgeDegrees += Edges.stream().filter(edge -> edge.getVertexA().equals(destination)).count();
            destination.edgeDegrees += Edges.stream().filter(edge -> edge.getVertexB().equals(destination)).count();
            if (destination.edgeDegrees == 4){
                ArrayList<Edge> affectedEdges = Edges.stream().filter(edge -> edge.getVertexA().equals(destination) || edge.getVertexB().equals(destination)).collect(Collectors
                        .toCollection(ArrayList::new));
                findMatchingEdges(affectedEdges, destination);
            }
        }
    }

    private void findMatchingEdges(ArrayList<Edge> affectedEdges, Destination start) {
        ArrayList<Edge> uncheckedEdges = (ArrayList<Edge>) Edges.clone();
        Edge startingEdge = affectedEdges.get(0);
        Destination nextDestination;
        if (startingEdge.getVertexA().equals(start)){
            nextDestination = startingEdge.getVertexB();
        } else {
            nextDestination = startingEdge.getVertexA();
        }
        uncheckedEdges.remove(startingEdge);
        Edge nextEdge = null;
        while (!uncheckedEdges.isEmpty()){
            Destination finalNextDestination = nextDestination;
            Optional<Edge> optionalEdge = uncheckedEdges.stream().filter(edge -> edge.getVertexA().equals(finalNextDestination) || edge.getVertexB().equals(finalNextDestination)).findAny();
            if (optionalEdge.isPresent()){
                nextEdge = optionalEdge.get();
                if (nextEdge.getVertexA().equals(nextDestination)){
                    nextDestination = nextEdge.getVertexB();
                } else {
                    nextDestination = nextEdge.getVertexA();
                }
                uncheckedEdges.remove(nextEdge);
                if (nextDestination.equals(start)){
                    break;
                }
            } else {
                break;
            }
        }

        createShortcut(start, startingEdge, nextEdge, affectedEdges);
    }

    private void createShortcut(Destination center, Edge startingEdge, Edge matchingEdge, ArrayList<Edge> affectedEdges) {
        affectedEdges.remove(startingEdge);
        affectedEdges.remove(matchingEdge);
        ArrayList<Improvement> list = new ArrayList<>();


        list.add(new Improvement(startingEdge, affectedEdges.get(0), center));
        list.add(new Improvement(startingEdge, affectedEdges.get(1), center));
        list.add(new Improvement(matchingEdge, affectedEdges.get(0), center));
        list.add(new Improvement(matchingEdge, affectedEdges.get(1), center));

        Collections.sort(list);

        int i = 0;

        while (list.get(i).getNewEdge().getWeight() == 0.0) {
            i++;
        }
//        int count = 0;
//        while (i < list.size()){
//
//            if (list.get(i).getNewEdge().getWeight() == 0.0) {
//                i++;
//            } else {
//
//                for (Edge edgeB : Edges) {
//                    if (!list.get(i).getNewEdge().equals(edgeB) && isIntersecting(list.get(i).getNewEdge(), edgeB)) {
//                        i++;
//                        break;
//                    }
//                }
//            }
//            if (i == count){
//                break ;
//            }
//            count++;
//        }

//        if (i != list.size()) {
            Edges.remove(list.get(i).getEdgeA());
            Edges.remove(list.get(i).getEdgeB());
            Edges.add(list.get(i).getNewEdge());
//        }
    }

    private void minimumSpanningTree(){
        Edges = createAllPermutationsOfEdgesFromDestinations(MainActivity.destinations);
        sortEdgesByWeight(Edges);
//        Collections.reverse(Edges);
//        removeIntersectingEdges();
//        sortEdgesByWeight(intersectingEdges);
//        readdNonIntersectingEdges();
//        sortEdgesByWeight(Edges);
        createMinimumSpanningTree();
    }

    private void minimumSpanningTree_LowMemoryUsage(){
        for (Destination destination : MainActivity.destinations){
            destinationsLite.add(new DestinationLite(destination.getLatitude(), destination.getLongitude()));
        }
        EdgesLite = createAllPermutationsOfEdgesFromDestinationsLite(destinationsLite);
        sortEdgesByWeightLite(EdgesLite);
//        Collections.reverse(Edges);
//        removeIntersectingEdges();
//        sortEdgesByWeight(intersectingEdges);
//        readdNonIntersectingEdges();
//        sortEdgesByWeight(Edges);
        createMinimumSpanningTreeLite();
    }

    private void addMinimumCostPerfectMatching() {
        ArrayList<Destination> oddDegreeVertices = new ArrayList<>();
        for (Destination destination : MainActivity.destinations) {
            destination.edgeDegrees = 0;
            destination.edgeDegrees += Edges.stream().filter(edge -> edge.getVertexA().equals(destination)).count();
            destination.edgeDegrees += Edges.stream().filter(edge -> edge.getVertexB().equals(destination)).count();
            if (destination.edgeDegrees % 2 != 0) {
                oddDegreeVertices.add(destination);
            }
        }

//        for (Destination destination : oddDegreeVertices) {
//            System.out.println("PLACE NAME: " + destination.getPlaceName());
//            MapsActivity.map.addMarker(new MarkerOptions().position(destination.getLatLong()).title(destination.getPlaceName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_black)));
//        }

        ArrayList<Edge> possibleEdges = createAllPermutationsOfEdgesFromDestinations(oddDegreeVertices);

        ArrayList<ArrayList<Edge>> possibleEdgeSets = new ArrayList<>();

        for (Destination destination : oddDegreeVertices){
            possibleEdgeSets.add(possibleEdges.stream().filter(edge -> edge.getVertexA().equals(destination) || edge.getVertexB().equals(destination)).collect(Collectors
                    .toCollection(ArrayList::new)));
        }

        Map<Double, PossibleEdgeSet> map = new HashMap<>();

        for (ArrayList<Edge> possibleEdgeSet : possibleEdgeSets){

            Double totalWeight = 0.0;

            for (Edge possibleEdge : possibleEdgeSet){
                totalWeight += possibleEdge.getWeight();
            }

            ArrayList<Edge> intersectingEdges = new ArrayList<>();

            for (Edge edgeA : possibleEdgeSet){
                for (Edge edgeB : Edges){
                    if (!edgeA.equals(edgeB) && isIntersecting(edgeA, edgeB)){
                        intersectingEdges.add(edgeA);
                        break;
                    }
                }
            }

            PossibleEdgeSet edgeSet = new PossibleEdgeSet(possibleEdgeSet, getDestinationAsKey(possibleEdgeSet));

//            edgeSet.getPossibleEdgeSet().removeAll(intersectingEdges);

            map.put(totalWeight, edgeSet);
        }

        LinkedHashMap<Destination, ArrayList<Edge>> sortedMap = new LinkedHashMap<>();

        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getValue().getDestinationAsKey(), x.getValue().getPossibleEdgeSet()));

//        Edges = new ArrayList<>();

        recursiveMatching(sortedMap);

//        sortEdgesByWeight(possibleEdges);
//
//        ArrayList<Edge> minimumCostPerfectMatching = new ArrayList<>();
//        for (Edge edge : possibleEdges) {
//            if (oddDegreeVertices.contains(edge.getVertexA()) && oddDegreeVertices.contains(edge.getVertexB())) {
//                minimumCostPerfectMatching.add(edge);
//                oddDegreeVertices.remove(edge.getVertexA());
//                oddDegreeVertices.remove(edge.getVertexB());
//            }
//
//            if (oddDegreeVertices.size() == 0)
//                break;
//        }

//        ArrayList<Edge> existingEdges = new ArrayList<>();
//        for (Edge edge : minimumCostPerfectMatching) {
//            if (Edges.stream().anyMatch(existingEdge -> existingEdge.compareTo(edge) == 0)) {
//                existingEdges.add(ed-+ge);
//            }
//        }
//        minimumCostPerfectMatching.removeAll(existingEdges);

//        Edges = minimumCostPerfectMatching;
//        Edges.addAll(minimumCostPerfectMatching);
    }

    private Destination getDestinationAsKey(ArrayList<Edge> nextDestination) {
        if (nextDestination.get(0).getVertexA().equals(nextDestination.get(1).getVertexA()) || nextDestination.get(0).getVertexA().equals(nextDestination.get(1).getVertexB())){
            return nextDestination.get(0).getVertexA();
        } else {
            return nextDestination.get(0).getVertexB();
        }
    }

    private void recursiveMatching(LinkedHashMap<Destination, ArrayList<Edge>> sortedMap) {
        if (sortedMap.size() >= 2){
        ArrayList<Edge> nextDestination = (ArrayList<Edge>) sortedMap.values().toArray()[0];

        Collections.sort(nextDestination, new Comparator<Edge>() {
            @Override
            public int compare(Edge edge, Edge t1) {
                return Double.compare(edge.getWeight(), t1.getWeight());
            }
        });

        for (int i = 0; i < nextDestination.size(); i++) {
            if (!(visitedMatchingDestinations.contains(nextDestination.get(i).getVertexA()) || visitedMatchingDestinations.contains(nextDestination.get(i).getVertexB()))) {
//                if((nextDestination.get(i).getVertexA().getPlaceName().equals("22") || nextDestination.get(i).getVertexB().getPlaceName().equals("22")) ) {
//                    System.out.println("PLACE NAME: ");
//
//                }
                Edges.add(nextDestination.get(i));

                visitedMatchingDestinations.add(nextDestination.get(i).getVertexA());
                visitedMatchingDestinations.add(nextDestination.get(i).getVertexB());
                sortedMap.remove(nextDestination.get(i).getVertexA());
                sortedMap.remove(nextDestination.get(i).getVertexB());

                break ;
            }
        }

        recursiveMatching(sortedMap);
        }
    }

    private ArrayList<Edge> createAllPermutationsOfEdgesFromDestinations(ArrayList<Destination> destinations) {

        ArrayList<Destination> destinationsSublist = (ArrayList<Destination>) destinations.clone();
        ArrayList<Edge> newEdges = new ArrayList<>();

        for (Destination destinationA : destinations){
            destinationsSublist.remove(destinationA);

            for (Destination destinationB : destinationsSublist) {
                if (!destinationA.equals(destinationB)) {
                    newEdges.add(new Edge(destinationA, destinationB));
                }
            }
        }
        return newEdges;
    }

    private ArrayList<EdgeLite> createAllPermutationsOfEdgesFromDestinationsLite(ArrayList<DestinationLite> destinations) {

        ArrayList<DestinationLite> destinationsSublist = (ArrayList<DestinationLite>) destinations.clone();
        ArrayList<EdgeLite> newEdges = new ArrayList<>();

        for (DestinationLite destinationA : destinations){
            destinationsSublist.remove(destinationA);

            for (DestinationLite destinationB : destinationsSublist) {
                if (!destinationA.equals(destinationB)) {
                    newEdges.add(new EdgeLite(destinationA, destinationB));
                }
            }
        }
        return newEdges;
    }

    private void createMinimumSpanningTree() {
        ArrayList<Edge> MST = new ArrayList<>();
        for (Destination destination : MainActivity.destinations){
            ArrayList<Destination> newSet = new ArrayList<Destination>();
            newSet.add(destination);
            MSTParents.add(newSet);
        }
        for ( Edge edge : Edges){
            if (!find(edge.getVertexA()).equals(find(edge.getVertexB()))){
                MST.add(edge);
                mergeParents(edge.getVertexA(), edge.getVertexB());
            }
        }
        Edges = MST;
    }

    private void createMinimumSpanningTreeLite() {
        ArrayList<EdgeLite> MST = new ArrayList<>();
        for(DestinationLite destination: destinationsLite){
            ArrayList<DestinationLite> newSet = new ArrayList<DestinationLite>();
            newSet.add(destination);
            MSTParentsLite.add(newSet);
        }
        for (EdgeLite edge : EdgesLite){
            if (!findLite(edge.vertexA).equals(findLite(edge.vertexB))){
                MST.add(edge);
                mergeParentsLite(edge.vertexA, edge.vertexB);
            }
        }

        MainActivity.Name = 1;

        for (EdgeLite edge : MST){
            Optional<Destination> destinationA = MainActivity.destinations.stream().filter(destination -> destination.getLatitude().equals(edge.vertexA.latitude) && destination.getLongitude().equals(edge.vertexA.longitude)).findAny();
            Optional<Destination> destinationB = MainActivity.destinations.stream().filter(destination -> destination.getLatitude().equals(edge.vertexB.latitude) && destination.getLongitude().equals(edge.vertexB.longitude)).findAny();

            if (destinationA.isPresent() && destinationB.isPresent()) {
                Edges.add(new Edge(destinationA.get(), destinationB.get()));
            }
        }
    }

    private void mergeParents(Destination vertexA, Destination vertexB) {
        ArrayList<Destination> setA = null;

        for (ArrayList<Destination> parentSet : MSTParents){
            if (parentSet.contains(vertexA)){
                setA = parentSet;
                break;
            }
        }

        for (ArrayList<Destination> parentSet : MSTParents){
            if (parentSet.contains(vertexB)){
                parentSet.addAll(setA);
            }
        }
        MSTParents.remove(setA);
    }

    private void mergeParentsLite(DestinationLite vertexA, DestinationLite vertexB) {
        ArrayList<DestinationLite> setA = null;

        for (ArrayList<DestinationLite> parentSet : MSTParentsLite){
            if (parentSet.contains(vertexA)){
                setA = parentSet;
                break;
            }
        }

        for (ArrayList<DestinationLite> parentSet : MSTParentsLite){
            if (parentSet.contains(vertexB)){
                parentSet.addAll(setA);
            }
        }
        MSTParentsLite.remove(setA);
    }

    private ArrayList<Destination> find(Destination destination) {

        for (ArrayList<Destination> parentSet : MSTParents){
            if (parentSet.contains(destination)){
                return parentSet;
            }
        }

        return null;
    }

    private ArrayList<DestinationLite> findLite(DestinationLite destination) {

        for (ArrayList<DestinationLite> parentSet : MSTParentsLite) {
            if (parentSet.contains(destination)) {
                return parentSet;
            }
        }

        return null;
    }

    public static Double distanceLite(DestinationLite destination1, DestinationLite destination2) {

        Double lat1 = destination1.latitude;
        Double lon1 = destination1.longitude;
        Double lat2 = destination2.latitude;
        Double lon2 = destination2.longitude;

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

//    private void removeIntersectingEdges(ArrayList<Edge> edges) {
//        intersectingEdges = new ArrayList<>();
//        for (Edge edgeA : Edges) {
//            for (Edge edgeB : edges) {
//                if (!edgeA.equals(edgeB) && isIntersecting(edgeA, edgeB)) {
//                    intersectingEdges.add(edgeB);
//                }
//            }
//        }

//        boolean breakPoint = false;
//
//        for (Edge edgeA : Edges){
//            if(breakPoint){
//                break;
//            }
//            for (Edge edgeB : edges)
//            {
//                if((edgeA.getVertexA().getPlaceName().equals("5") || edgeA.getVertexB().getPlaceName().equals("5")) && (edgeA.getVertexA().getPlaceName().equals("11") || edgeA.getVertexB().getPlaceName().equals("11"))){
//                    if((edgeB.getVertexA().getPlaceName().equals("17") || edgeB.getVertexB().getPlaceName().equals("17")) && (edgeB.getVertexA().getPlaceName().equals("19") || edgeB.getVertexB().getPlaceName().equals("19"))) {
//                        System.out.println("PLACE NAME: " + edgeB.getVertexA().getPlaceName());
//                        System.out.println("PLACE NAME: " + edgeB.getVertexB().getPlaceName());
//                    }
//                }
//                if (!(edgeA.equals(edgeB) || edgeA.isRemoved() || edgeB.isRemoved())) {
//                    if (isIntersecting(edgeA, edgeB)) {
//                        if (edgeA.getWeight() > edgeB.getWeight()) {
//                            if((edgeA.getVertexA().getPlaceName().equals("5") || edgeA.getVertexB().getPlaceName().equals("5")) && (edgeA.getVertexA().getPlaceName().equals("11") || edgeA.getVertexB().getPlaceName().equals("11"))){
//                                System.out.println("PLACE NAME: " + edgeB.getVertexA().getPlaceName());
//                                System.out.println("PLACE NAME: " + edgeB.getVertexB().getPlaceName());
//                                breakPoint = true;
//                                break;
//                            }
//                            intersectingEdges.add(edgeB);
//                            edgeB.setRemoved(true);
//                        } else {
//
//                            intersectingEdges.add(edgeB);
//                            edgeB.setRemoved(true);
//                        }
//                    }
//                }
//            }
//        }
//
//        edges.remove(intersectingEdges);
//        return edges;
//    }

//    private void readdNonIntersectingEdges() {
//        for (Edge edgeA : intersectingEdges) {
//            int count = 0;
//            for (Edge edgeB : Edges) {
//                if (isIntersecting(edgeA, edgeB)) {
//                    count++;
//                }
//            }
//            if (count == 0){
//                Edges.add(edgeA);
//            }
//        }
//    }

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

        if (((AA <= I && I <= AB) || (AB <= I && I <= AA)) && ((BA <= I && I <= BB) || (BB <= I && I <= BA))){
            return true;
        }

        return false;
    }

    private Destination getIntersectingVertex(Edge edgeA, Edge edgeB) {
        if (edgeA.getGradient().equals(edgeB.getGradient())){
            return null;
        }
        Double longitude;
        Double latitude;
        if (edgeA.getGradient().isInfinite()){
            longitude = edgeA.getVertexA().getLongitude();
            latitude = (edgeB.getGradient() * longitude) + edgeB.getIntercept();
            if ((edgeA.getVertexA().getLatitude() < latitude && latitude < edgeA.getVertexB().getLatitude()) || (edgeA.getVertexB().getLatitude() < latitude && latitude < edgeA.getVertexA().getLatitude())){
                return new Destination(latitude, longitude);
            } else {
                return null;
            }
        } else if (edgeB.getGradient().isInfinite()){
            longitude = edgeB.getVertexA().getLongitude();
            latitude = (edgeA.getGradient() * longitude) + edgeA.getIntercept();
            if ((edgeB.getVertexA().getLatitude() < latitude && latitude < edgeB.getVertexB().getLatitude()) || (edgeB.getVertexB().getLatitude() < latitude && latitude < edgeB.getVertexA().getLatitude())){
                return new Destination(latitude, longitude);
            } else {
                return null;
            }
        } else {
            longitude = edgeB.getIntercept() / (edgeA.getGradient() - edgeB.getGradient()) - edgeA.getIntercept() / (edgeA.getGradient() - edgeB.getGradient());
            latitude = (edgeA.getGradient() * longitude) + edgeA.getIntercept();
        }
        return new Destination(latitude, longitude);
    }

    private void sortEdgesByWeight(ArrayList<Edge> List) {
        Collections.sort(List, new Comparator<Edge>() {
            @Override
            public int compare(Edge edge, Edge t1) {
                return Double.compare(edge.getWeight(), t1.getWeight());
            }
        });
    }

    private void sortEdgesByWeightLite(ArrayList<EdgeLite> List) {
        Collections.sort(List, new Comparator<EdgeLite>() {
            @Override
            public int compare(EdgeLite edge, EdgeLite t1) {
                return Double.compare(edge.weight, t1.weight);
            }
        });
    }

    public ArrayList<Edge> getEdges() {
        return Edges;
    }
}