package za.co.rheeders.roundtrip;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Solution extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        ImageView imageViewSolution = findViewById(R.id.imageViewSolution);

        switch (MainActivity.solution) {
            case 1:
                imageViewSolution.setImageResource(R.drawable.solution_29_points);
                break;
            case 2:
                imageViewSolution.setImageResource(R.drawable.solution_38_points);
                break;
            case 3:
                imageViewSolution.setImageResource(R.drawable.solution_980_points);
                break;
        }
    }
}
