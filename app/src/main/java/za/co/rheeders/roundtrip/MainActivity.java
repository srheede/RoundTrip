package za.co.rheeders.roundtrip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Destination> destinations = new ArrayList<>();
    public static ArrayList<Destination> destinationsShort = new ArrayList<>();
    public static int Name = 1;
    private static final int STORAGE_PERMISSION_CODE = 101;
    public static String filePath;
    public static int switchAlgorithm = 0;
    public static int hasSolution = 0;
    public static int solution = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RadioGroup LoadOption = findViewById(R.id.radioGroupLoadOption);
        Button Next = findViewById(R.id.buttonNext);
        Button Reset = findViewById(R.id.buttonReset);

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destinations.clear();
                destinationsShort.clear();
                Name = 1;
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destinations.clear();
                destinationsShort.clear();
                Name = 1;
                switch (LoadOption.getCheckedRadioButtonId()) {
                    case R.id.radioButtonLoadCo:
                        startActivity(new Intent(getApplicationContext(), LoadCoordinates.class));
                        break;
                    case R.id.radioButtonLoadGeoHash:
                        startActivity(new Intent(getApplicationContext(), LoadGeoHash.class));
                        break;
                    case R.id.radioButtonEnterCo:
                        startActivity(new Intent(getApplicationContext(), EnterCoordinates.class));
                        break;
                    case R.id.radioButtonSelectPlaces:
                        startActivity(new Intent(getApplicationContext(), SelectPlaces.class));
                        break;
                    case R.id.radioButtonProofCheck:
                        startActivity(new Intent(getApplicationContext(), ProofCheckAlgorithm.class));
                        break;
                    case R.id.radioButtonUseExample:
                        startActivity(new Intent(getApplicationContext(), UseExample.class));
                        break;
                }
            }
        });
    }
}