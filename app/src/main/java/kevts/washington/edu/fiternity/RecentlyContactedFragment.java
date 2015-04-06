package kevts.washington.edu.fiternity;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecentlyContactedFragment extends ListFragment {


    public RecentlyContactedFragment() {
        // Required empty public constructor
    }

    public static RecentlyContactedFragment newInstance() {
        RecentlyContactedFragment fragment = new RecentlyContactedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recently_contacted, container, false);
    }


}
