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

        ArrayList<Destination> destinations = new ArrayList<>();
        Random random = new Random();
        double latitude;
        double longitude;

        for (int i = 0; i <= 13; i++) {
//            for (int j = 0; j <= 10; j++){
            for (int k = 0; k <= i; k++) {
                latitude = random.nextInt(40000) + 25000;
                latitude /= 1000;
                longitude = random.nextInt(40000) + 70000;
                longitude /= 1000;
                Destination destination = new Destination(latitude, longitude);
                destinations.add(destination);
            }

//            }
        }
        MainActivity.destinations = destinations;
    }
}