package za.co.rheeders.roundtrip;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class UseExample extends AppCompatActivity {

    private RadioGroup examples;
    private String selectedExampleFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_example);

        Spinner dropdown = findViewById(R.id.spinnerCitiesList);
        String[] fileNames = readFileNames();
        Arrays.sort(fileNames,  new FileNameBySizeSortingComparator());
        String[] items = fileNames;

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedExampleFileName = fileNames[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Button buttonCalcRouteParameterDiamond = findViewById(R.id.buttonCalcRouteParameterDiamond);
        Button buttonCalcRouteBubbleShrink = findViewById(R.id.buttonCalcRouteShrinkCycle);
        Button buttonCalcRouteCombo = findViewById(R.id.buttonCalcRouteCombo);
        Button buttonCalcRouteChristofides = findViewById(R.id.buttonCalcRouteChristofides);

        buttonCalcRouteBubbleShrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.switchAlgorithm = 0;
                selectExample();
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });

        buttonCalcRouteParameterDiamond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.switchAlgorithm = 1;
                selectExample();
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });

        buttonCalcRouteChristofides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.switchAlgorithm = 2;
                selectExample();
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });

    }

    private String[] readFileNames() {
        try {
            Resources r = this.getResources();
            String[] examples = r.getAssets().list("examples");
            return examples;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void selectExample() {
        try {
            MainActivity.destinations.clear();
            InputStream inputStream = this.getResources().getAssets().open("examples/" + selectedExampleFileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8 ));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace(',', ' ');
                line = line.replace('°', ' ');

                Scanner scanner = new Scanner(line);
                scanner.useLocale(Locale.ROOT);
                int count = 0;
                double first = 0;
                double second = 0;
                double third = 0;
                while (scanner.hasNext()) {
                    if (scanner.hasNextDouble()) {
                        count++;
                        switch (count) {
                            case 1:
                                first = scanner.nextDouble();
                                break;
                            case 2:
                                second = scanner.nextDouble();
                                break;
                            case 3:
                                third = scanner.nextDouble();
                                break;
                            default:
                                scanner.next();
                        }
                        if (second > 1000) {
                            second = second / 1000;
                        } else if (third > 1000) {
                            third = third / 1000;
                        }
                    } else {
                        String next = scanner.next();
                        if (next.charAt(0) == 'S' && first > 0) {
                            first = 0 - first;
                        } else if (next.charAt(0) == 'W' && second > 0) {
                            second = 0 - second;
                        }
                    }
                }
                scanner.close();
                if (count == 2) {
                    Destination destination = new Destination(first, second);
                    MainActivity.destinations.add(destination);
                } else if (count == 3) {
                    Destination destination = new Destination(second, third);
                    destination.setPlaceName(Integer.toString((int)first));
                    MainActivity.destinations.add(destination);
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}



