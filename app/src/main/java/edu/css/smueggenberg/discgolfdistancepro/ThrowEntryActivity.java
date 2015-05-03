package edu.css.smueggenberg.discgolfdistancepro;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.Calendar;


public class ThrowEntryActivity extends ActionBarActivity {

    TextView txtDistance;
    EditText txtCourseName;
    Spinner spnThrowType;
    Bundle extras;

    Button btnSave;
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throw_entry);

        extras = getIntent().getExtras();

        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        txtDistance = (TextView) findViewById(R.id.txtDistance);
        txtCourseName = (EditText) findViewById(R.id.txtCourseName);
        spnThrowType = (Spinner) findViewById(R.id.spnThrowType);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.typesOfThrows, R.layout.spinner_item);

        spnThrowType.setAdapter(adapter);

        txtDistance.setTextColor(Color.YELLOW);
        txtCourseName.setTextColor(Color.YELLOW);

        txtDistance.setText(Float.toString(extras.getFloat("Distance")) + " meters");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThrowsDAO datasource = new ThrowsDAO(getApplicationContext());
                try{
                    datasource.open();

                    Calendar c = Calendar.getInstance();

                    datasource.saveThrow((long) extras.getFloat("Distance"), spnThrowType.getSelectedItem().toString(),
                            txtCourseName.getText().toString(), Integer.toString(c.get(Calendar.DATE)));
                }catch (SQLException e){
                    Toast.makeText(getApplicationContext(), "Error saving throw", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), "Throw saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
