package edu.css.smueggenberg.discgolfdistancepro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by smueggenberg
 * Code for activity where the user can view throws saved to the database.
 * The user can be viewing putts or drives
 */
public class RecordsViewActivity extends FragmentActivity {

    boolean viewingPutts;
    Bundle extras;
    ListView recordsView;
    ThrowsDAO datasource;
    private Throw selectedThrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_view);
        recordsView = (ListView) findViewById(R.id.recordsView);

        extras = getIntent().getExtras();
        viewingPutts = extras.getBoolean("putts");

        datasource = new ThrowsDAO(this);

        List<Throw> throwList;
        final ArrayAdapter<Throw> adapter;

        try {
            datasource.open();

            // Get the list of throws depending on whether view putts or view drives was selected
            if (viewingPutts) {
                throwList = datasource.getThrowList("putt");
            } else {
                throwList  = datasource.getThrowList("drive");
            }

            adapter = new ArrayAdapter<Throw>(getApplicationContext(), R.layout.spinner_item, throwList);
            recordsView.setAdapter(adapter);

            // Sets the on click listener for list items
            // When a list item is clicked, the user can choose to delete the entry or cancel
            recordsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedThrow = (Throw) adapterView.getItemAtPosition(i);

                    AlertDialog.Builder adBuilder = new AlertDialog.Builder(RecordsViewActivity.this);

                    adBuilder.setTitle("Delete Entry");
                    adBuilder.setMessage("Are you sure you want to delete this entry forever?");

                    // Set up the popup dialogue box
                    adBuilder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            datasource.deleteThrow(selectedThrow);
                            adapter.remove(selectedThrow);
                        }
                    });
                    adBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    AlertDialog dialog = adBuilder.create();
                    dialog.show();
                }// end onItemClick method
            });// end onItemClickListener
        }catch (SQLException e){
            // If unable to connect to database, display a toast error message
            Toast toast = new Toast(getApplicationContext());
            toast.setText("Error connecting to database");
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
    }//end on create
}
