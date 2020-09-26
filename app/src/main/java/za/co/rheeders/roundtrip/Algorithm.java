package za.co.rheeders.roundtrip;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Algorithm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorithm);

        StringBuilder stringBuilder = new StringBuilder();

        for (Destination destination : MainActivity.destinations) {
            stringBuilder.append(destination.getGeoHash()).append("\n");
        }

        TextView textViewCalc = findViewById(R.id.textViewCalc);

        textViewCalc.setText(stringBuilder.toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
    }
}