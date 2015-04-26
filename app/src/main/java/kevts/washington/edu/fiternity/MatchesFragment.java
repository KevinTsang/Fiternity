package kevts.washington.edu.fiternity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MatchesFragment extends ListFragment {

    private FiternityApplication instance;
    private List<ParseUser> matches;

    public MatchesFragment() {
        // Required empty public constructor
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
        View rootView = inflater.inflate(R.layout.fragment_matches, container, false);
        instance = FiternityApplication.getInstance();
        matches = new ArrayList<ParseUser>();
        for (String id : instance.getFriendIds()) {
            ParseQuery<ParseUser> matchesQuery = ParseUser.getQuery();
            matchesQuery.whereEqualTo("authData", id + "");
            try {
                matches.add(matchesQuery.find().get(0));
            } catch (ParseException e) {
                Log.e("MatchesFragment", "Failed to add user to match list");
            }
        }
        ArrayAdapter<ParseUser> matchAdapter = new ArrayAdapter<ParseUser>(getActivity(),
                R.layout.match_row, matches);
        ListView matchesView = (ListView)rootView.findViewById(android.R.id.list);
        matchesView.setAdapter(matchAdapter);
        matchesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        return rootView;
    }


}
