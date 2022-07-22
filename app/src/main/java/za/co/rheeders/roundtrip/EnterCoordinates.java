package za.co.rheeders.roundtrip;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class EnterCoordinates extends AppCompatActivity {

    private EditText latitude;
    private EditText longitude;
    private TextView textViewAdded;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_coordinates);

        latitude = findViewById(R.id.editTextLatitude);
        longitude = findViewById(R.id.editTextLongitude);
        textViewAdded = findViewById(R.id.textViewAdded);
        Button buttonAddCo = findViewById(R.id.buttonAddCo);
        Button buttonCalcRouteParameterDiamond = findViewById(R.id.buttonCalcRouteParameterDiamond);
        Button buttonCalcRouteBubbleShrink = findViewById(R.id.buttonCalcRouteBubbleShrink);
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

    }

    private void addCoordinates() {
        int count = 0;
        String lat = latitude.getText().toString();
        lat = lat.replace(',', ' ');
        lat = lat.replace('°', ' ');
        Scanner scanLat = new Scanner(lat);

        Double doubleLat = 0.0;
        if (scanLat.hasNextDouble()) {
            doubleLat = scanLat.nextDouble();
            count++;
        }
        while (scanLat.hasNext()) {
            String nextLat = scanLat.next();
            if (nextLat.charAt(0) == 'S' && doubleLat > 0) {
                doubleLat = 0.0 - doubleLat;
            }
        }
        scanLat.close();

        String lon = longitude.getText().toString();
        lon = lon.replace(',', ' ');
        lon = lon.replace('°', ' ');

        Scanner scanLong = new Scanner(lon);
        Double doubleLong = 0.0;
        if (scanLong.hasNextDouble()) {
            doubleLong = scanLong.nextDouble();
            count++;
        }

        while (scanLong.hasNext()) {
            String nextLong = scanLong.next();
            if (nextLong.charAt(0) == 'W' && doubleLong > 0) {
                doubleLong = 0.0 - doubleLong;
            }
        }
        scanLat.close();

        if (count == 2) {
            Destination destination = new Destination(doubleLat, doubleLong);
            MainActivity.destinations.add(destination);
            String string = getString(R.string.destination_added, doubleLat, doubleLong);
            textViewAdded.setText(string);
        } else {
            textViewAdded.setText(R.string.invalid_coordinates);
        }
    }

    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(EnterCoordinates.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(EnterCoordinates.this, new String[]{permission}, requestCode);
        }
    }

    private void saveFile() {
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        try {
            String path = "/storage/emulated/0/Download/EnteredCoordinates" + MainActivity.destinations.size() + ".txt";
            FileWriter fileWriter = new FileWriter(path);
            int n = 1;
            for (Destination destination : MainActivity.destinations) {
                fileWriter.write(n++ + ". " + destination.getLatitude() + ", " + destination.getLongitude() + destination.getPlaceName() + "\n");
            }
            fileWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}



