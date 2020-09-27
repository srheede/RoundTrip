package za.co.rheeders.roundtrip;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

public class MapsActivityShort extends FragmentActivity implements OnMapReadyCallback {

    private static final int STORAGE_PERMISSION_CODE = 101;
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
    private ArrayList<Destination> route = new ArrayList<>();
    private double routeDistance;
    private double totalDistance;
    private HashMap<Double, ArrayList<Destination>> possibleRoutes = new HashMap<>();
    private int number = 0;
    private TextView tvDistanceShort;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_short);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapShort);
        mapFragment.getMapAsync(this);

        tvDistanceShort = findViewById(R.id.textViewDistanceShort);

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

        int size = MainActivity.destinations.size();

        if (MainActivity.destinationsShort.isEmpty()) {
            algorithmBrutForce(size);
            size = factorial(size);
            if (number == size) {
                Toast.makeText(MapsActivityShort.this, "Brute Force Search Completed.",
                        Toast.LENGTH_LONG).show();
                setMap(googleMap);
                saveFile();
            } else {
                Toast.makeText(MapsActivityShort.this, "Only " + number + " of " + size + " routes searched.",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            ArrayList<Destination> fullRoute = new ArrayList<>(MainActivity.destinationsShort);
            fullRoute.add(MainActivity.destinations.get(0));
            totalDistance = totalDistance(fullRoute);
            setMap(googleMap);
        }
    }

    private void saveFile() {
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        try {
            String path;
            if (MainActivity.filePath.isEmpty()) {
                path = "ManualCoordinates" + MainActivity.destinations.size();
            } else {
                path = MainActivity.filePath;
            }
            FileWriter fileWriter = new FileWriter(path.substring(0, path.lastIndexOf('.')) + " BruteForceSolution.txt");
            int n = 1;
            String string;
            for (Destination destination : MainActivity.destinationsShort) {
                string = n++ + ". " + destination.getLatitude() + ", " + destination.getLongitude() + " ";
                if (!destination.getPlaceName().isEmpty()) {
                    string += destination.getPlaceName();
                }
                fileWriter.write(string + "\n");
            }
            fileWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MapsActivityShort.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MapsActivityShort.this, new String[]{permission}, requestCode);
        }
    }

    private void setMap(GoogleMap googleMap) {
        String place;

        PolylineOptions polylineOptions = new PolylineOptions().clickable(false);
        PolygonOptions polygonOptions = new PolygonOptions().clickable(false);

        for (Destination destination : MainActivity.destinationsShort) {
            try {
                polylineOptions.add(destination.getLatLong());
                polygonOptions.add(destination.getLatLong());

                if (destination.getPlaceName() == null) {
                    List<Address> addresses = geocoder.getFromLocation(destination.getLatitude(), destination.getLongitude(), 1);
                    StringBuilder stringBuilder = new StringBuilder();
                    String address = addresses.get(0).getAddressLine(0);
                    String cityName = addresses.get(0).getAddressLine(1);
                    String stateName = addresses.get(0).getAddressLine(2);
                    if (address != null) {
                        stringBuilder.append(address);
                    }
                    if (cityName != null) {
                        stringBuilder.append(cityName);
                    }
                    if (stateName != null) {
                        stringBuilder.append(stateName);
                    }
                    if (stringBuilder.length() != 0) {
                        place = stringBuilder.toString();
                    } else {
                        place = destination.getGeoHash();
                    }
                } else {
                    place = destination.getPlaceName();
                }
                googleMap.addMarker(new MarkerOptions().position(destination.getLatLong()).title(place));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Polyline polyline = googleMap.addPolyline(polylineOptions);
        stylePolyline(polyline);


        Polygon polygon = googleMap.addPolygon(polygonOptions);
        polygon.setTag("alpha");
        stylePolygon(polygon);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(MainActivity.destinationsShort.get(0).getLatLong()));
        String totalDis = "Total distance: " + (int) totalDistance + " km";
        tvDistanceShort.setText(totalDis);

        googleMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {

            }
        });

        tvDistanceShort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });
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

    private void algorithmBrutForce(int n) {
        if (n > 13) {
            Toast.makeText(MapsActivityShort.this, "Too many destinations to brute force solution.",
                    Toast.LENGTH_LONG).show();
            tvDistanceShort.setText(R.string.route_not_aval);

        } else {
            Toast.makeText(MapsActivityShort.this, "Brute forcing the shortest route.",
                    Toast.LENGTH_LONG).show();
            MainActivity.destinationsShort = new ArrayList<>(MainActivity.destinations);
            ArrayList<Destination> fullRoute = new ArrayList<>(MainActivity.destinations);
            fullRoute.add(MainActivity.destinations.get(0));
            totalDistance = totalDistance(fullRoute);
            shortestPermutation(MainActivity.destinations, 0);
        }
    }

    private void shortestPermutation(ArrayList<Destination> destinations, int n) {
        if (n >= destinations.size()) {
            System.out.println("Count: " + ++number);
            System.out.println("Next destinations: ");
            for (Destination destination : destinations) {
                System.out.println(destination.getPlaceName());
            }
            ArrayList<Destination> fullRoute = new ArrayList<>(destinations);
            fullRoute.add(destinations.get(0));
            if (totalDistance(fullRoute) < totalDistance) {
                MainActivity.destinationsShort = new ArrayList<>(destinations);
                totalDistance = totalDistance(fullRoute);
                System.out.println("Distance: " + (int) totalDistance);
            }
        } else {
            for (int i = n; i < destinations.size(); i++) {
                Collections.swap(destinations, n, i);
                shortestPermutation(destinations, n + 1);
                Collections.swap(destinations, n, i);
            }
        }
    }

    // method to find factorial of given number
    int factorial(int n) {
        if (n == 0) {
            return 1;
        } else if (n > 13) {
            return 0;
        }

        return n * factorial(n - 1);
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