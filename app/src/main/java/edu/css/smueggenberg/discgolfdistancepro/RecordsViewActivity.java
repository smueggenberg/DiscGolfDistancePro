package edu.css.smueggenberg.discgolfdistancepro;

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


public class RecordsViewActivity extends FragmentActivity {

    boolean viewingPutts;
    Bundle extras;
    ListView recordsView;
    ThrowsDAO datasource;

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

            if (viewingPutts) {
                throwList = datasource.getThrowList("putt");
            } else {
                throwList  = datasource.getThrowList("drive");
            }

            adapter = new ArrayAdapter<Throw>(getApplicationContext(), android.R.layout.simple_list_item_1, throwList);

            recordsView.setAdapter(adapter);

            recordsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Throw selectedThrow = (Throw) adapterView.getItemAtPosition(i);

                    datasource.deleteThrow(selectedThrow);
                    adapter.remove(selectedThrow);
                }
            });
        }catch (SQLException e){
            Toast toast = new Toast(getApplicationContext());
            toast.setText("Error connecting to database");
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }finally {

        }

//        recordsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Throw selectedThrow = (Throw) adapterView.getItemAtPosition(i);
//
//                datasource.deleteThrow(selectedThrow);
//                adapter.remove(selectedThrow);
//            }
//        });
    }//end on create

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_records_view, menu);
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
