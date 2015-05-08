package edu.css.smueggenberg.discgolfdistancepro;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.sql.SQLException;

/**
 * Created by smueggenberg
 * The main activity for the app
 * Shows three buttons for navigating the app
 */
public class MainActivity extends FragmentActivity {

    ImageButton btnMeasure;
    ImageButton btnViewDrives;
    ImageButton btnViewPutts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link the button widgets to variables in code
        btnMeasure = (ImageButton) findViewById(R.id.btnMeasure);
        btnViewDrives = (ImageButton) findViewById(R.id.btnDrivingRecords);
        btnViewPutts = (ImageButton) findViewById(R.id.btnPuttRecords);

        // The onClick listener for "Measure my throw"
        // Opens the distance measuring activity
        btnMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DistanceMeasuringActivity.class);
                startActivity(i);
            }
        });

        // The next two methods set the onClick listeners for viewing distance records and viewing putting records
        // A variable is passed to the records view activity to identify which button was clicked
        btnViewPutts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RecordsViewActivity.class);

                i.putExtra("putts", true);

                startActivity(i);
            }
        });

        btnViewDrives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RecordsViewActivity.class);

                i.putExtra("putts", false);

                startActivity(i);
            }
        });
    }
}
