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

public class MainActivity extends FragmentActivity {

    ImageButton btnMeasure;
    ImageButton btnViewDrives;
    ImageButton btnViewPutts;
    LinearLayout background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnMeasure = (ImageButton) findViewById(R.id.btnMeasure);
        btnViewDrives = (ImageButton) findViewById(R.id.btnDrivingRecords);
        btnViewPutts = (ImageButton) findViewById(R.id.btnPuttRecords);
        background = (LinearLayout) findViewById(R.id.background);

        // Create sample throws for testing: delete later
        ThrowsDAO datasource = new ThrowsDAO(this);
        try{
            datasource.open();

            //datasource.saveThrow(230, "drive", "UMD", "4/29/2015");
            //datasource.saveThrow(90, "putt", "Bunker", "4/27/2015");
        }catch (SQLException e){

        }

        //end sample code
        btnMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DistanceMeasuringActivity.class);
                startActivity(i);
            }
        });

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

//    @Override
//    public void onClick(View view) {
//        Log.i(MainActivity.class.getName(), "Initiating generic onClick listener");
//
//        Intent i = new Intent(getApplicationContext(), RecordsViewActivity.class);
//
//        boolean putts = view.getId() == R.id.btnPuttRecords;
//        i.putExtra("putts", putts);
//
//        Log.i(MainActivity.class.getName(), "The variable \"putts\" is" + putts);
//
//        startActivity(i);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
