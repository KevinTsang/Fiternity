package kevts.washington.edu.fiternity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.parse.Parse;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class MatchesFragment extends ListFragment {

    private FiternityApplication instance;
    private ArrayAdapter<ParseUser> matches;

    public MatchesFragment() {
        // Required empty public constructor
        instance = FiternityApplication.getInstance();
        for (long id : instance.getFriendIds()) {
            ParseQuery<ParseUser> matchesQuery = ParseUser.getQuery();
            matchesQuery.whereEqualTo("authData", id + "");
            // Add to matches here
        }
    }

    public static MatchesFragment newInstance() {
        MatchesFragment fragment = new MatchesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matches, container, false);
    }


}
