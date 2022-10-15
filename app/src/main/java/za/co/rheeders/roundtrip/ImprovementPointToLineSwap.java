package za.co.rheeders.roundtrip;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.Collections;

public class ImprovementPointToLineSwap {
    private ArrayList<Destination> route;
    private ArrayList<PointToLine> pointToLines = new ArrayList<>();
    private ArrayList<TwoPointsToLine> twoPointsToLines = new ArrayList<>();

    public ImprovementPointToLineSwap(ArrayList<Destination> route) {
        this.route = route;
        runImprovement();
    }

    private void runImprovement() {
        boolean didImprove = false;

//        if (twoPointImprovement()){
//            didImprove = true;
//        }

        if (onePointImprovement()){
            didImprove = true;
        }

//        if (didImprove){
//            runImprovement();
//        }
    }

    private boolean onePointImprovement(){
        ArrayList<Destination> improvedDestinations = new ArrayList<>();
        distancesPointToLine();
        Collections.sort(pointToLines);
        boolean didImprove = false;

        for (PointToLine pointToLine : pointToLines){
            if (!(improvedDestinations.contains(pointToLine.getPoint()) || improvedDestinations.contains(pointToLine.getPointVertexA()) || improvedDestinations.contains(pointToLine.getPointVertexB()) || improvedDestinations.contains(pointToLine.getLineVertexA()) || improvedDestinations.contains(pointToLine.getLineVertexB()))) {
                if (isImprovement(pointToLine)) {

                    route.remove(pointToLine.getPoint());
                    route.add(route.indexOf(pointToLine.getLineVertexB()), pointToLine.getPoint());
                    improvedDestinations.add(pointToLine.getPoint());
                    improvedDestinations.add(pointToLine.getPointVertexA());
                    improvedDestinations.add(pointToLine.getPointVertexB());
                    improvedDestinations.add(pointToLine.getLineVertexA());
                    improvedDestinations.add(pointToLine.getLineVertexB());

//                    didImprove = true;
                }
            }
        }

        if (didImprove){
            onePointImprovement();
            return true;
        }
        return false;
    }

    private boolean twoPointImprovement(){
        ArrayList<Destination> improvedDestinations = new ArrayList<>();
        distancesTwoPointsToLine();
        Collections.sort(twoPointsToLines);
        boolean didImprove = false;

        for (TwoPointsToLine twoPointsToLine : twoPointsToLines){

            if (!(improvedDestinations.contains(twoPointsToLine.getPointA()) || improvedDestinations.contains(twoPointsToLine.getPointB()) || improvedDestinations.contains(twoPointsToLine.getPointVertexA()) || improvedDestinations.contains(twoPointsToLine.getPointVertexB()) || improvedDestinations.contains(twoPointsToLine.getLineVertexA()) || improvedDestinations.contains(twoPointsToLine.getLineVertexB()))) {

                if (isImprovementTwoPoints(twoPointsToLine)) {

                    route.remove(twoPointsToLine.getPointA());
                    route.remove(twoPointsToLine.getPointB());

                    route.add(Math.max(route.indexOf(twoPointsToLine.getLineVertexA()), route.indexOf(twoPointsToLine.getLineVertexB())), twoPointsToLine.getPointB());
                    route.add(Math.max(route.indexOf(twoPointsToLine.getLineVertexA()), route.indexOf(twoPointsToLine.getLineVertexB())), twoPointsToLine.getPointA());

                    improvedDestinations.add(twoPointsToLine.getPointA());
                    improvedDestinations.add(twoPointsToLine.getPointB());
                    improvedDestinations.add(twoPointsToLine.getPointVertexA());
                    improvedDestinations.add(twoPointsToLine.getPointVertexB());
                    improvedDestinations.add(twoPointsToLine.getLineVertexA());
                    improvedDestinations.add(twoPointsToLine.getLineVertexB());

//                    didImprove = true;
                }
            }
        }

        if (didImprove){
            twoPointImprovement();
            return true;
        }
        return false;
    }

    private boolean isImprovement(PointToLine pointToLine) {
        //Previous lineA = A
        //Previous lineB = B
        //Point = P
        //Improved LineA = X
        //Improved LineB = Y

        Double AtoP = distance(pointToLine.getPointVertexA(), pointToLine.getPoint());
        Double PtoB = distance(pointToLine.getPoint(), pointToLine.getPointVertexB());
        Double AtoB = distance(pointToLine.getPointVertexA(), pointToLine.getPointVertexB());

        Double XtoP = distance(pointToLine.getLineVertexA(), pointToLine.getPoint());
        Double PtoY = distance(pointToLine.getPoint(), pointToLine.getLineVertexB());
        Double XtoY = distance(pointToLine.getLineVertexA(), pointToLine.getLineVertexB());


        Double previouslyAffectedDistance = (AtoP + PtoB) - AtoB;
        Double improvementAffectedDistance = (XtoP + PtoY) - XtoY;
        if (improvementAffectedDistance < previouslyAffectedDistance){
            return true;
        }
        return false;
    }

    private boolean isImprovementTwoPoints(TwoPointsToLine twoPointsToLine) {
        //Previous lineA = A
        //Previous lineB = B
        //PointA = M
        //PointB = N
        //Improved LineA = X
        //Improved LineB = Y

        Double AtoM = distance(twoPointsToLine.getPointVertexA(), twoPointsToLine.getPointA());
        Double MtoN = distance(twoPointsToLine.getPointA(), twoPointsToLine.getPointB());
        Double NtoB = distance(twoPointsToLine.getPointB(), twoPointsToLine.getPointVertexB());
        Double AtoB = distance(twoPointsToLine.getPointVertexA(), twoPointsToLine.getPointVertexB());

        Double XtoM = distance(twoPointsToLine.getLineVertexA(), twoPointsToLine.getPointA());
        Double NtoY = distance(twoPointsToLine.getPointB(), twoPointsToLine.getLineVertexB());
        Double XtoY = distance(twoPointsToLine.getLineVertexA(), twoPointsToLine.getLineVertexB());


        Double previouslyAffectedDistance = (AtoM + MtoN + NtoB) - AtoB;
        Double improvementAffectedDistance = (XtoM + MtoN + NtoY) - XtoY;
        if (improvementAffectedDistance < previouslyAffectedDistance){
            return true;
        }
        return false;
    }

    public ArrayList<Destination> getRoute() {
        return route;
    }

    public void setRoute(ArrayList<Destination> route) {
        this.route = route;
    }

    private void distancesPointToLine() {
        pointToLines = new ArrayList<>();
        ArrayList<Destination> points = (ArrayList<Destination>) route.clone();
        Destination lastDestination = null;
        int indexPointVertexA;
        int indexPointVertexB;

        for (Destination destination : route){
            if (lastDestination != null){
                for (Destination point : points) {
                    if (!(destination.equals(point) || lastDestination.equals(point))) {
                        if (!(destination.equals(point) && lastDestination.equals(point))) {
                            if (points.indexOf(point) == 0) {
                                indexPointVertexA = points.size() - 1;
                                indexPointVertexB = 1;
                            } else if (points.indexOf(point) == points.size() - 1) {
                                indexPointVertexA = points.size() - 2;
                                indexPointVertexB = 0;
                            } else {
                                indexPointVertexA = points.indexOf(point) - 1;
                                indexPointVertexB = points.indexOf(point) + 1;
                            }
                            pointToLines.add(new PointToLine(point, lastDestination, destination, points.get(indexPointVertexA), points.get(indexPointVertexB)));
                        }
                    }
                }
            }
            lastDestination = destination;
        }
    }

    private void distancesTwoPointsToLine() {
        twoPointsToLines = new ArrayList<>();
        ArrayList<Destination> points = (ArrayList<Destination>) route.clone();
        Destination lastDestination = null;
        Destination lastPoint = null;
        int indexPointVertexA;
        int indexPointVertexB;

        for (Destination destination : route){
            if (lastDestination != null){
                for (Destination point : points) {
                    if (lastPoint != null) {
                        if (!(destination.equals(point) || lastDestination.equals(point) || destination.equals(lastPoint) || lastDestination.equals(lastPoint))) {
                            if (points.indexOf(lastPoint) == 0) {
                                indexPointVertexA = points.size() - 1;
                                indexPointVertexB = 2;
                            } else if (points.indexOf(point) == points.size() - 1) {
                                indexPointVertexA = points.size() - 3;
                                indexPointVertexB = 0;
                            } else {
                                indexPointVertexA = points.indexOf(lastPoint) - 1;
                                indexPointVertexB = points.indexOf(point) + 1;
                            }
                            twoPointsToLines.add(new TwoPointsToLine(lastPoint, point, lastDestination, destination, points.get(indexPointVertexA), points.get(indexPointVertexB)));
                        }
                    }
                    lastPoint = point;
                }
            }
            lastDestination = destination;
        }
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
}
