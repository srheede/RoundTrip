package za.co.rheeders.roundtrip;

public class Circle {
    Destination centerCoordinates;
    double radius;

    public Circle(double latitude, double longitude, double radius) {
        this.centerCoordinates = new Destination(latitude, longitude);
        this.radius = radius;
    }

    public Circle(Destination destination, double radius) {
        this.centerCoordinates = destination;
        this.radius = radius;
    }

    public Destination getDestination() {
        return centerCoordinates;
    }

    public void setDestination(Destination destination) {
        this.centerCoordinates = destination;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
