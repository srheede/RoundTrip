package za.co.rheeders.roundtrip;

public class DestinationLite implements Comparable {
    public Double latitude;
    public Double longitude;

    public DestinationLite(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public int compareTo(Object other) {
        DestinationLite destination = (DestinationLite) other;
        return Double.compare(this.latitude, destination.latitude);
    }
}
