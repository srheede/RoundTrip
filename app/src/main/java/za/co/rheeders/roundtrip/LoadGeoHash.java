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

public class LoadGeoHash extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static final int READ_REQUEST_CODE = 42;
    private static final int READ_REQUEST_CODE_SHORT = 43;
    private TextView tv_output;
    private TextView tv_output_short;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_geohash);

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
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                readText(path, READ_REQUEST_CODE);
                tv_output.setText(path);
            }
        } else if (requestCode == READ_REQUEST_CODE_SHORT && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
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
                Scanner scanner = new Scanner(line);
                int count = 0;
                while (scanner.hasNext()) {
                    count++;
                    scanner.next();
                }
                scanner.close();
                if (count == 1 && requestCode == READ_REQUEST_CODE) {
                    Destination destination = new Destination(line);
                    MainActivity.destinations.add(destination);
                } else if (count == 1 && requestCode == READ_REQUEST_CODE_SHORT) {
                    Destination destination = new Destination(line);
                    MainActivity.destinationsShort.add(destination);
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