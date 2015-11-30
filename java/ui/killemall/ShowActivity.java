/**
 * Class ShowActivity:
 *
 * This activity shows the details of an entry (DEAD or ALIVE)
 *
 * author: Anuj More (atm140330)
 */

package ui.killemall;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.android.camera2basic.R;
import java.util.Locale;
import ui.killemall.model.Entry;
import ui.killemall.model.EntryController;
import ui.killemall.model.EntryStatus;

public class ShowActivity extends Activity {
    Entry current;
    Bundle extras;
    final EntryController controller = new EntryController();
    final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText date = (EditText) findViewById(R.id.date);
        final EditText time = (EditText) findViewById(R.id.time);
        final EditText longitude = (EditText) findViewById(R.id.longitude);
        final EditText latitude = (EditText) findViewById(R.id.latitude);
        final ImageView image = (ImageView) findViewById(R.id.image);

        Button maps = (Button) findViewById(R.id.maps);
        Button virtualKill = (Button) findViewById(R.id.virtual_kill);
        Button forgive = (Button) findViewById(R.id.forgive);

        extras = getIntent().getExtras();
        int number = extras.getInt("NUMBER");
        String status = extras.getString("STATUS");

        if (status.equals("ALIVE")) {
            current = controller.fetchAlive().get(number);
        } else if (status.equals("DEAD")) {
            current = controller.fetchDead().get(number);
        }

        if (current.getStatus().equals(EntryStatus.DEAD)) {
            forgive.setEnabled(false);
            virtualKill.setEnabled(false);
        }

        date.setText(current.getDate());
        time.setText(current.getTime());
        longitude.setText(current.getLongitude());
        latitude.setText(current.getLatitude());
        image.setImageBitmap(BitmapFactory.decodeFile(current.getFile().getAbsolutePath()));

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = current.getLocation();
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f",
                        location.getLatitude(), location.getLongitude(),
                        location.getLatitude(), location.getLongitude());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        virtualKill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = extras.getInt("NUMBER");
                current.setStatus(EntryStatus.DEAD);
                controller.updateEntryAt(number, current);
                finish();
            }
        });

        forgive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = extras.getInt("NUMBER");
                current.deleteImage();
                controller.delete(number);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "Successfully forgiven", Toast.LENGTH_SHORT).show();
                    }
                });

                finish();
            }
        });
    }
}
