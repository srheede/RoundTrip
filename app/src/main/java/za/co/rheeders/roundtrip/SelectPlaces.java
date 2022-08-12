package za.co.rheeders.roundtrip;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static za.co.rheeders.roundtrip.GeoHash.encodeHash;

public class SelectPlaces extends AppCompatActivity {

    private String placeId;
    private LatLng latLng;
    private String geoHash;
    private TextView textViewAdded;
    private Place place;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_places);
        MainActivity.hasSolution = 0;
        textViewAdded = findViewById(R.id.textViewAdded);
        Button buttonAddDes = findViewById(R.id.buttonAddDes);
        Button buttonCalcRouteParameterDiamond = findViewById(R.id.buttonCalcRouteParameterDiamond);
        Button buttonCalcRouteBubbleShrink = findViewById(R.id.buttonCalcRouteShrinkCycle);
        Button buttonCalcRouteCombo = findViewById(R.id.buttonCalcRouteCombo);

        buttonCalcRouteBubbleShrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                saveFile();
                MainActivity.switchAlgorithm = 0;
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });

        buttonCalcRouteParameterDiamond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                saveFile();
                MainActivity.switchAlgorithm = 1;
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });

        buttonCalcRouteCombo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFile();
                MainActivity.switchAlgorithm = 2;
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });

        // Initialize the SDK
        Places.initialize(getApplicationContext(), getString(R.string.API_KEY));

        // Create a new Places client instance
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        if (autocompleteFragment != null) {
            // Specify the types of place data to return.
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

            // Set up a PlaceSelectionListener to handle the response.
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place placeSelected) {
                    place = placeSelected;
                }

                @Override
                public void onError(@NonNull Status status) {
                    System.out.println(status);
                }
            });
        }

        buttonAddDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (place != null) {
                    latLng = place.getLatLng();
                    if (latLng != null) {
                        geoHash = encodeHash(latLng.latitude, latLng.longitude, 10);
                    }
                    Destination destination = new Destination(geoHash);
                    destination.setPlaceId(place.getId());
                    destination.setPlaceName(place.getName());
                    MainActivity.destinations.add(destination);
                    String string = getString(R.string.place_added, place.getName());
                    textViewAdded.setText(string);
                } else {
                    textViewAdded.setText(R.string.select_dest);
                }
            }
        });
    }

    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(SelectPlaces.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(SelectPlaces.this, new String[]{permission}, requestCode);
        }
    }

    private void saveFile() {
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        try {
            String path = "/storage/emulated/0/Download/PlacesCoordinates" + MainActivity.destinations.size() + ".txt";
            FileWriter fileWriter = new FileWriter(path);
            int n = 1;
            for (Destination destination : MainActivity.destinations) {
                fileWriter.write(n++ + ". " + destination.getLatitude() + ", " + destination.getLongitude() + "\n");
            }
            fileWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}