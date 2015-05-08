package edu.css.smueggenberg.discgolfdistancepro;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created by smueggenberg
 * Code for activity where a throw's information is entered and reviewed before it is saved
 */
public class ThrowEntryActivity extends FragmentActivity {

    TextView txtDistance;
    EditText txtCourseName;
    Spinner spnThrowType;
    Bundle extras;

    ImageButton btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throw_entry);

        // Set up the user interface and link widgets to code
        extras = getIntent().getExtras();

        btnSave = (ImageButton) findViewById(R.id.btnSave);
        btnCancel = (ImageButton) findViewById(R.id.btnCancel);

        txtDistance = (TextView) findViewById(R.id.txtDistance);
        txtCourseName = (EditText) findViewById(R.id.txtCourseName);
        spnThrowType = (Spinner) findViewById(R.id.spnThrowType);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.typesOfThrows, R.layout.spinner_item);
        spnThrowType.setAdapter(adapter);

        txtDistance.setTextColor(Color.YELLOW);
        txtCourseName.setTextColor(Color.YELLOW);

        txtDistance.setText(Float.toString(extras.getFloat("Distance")) + " feet");

        // Set the on click listener for the "save" button to save the throw to the database
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThrowsDAO datasource = new ThrowsDAO(getApplicationContext());
                try{
                    datasource.open();

                    Calendar c = Calendar.getInstance();

                    datasource.saveThrow((long) extras.getFloat("Distance"), spnThrowType.getSelectedItem().toString(),
                            txtCourseName.getText().toString(),
                            Integer.toString(c.get(Calendar.MONTH) + 1) + "/" + Integer.toString(c.get(Calendar.DATE)) + "/" + Integer.toString(c.get(Calendar.YEAR)));
                }catch (SQLException e){
                    // If there is an error accessing the database, display an error message
                    Toast.makeText(getApplicationContext(), "Error saving throw", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), "Throw saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Exit the activity
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
