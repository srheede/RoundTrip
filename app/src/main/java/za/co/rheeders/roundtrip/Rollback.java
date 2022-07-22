package za.co.rheeders.roundtrip;

import java.util.ArrayList;

public class Rollback {
    ArrayList<Destination> enteredRoute;
    ArrayList<Destination> bubbleShrinkRoute;
    ArrayList<Destination> parameterDiamondRoute;
    ArrayList<Destination> sortedLatitude;

    Double bubbleShrinkDistance;
    Double parameterDiamondDistance;
    Double shortestDistance;
    Circle smallestCircle;

    public Rollback() {
        if (MainActivity.switchAlgorithm == 0){
            this.bubbleShrinkRoute = new ArrayList<>(Algorithm.bubbleShrinkRoute);
            this.bubbleShrinkDistance = Algorithm.bubbleShrinkDistance;
            this.smallestCircle = new Circle(Algorithm.smallestCircle);
        }
        this.enteredRoute = new ArrayList<>(Algorithm.enteredRoute);
        this.parameterDiamondRoute = new ArrayList<>(Algorithm.parameterDiamondRoute);
        this.sortedLatitude = new ArrayList<>(Algorithm.sortedLatitude);
        this.parameterDiamondDistance = Algorithm.parameterDiamondDistance;
        this.shortestDistance = Algorithm.shortestDistance;
    }
}
