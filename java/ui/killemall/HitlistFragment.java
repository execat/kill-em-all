package ui.killemall;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.camera2basic.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class HitlistFragment extends Fragment {

    public HitlistFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hitlist, container, false);
    }
}
