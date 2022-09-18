package za.co.rheeders.roundtrip;

import static za.co.rheeders.roundtrip.GeoHash.decodeHash;
import static za.co.rheeders.roundtrip.GeoHash.encodeHash;

import com.google.android.gms.maps.model.LatLng;

public class Destination implements Comparable {
    private Double latitude;
    private Double longitude;
    private Double distanceCenter;
    private String geoHash;
    private String placeId;
    private String placeName;
    private LatLng latLong;
    public int edgeDegrees = 0;

    public Destination(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.geoHash = encodeHash(latitude, longitude, 12);
        this.latLong = new LatLng(latitude, longitude);
        this.placeName = Integer.toString(MainActivity.Name++);
    }

    public Destination(Double latitude, Double longitude, String placeName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.geoHash = encodeHash(latitude, longitude, 12);
        this.latLong = new LatLng(latitude, longitude);
        this.placeName = placeName;
    }

    public Destination(String geoHash) {
        LatLong latLong = decodeHash(geoHash);
        this.geoHash = geoHash;
        this.latitude = latLong.getLat();
        this.longitude = latLong.getLon();
        this.latLong = new LatLng(latitude, longitude);
    }

    @Override
    public int compareTo(Object other) {
        Destination destination = (Destination) other;
        return Double.compare(this.latitude, destination.latitude);
    }

    public Double getDistanceCenter() {
        return distanceCenter;
    }

    public void setDistanceCenter(Double distanceCenter) {
        this.distanceCenter = distanceCenter;
    }

    public LatLng getLatLong() {
        return latLong;
    }

    public void setLatLong(LatLng latLong) {
        this.latLong = latLong;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }
}
