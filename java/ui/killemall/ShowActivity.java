package ui.killemall;

import android.os.Bundle;
import android.app.Activity;

import com.example.android.camera2basic.R;

public class ShowActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
