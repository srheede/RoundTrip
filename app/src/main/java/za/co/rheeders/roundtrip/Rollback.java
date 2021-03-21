package za.co.rheeders.roundtrip;

import java.util.ArrayList;

public class Rollback {
    ArrayList<Destination> enteredRoute;
    ArrayList<Destination> shortestRoute;
    ArrayList<Destination> bubbleShrinkRoute;
    ArrayList<Destination> parameterDiamondRoute;
    ArrayList<Destination> sortedLatitude;

    double bubbleShrinkDistance;
    double parameterDiamondDistance;
    double shortestDistance;
    Circle smallestCircle;

    public Rollback(){
        this.enteredRoute = new ArrayList<>(Algorithm.enteredRoute);
        this.shortestRoute = new ArrayList<>(Algorithm.shortestRoute);
        this.bubbleShrinkRoute = new ArrayList<>(Algorithm.bubbleShrinkRoute);
        this.parameterDiamondRoute = new ArrayList<>(Algorithm.parameterDiamondRoute);
        this.sortedLatitude = new ArrayList<>(Algorithm.sortedLatitude);
        this.bubbleShrinkDistance = Algorithm.bubbleShrinkDistance;
        this.parameterDiamondDistance = Algorithm.parameterDiamondDistance;
        this.shortestDistance = Algorithm.shortestDistance;
        this.smallestCircle = new Circle(Algorithm.smallestCircle);
    }
}
