package za.co.rheeders.roundtrip;

import com.google.android.gms.maps.model.LatLng;

import static za.co.rheeders.roundtrip.GeoHash.decodeHash;
import static za.co.rheeders.roundtrip.GeoHash.encodeHash;

public class Destination {
    private double latitude;
    private double longitude;
    private String geoHash;
    private String placeId;
    private String placeName;
    private LatLng latLong;

    public Destination(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.geoHash = encodeHash(latitude, longitude, 12);
        this.latLong = new LatLng(latitude, longitude);
        this.placeName = Integer.toString(MainActivity.Name++);
    }

    public Destination(String geoHash) {
        LatLong latLong = decodeHash(geoHash);
        this.geoHash = geoHash;
        this.latitude = latLong.getLat();
        this.longitude = latLong.getLon();
        this.latLong = new LatLng(latitude, longitude);
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }
}
