package kevts.washington.edu.fiternity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MatchesFragment extends Fragment {

    private FiternityApplication instance;
    private List<ParseUser> matches;

    public MatchesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_matches, container, false);
        instance = FiternityApplication.getInstance();
        matches = new ArrayList<ParseUser>();
        for (String id : instance.getFriendIds()) {
            ParseQuery<ParseUser> matchesQuery = ParseUser.getQuery();
            matchesQuery.whereEqualTo("facebookId", id);
            try {
                matches.add(matchesQuery.getFirst());
            } catch (ParseException e) {
                Log.e("MatchesFragment", "Failed to add user to match list");
            }
        }
        if (matches.size() == 0) {
            Toast.makeText(getActivity(), "You have no matches.", Toast.LENGTH_LONG);
        }
        MatchesArrayAdapter matchAdapter = new MatchesArrayAdapter(getActivity(),
                R.layout.match_row, android.R.id.text1,
                /*replace this with proper material design text view,*/ matches);

        ListView matchesView = (ListView)rootView.findViewById(R.id.matches_list);
        matchesView.setAdapter(matchAdapter);
        matchesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MatchProfileActivity.class);
                intent.putExtra("facebookId", matches.get(position).getString("facebookId"));
                startActivity(intent);
            }
        });
        return rootView;
    }
}
