package za.co.rheeders.roundtrip;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LoadCoordinates extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static final int READ_REQUEST_CODE = 42;
    private static final int READ_REQUEST_CODE_SHORT = 43;
    private TextView tv_output;
    private TextView tv_output_short;
    private static final int STORAGE_PERMISSION_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_coordinates);
        MainActivity.hasSolution = 0;

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }

        Button load = findViewById(R.id.buttonLoad);
        Button loadShort = findViewById(R.id.buttonLoadShort);
        tv_output = findViewById(R.id.tv_output);
        tv_output_short = findViewById(R.id.tv_output_short);
        Button buttonCalcRouteChristofides = findViewById(R.id.buttonCalcRouteChristofides);
        Button buttonCalcRouteParameterDiamond = findViewById(R.id.buttonCalcRouteParameterDiamond);
        Button buttonCalcRouteShrinkCycle = findViewById(R.id.buttonCalcRouteShrinkCycle);
        Button buttonCalcRouteCombo = findViewById(R.id.buttonCalcRouteCombo);

        buttonCalcRouteShrinkCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.switchAlgorithm = 0;
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });

        buttonCalcRouteParameterDiamond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.switchAlgorithm = 1;
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });

        buttonCalcRouteChristofides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.switchAlgorithm = 2;
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performFileSearch(READ_REQUEST_CODE);
            }
        });

        loadShort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performFileSearch(READ_REQUEST_CODE_SHORT);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                MainActivity.filePath = uri.getPath();
                MainActivity.filePath = MainActivity.filePath.substring(MainActivity.filePath.indexOf(":") + 1);
                readText(MainActivity.filePath, READ_REQUEST_CODE);
                tv_output.setText(MainActivity.filePath);

            }
        } else if (requestCode == READ_REQUEST_CODE_SHORT && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                MainActivity.filePath = uri.getPath();
                MainActivity.filePath = MainActivity.filePath.substring(MainActivity.filePath.indexOf(":") + 1);
                readText(MainActivity.filePath, READ_REQUEST_CODE_SHORT);
                tv_output_short.setText(MainActivity.filePath);
            }
        }
    }

    private void readText(String input, int requestCode) {
        File file = new File(input);
        try {
            MainActivity.destinations.clear();
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace(',', ' ');
                line = line.replace('°', ' ');
                Scanner scanner = new Scanner(line);
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
                if (count == 2 && requestCode == READ_REQUEST_CODE) {
                    Destination destination = new Destination(first, second);
                    MainActivity.destinations.add(destination);
                } else if (count == 2 && requestCode == READ_REQUEST_CODE_SHORT) {
                    Destination destination = new Destination(first, second);
                    MainActivity.destinationsShort.add(destination);
                } else if (count == 3 && requestCode == READ_REQUEST_CODE) {
                    Destination destination = new Destination(second, third);
                    MainActivity.destinations.add(destination);
                } else if (count == 3 && requestCode == READ_REQUEST_CODE_SHORT) {
                    Destination destination = new Destination(second, third);
                    MainActivity.destinationsShort.add(destination);
                }
            }
            br.close();
//            saveFile(file);
            tv_output.setText(MainActivity.filePath + "\n\nsuccessfully loaded.");
        } catch (IOException e) {
            tv_output.setText(e.toString());
        }
    }

    private void performFileSearch(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        readFileActivityResultLauncher.launch(intent);
//        startActivityForResult(intent, requestCode);
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> readFileActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            Uri uri = data.getData();
                            MainActivity.filePath = uri.getPath();
                            MainActivity.filePath = MainActivity.filePath.substring(MainActivity.filePath.indexOf(":") + 1);
                            readText(MainActivity.filePath, READ_REQUEST_CODE);
                        }
                    }
                }
            });


    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(LoadCoordinates.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(LoadCoordinates.this, new String[]{permission}, requestCode);
        }
    }

    private void saveFile(File filePath) {
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        try {
            String path = "/storage/emulated/0/Download/PlacesCoordinates" + MainActivity.destinations.size() + ".txt";
            FileWriter fileWriter = new FileWriter(filePath);//.getPath().substring(0, filePath.getPath().length() - filePath.getName().length()) + "CopyOf" + filePath.getName().toLowerCase().substring(0, filePath.getName().length()));
            ArrayList<Destination> addedDestinations = new ArrayList<>();
            for (Destination destination : MainActivity.destinations) {
//                if (!addedDestinations.stream().anyMatch(addedDestination -> addedDestination.compareTo(destination) == 0)) {
                    fileWriter.write("destination = new Destination(" + destination.getLatitude() + "," + destination.getLongitude() + ");\nMainActivity.destinations.add(destination);\n");
                    addedDestinations.add(destination);
//                }
            }
            fileWriter.close();
            tv_output.setText(MainActivity.filePath + "\n\nsuccessfully updated.");
        } catch (IOException e) {
            tv_output.setText("Exception: " + e.toString());
        }
    }
}