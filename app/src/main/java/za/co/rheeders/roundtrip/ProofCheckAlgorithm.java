package za.co.rheeders.roundtrip;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class ProofCheckAlgorithm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proof_check_algorithm);

        ArrayList<Destination> destinations;
        ArrayList<ArrayList<Destination>> routes = new ArrayList<ArrayList<Destination>>();
        Random random = new Random();
        double latitude;
        double longitude;

        MainActivity.destinations.clear();

        for (int i = 4; i <= 300; i++) {
            destinations = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                latitude = random.nextInt(10000) - 10000;
                latitude /= 1000;
                longitude = random.nextInt(10000) + 18500;
                longitude /= 1000;
                destinations.add(new Destination(latitude, longitude));
            }
            routes.add(destinations);
        }
        MainActivity.destinations = routes.get(30);
    }
}