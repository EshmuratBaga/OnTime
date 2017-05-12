package project.ontime.kz.ontime.screen.statistic.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.ontime.kz.ontime.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeekFragment extends Fragment {

    public static WeekFragment getInstance() {
        Bundle args = new Bundle();
        WeekFragment fragment = new WeekFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public WeekFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_week, container, false);
    }

}
