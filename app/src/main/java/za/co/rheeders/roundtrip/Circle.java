package za.co.rheeders.roundtrip;

public class Circle {
    Destination centerCoordinates;
    Double radius;

    public Circle(Double latitude, Double longitude, Double radius) {
        this.centerCoordinates = new Destination(latitude, longitude);
        this.radius = radius;
    }

    public Circle(Destination destination, Double radius) {
        this.centerCoordinates = destination;
        this.radius = radius;
    }

    public Circle(Circle circle){
        this.centerCoordinates = circle.centerCoordinates;
        this.radius = circle.radius;
    }

    public Destination getDestination() {
        return centerCoordinates;
    }

    public void setDestination(Destination destination) {
        this.centerCoordinates = destination;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }
}
