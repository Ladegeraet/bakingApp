package name.oho.baking.ui.receipt;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import name.oho.baking.R;

/**
 * Created by tobi on 18.02.18.
 */

public class StepDetailFragment extends Fragment {

    public StepDetailFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        return rootView;
    }
}
