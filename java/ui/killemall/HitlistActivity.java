package ui.killemall;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.camera2basic.R;

import java.util.ArrayList;

import ui.killemall.model.CustomAdapter;
import ui.killemall.model.Entry;
import ui.killemall.model.EntryController;
import ui.killemall.model.ListViewItem;

public class HitlistActivity extends Activity {
    EntryController controller;
    ArrayList<Entry> data;
    ArrayList<ListViewItem> listViewItems;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hitlist);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        controller = new EntryController();
        data = controller.fetchAlive();
        listViewItems = new ArrayList<ListViewItem>();
        for (Entry row : data) {
            Location location = row.getLocation();
            String latitude = Double.toString(location.getLatitude());
            String longitude = Double.toString(location.getLongitude());
            listViewItems.add(new ListViewItem(row.getTimestamp(), latitude, longitude));
            System.out.println("Added" + row);
        }

        boolean listEmpty = listViewItems.isEmpty();
        if (listEmpty) {
            listViewItems.add(new ListViewItem("Add an item -- Click '+'", "0", "0"));
        }

        listView = (ListView) findViewById(R.id.listView);
        CustomAdapter adapter = new CustomAdapter(this, listViewItems, !listEmpty);
        listView.setAdapter(adapter);

        if (!listEmpty) {
            // Set click event on the list view
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                    Intent intent = new Intent(view.getContext(), ShowActivity.class);
                    // Assign extra information about the click (edit contact or new contact)
                    intent.putExtra("NUMBER", position);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
