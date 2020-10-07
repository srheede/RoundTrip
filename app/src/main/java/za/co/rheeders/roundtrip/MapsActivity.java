package za.co.rheeders.roundtrip;

import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.RoundCap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PATTERN_GAP_LENGTH_PX = 15;
    private static final PatternItem DOT = new Dash(30);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);
    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;
    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    // Create a stroke pattern of a gap followed by a dash.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);
    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private static final List<PatternItem> PATTERN_POLYGON_BETA =
            Arrays.asList(DOT, GAP, DASH, GAP);
    public static TextView tvDistance;
    public static Geocoder geocoder;
    public static GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tvDistance = findViewById(R.id.textViewDistance);

        geocoder = new Geocoder(this, Locale.getDefault());
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (!MainActivity.destinations.isEmpty()) {
            String place;

//            PolylineOptions polylineOptions = new PolylineOptions().clickable(false);
//            PolygonOptions polygonOptions = new PolygonOptions().clickable(false);

            Algorithm algorithm = new Algorithm(MainActivity.destinations);
//            for (Destination destination : algorithm.getShortestRoute()) {
//                try {
//                    polylineOptions.add(destination.getLatLong());
//                    polygonOptions.add(destination.getLatLong());
//
//                    if (destination.getPlaceName() == null) {
//                        List<Address> addresses = geocoder.getFromLocation(destination.getLatitude(), destination.getLongitude(), 1);
//                        StringBuilder stringBuilder = new StringBuilder();
//                        String address = addresses.get(0).getAddressLine(0);
//                        String cityName = addresses.get(0).getAddressLine(1);
//                        String stateName = addresses.get(0).getAddressLine(2);
//                        if (address != null) {
//                            stringBuilder.append(address);
//                        }
//                        if (cityName != null) {
//                            stringBuilder.append(cityName);
//                        }
//                        if (stateName != null) {
//                            stringBuilder.append(stateName);
//                        }
//                        if (stringBuilder.length() != 0) {
//                            place = stringBuilder.toString();
//                        } else {
//                            place = destination.getGeoHash();
//                        }
//                    } else {
//                        place = destination.getPlaceName();
//                    }
////                    googleMap.addMarker(new MarkerOptions().position(destination.getLatLong()).title(place));
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            Polygon polygon = googleMap.addPolygon(polygonOptions);
//            polygon.setTag("alpha");
//            stylePolygon(polygon);

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(MainActivity.destinations.get(0).getLatLong()));
//
            String totalDis = "Total distance: " + (int) algorithm.getShortestDistance() + " km";

            MapsActivity.tvDistance.setText(totalDis);

            tvDistance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    startActivity(new Intent(getApplicationContext(), MapsActivityShort.class));
                    Algorithm.addNextDestination();
                }
            });

            googleMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
                @Override
                public void onPolylineClick(Polyline polyline) {

                }
            });
        }
    }

    private double distancePointToLine(Destination point, Destination segmentPointA, Destination segmentPointB) {

        double factor;
        double dotProduct;
        double len_sq;

        double x0 = point.getLongitude();
        double y0 = point.getLatitude();
        double x1 = segmentPointA.getLongitude();
        double y1 = segmentPointA.getLatitude();
        double x2 = segmentPointB.getLongitude();
        double y2 = segmentPointB.getLatitude();

        double A = x0 - x1;
        double B = y0 - y1;
        double C = x2 - x1;
        double D = y2 - y1;

        dotProduct = A * C + B * D;
        len_sq = C * C + D * D;
        factor = -1;

        if (len_sq != 0)
            factor = dotProduct / len_sq;

        double yy;
        double xx;

        if (factor < 0) {
            xx = x1;
            yy = y1;
        } else if (factor >= 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + factor * C;
            yy = y1 + factor * D;
        }

        double dx = x0 - xx;
        double dy = y0 - yy;

        return Math.sqrt(dx * dx + dy * dy);
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
        double distance = 0;
        LatLng lastLatLng = null;

        for (Destination destination : destinations) {

            double lat = destination.getLatitude();
            double lng = destination.getLongitude();

            if (lastLatLng != null) {
                distance += distance(lat, lng, lastLatLng.latitude, lastLatLng.longitude);
            }

            lastLatLng = destination.getLatLong();
        }

        return distance;
    }

    private void stylePolygon(Polygon polygon) {
        String type = "";
        // Get the data object stored with the polygon.
        if (polygon.getTag() != null) {
            type = polygon.getTag().toString();
        }

        List<PatternItem> pattern = null;
        int strokeColor = COLOR_BLACK_ARGB;
        int fillColor = COLOR_WHITE_ARGB;

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "alpha":
                // Apply a stroke pattern to render a dashed line, and define colors.
                pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = COLOR_GREEN_ARGB;
                fillColor = COLOR_PURPLE_ARGB;
                break;
            case "beta":
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_BETA;
                strokeColor = COLOR_ORANGE_ARGB;
                fillColor = COLOR_BLUE_ARGB;
                break;
        }

        pattern = PATTERN_POLYGON_ALPHA;
        strokeColor = COLOR_GREEN_ARGB;
        fillColor = COLOR_PURPLE_ARGB;
        polygon.setStrokePattern(pattern);
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon.setStrokeColor(strokeColor);
        polygon.setFillColor(fillColor);
    }

    private void stylePolyline(Polyline polyline) {
        polyline.setStartCap(new RoundCap());
        polyline.setEndCap(new RoundCap());
        polyline.setWidth(10);
        polyline.setColor(0xffdc143c);
        polyline.setJointType(JointType.BEVEL);
        polyline.setPattern(PATTERN_POLYLINE_DOTTED);
    }
}