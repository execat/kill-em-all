package ui.killemall;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_capture_image:
                Toast.makeText(this, "Capturing image...", Toast.LENGTH_LONG);
                return true;

            case R.id.action_open_obituary:
                Toast.makeText(this, "Opening obituary page...", Toast.LENGTH_LONG);
                return true;

            case R.id.action_view_list:
                Toast.makeText(this, "Viewing list...", Toast.LENGTH_LONG);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
