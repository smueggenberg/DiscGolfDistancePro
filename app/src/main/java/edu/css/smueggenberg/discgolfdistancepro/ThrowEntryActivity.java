package edu.css.smueggenberg.discgolfdistancepro;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class ThrowEntryActivity extends ActionBarActivity {

    TextView distance;
    EditText courseName;
    Spinner throwType;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throw_entry);

        extras = getIntent().getExtras();

        distance = (TextView) findViewById(R.id.txtDistance);
        courseName = (EditText) findViewById(R.id.txtCourseName);
        throwType = (Spinner) findViewById(R.id.spnThrowType);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.typesOfThrows, android.R.layout.simple_spinner_dropdown_item);

        throwType.setAdapter(adapter);

        distance.setTextColor(Color.YELLOW);
        courseName.setTextColor(Color.YELLOW);

        distance.setText(Float.toString(extras.getFloat("Distance")) + " meters");
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_throw_entry, menu);
//        return true;
//    }
//
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
