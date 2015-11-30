/**
 * Class ObituariesActivity:
 *
 * This activity shows the obituaries list
 *
 * author: Anuj More (atm140330)
 */

package ui.killemall;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.ArrayList;
import ui.killemall.model.Entry;
import ui.killemall.model.EntryController;

public class ObituariesActivity extends Activity {
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
        data = controller.fetchDead();
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
        ImageView icon = (ImageView) findViewById(R.id.imageView);

        CustomAdapter adapter = new CustomAdapter(this, listViewItems, !listEmpty);
        listView.setAdapter(adapter);

        icon.setImageDrawable(getResources().getDrawable(R.drawable.list_icon_dead));

        if (!listEmpty) {
            // Set click event on the list view
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                    Intent intent = new Intent(view.getContext(), ShowActivity.class);
                    // Assign extra information about the click (edit contact or new contact)
                    intent.putExtra("STATUS", "DEAD");
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
