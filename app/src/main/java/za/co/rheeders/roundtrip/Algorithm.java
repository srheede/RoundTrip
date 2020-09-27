package za.co.rheeders.roundtrip;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Algorithm {

    private ArrayList<Destination> enteredRoute;
    private ArrayList<Destination> shortestRoute;
    private ArrayList<Destination> bubbleShrinkRoute;
    private ArrayList<Destination> convexHullDiamondRoute;
    private double bubbleShrinkDistance;
    private double convexHullDiamondDistance;
    private double shortestDistance;

    public Algorithm(ArrayList<Destination> destinations) {
        this.enteredRoute = destinations;
        if (destinations.size() < 4) {
            this.shortestRoute = new ArrayList<>(destinations);
            this.shortestDistance = totalDistance(destinations);
        } else {
            shortestRoute();
        }
    }

    public ArrayList<Destination> getEnteredRoute() {
        return enteredRoute;
    }

    public ArrayList<Destination> getShortestRoute() {
        return shortestRoute;
    }

    public double getShortestDistance() {
        return shortestDistance;
    }

    private void shortestRoute() {
        bubbleShrink();
        convexHullDiamond();
        if (bubbleShrinkDistance < convexHullDiamondDistance) {
            shortestRoute = bubbleShrinkRoute;
            shortestDistance = bubbleShrinkDistance;
        } else {
            shortestRoute = convexHullDiamondRoute;
            shortestDistance = convexHullDiamondDistance;
        }
    }

    private void convexHullDiamond() {
        convexHullDiamondRoute = new ArrayList<>(enteredRoute);
        convexHullDiamondDistance = totalDistance(convexHullDiamondRoute);
    }

    private void bubbleShrink() {
        bubbleShrinkRoute = new ArrayList<>(enteredRoute);
        bubbleShrinkDistance = totalDistance(bubbleShrinkRoute);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return (c * r);
    }

    private double totalDistance(ArrayList<Destination> destinations) {
        ArrayList<Destination> fullRoute = new ArrayList<>(destinations);
        fullRoute.add(destinations.get(0));
        double distance = 0;
        LatLng lastLatLng = null;

        for (Destination destination : fullRoute) {

            double lat = destination.getLatitude();
            double lng = destination.getLongitude();

            if (lastLatLng != null) {
                distance += distance(lat, lng, lastLatLng.latitude, lastLatLng.longitude);
            }

            lastLatLng = destination.getLatLong();
        }

        return distance;
    }
}
