package za.co.rheeders.roundtrip;

import java.util.ArrayList;

public class Rollback {
    ArrayList<Destination> enteredRoute;
    ArrayList<Destination> bubbleShrinkRoute;
    ArrayList<Destination> parameterDiamondRoute;
    ArrayList<Destination> sortedLatitude;

    double bubbleShrinkDistance;
    double parameterDiamondDistance;
    double shortestDistance;
    Circle smallestCircle;

    public Rollback() {
        if (!Algorithm.enteredRoute.isEmpty()) {
            this.enteredRoute = new ArrayList<>(Algorithm.enteredRoute);
        } else {
            this.enteredRoute = new ArrayList<>();
        }
        if (!Algorithm.bubbleShrinkRoute.isEmpty()) {
            this.bubbleShrinkRoute = new ArrayList<>(Algorithm.bubbleShrinkRoute);
        } else {
            this.bubbleShrinkRoute = new ArrayList<>();
        }
        if (!Algorithm.parameterDiamondRoute.isEmpty()) {
            this.parameterDiamondRoute = new ArrayList<>(Algorithm.parameterDiamondRoute);
        } else {
            this.parameterDiamondRoute = new ArrayList<>();
        }
        if (!Algorithm.sortedLatitude.isEmpty()) {
            this.sortedLatitude = new ArrayList<>(Algorithm.sortedLatitude);
        } else {
            this.sortedLatitude = new ArrayList<>();
        }
        this.bubbleShrinkDistance = Algorithm.bubbleShrinkDistance;
        this.parameterDiamondDistance = Algorithm.parameterDiamondDistance;
        this.shortestDistance = Algorithm.shortestDistance;
        this.smallestCircle = new Circle(Algorithm.smallestCircle);
    }
}
