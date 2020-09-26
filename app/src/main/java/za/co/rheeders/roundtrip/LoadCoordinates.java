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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoadCoordinates extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static final int READ_REQUEST_CODE = 42;
    private static final int READ_REQUEST_CODE_SHORT = 43;
    private TextView tv_output;
    private TextView tv_output_short;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_coordinates);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }

        Button load = findViewById(R.id.buttonLoad);
        Button loadShort = findViewById(R.id.buttonLoadShort);
        Button buttonCalcRoute = findViewById(R.id.buttonCalcRoute);
        tv_output = findViewById(R.id.tv_output);
        tv_output_short = findViewById(R.id.tv_output_short);

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

        buttonCalcRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
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
                String path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                readText(path, READ_REQUEST_CODE);
                tv_output.setText(path);
            }
        } else if (requestCode == READ_REQUEST_CODE_SHORT && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                readText(path, READ_REQUEST_CODE_SHORT);
                tv_output_short.setText(path);
            }
        }
    }

    private void readText(String input, int requestCode) {
        File file = new File(input);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
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
                } else if (count == 3) {
                    Destination destination = new Destination(second, third);
                    MainActivity.destinations.add(destination);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void performFileSearch(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, requestCode);
    }
}