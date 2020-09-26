package za.co.rheeders.roundtrip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Scanner;

import androidx.appcompat.app.AppCompatActivity;

public class EnterCoordinates extends AppCompatActivity {

    private EditText latitude;
    private EditText longitude;
    private TextView textViewAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_coordinates);

        latitude = findViewById(R.id.editTextLatitude);
        longitude = findViewById(R.id.editTextLongitude);
        textViewAdded = findViewById(R.id.textViewAdded);
        Button buttonAddCo = findViewById(R.id.buttonAddCo);
        Button buttonCalcRoute = findViewById(R.id.buttonCalcRoute);

        buttonAddCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCoordinates();
            }
        });

        buttonCalcRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        double doubleLat = 0;
        if (scanLat.hasNextDouble()) {
            doubleLat = scanLat.nextDouble();
            count++;
        }
        while (scanLat.hasNext()) {
            String nextLat = scanLat.next();
            if (nextLat.charAt(0) == 'S' && doubleLat > 0) {
                doubleLat = 0 - doubleLat;
            }
        }
        scanLat.close();

        String lon = longitude.getText().toString();
        lon = lon.replace(',', ' ');
        lon = lon.replace('°', ' ');

        Scanner scanLong = new Scanner(lon);
        double doubleLong = 0;
        if (scanLong.hasNextDouble()) {
            doubleLong = scanLong.nextDouble();
            count++;
        }

        while (scanLong.hasNext()) {
            String nextLong = scanLong.next();
            if (nextLong.charAt(0) == 'W' && doubleLong > 0) {
                doubleLong = 0 - doubleLong;
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
}



