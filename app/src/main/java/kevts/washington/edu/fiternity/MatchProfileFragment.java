package kevts.washington.edu.fiternity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class MatchProfileFragment extends Fragment {

    private FiternityApplication instance;

    public MatchProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_match_profile, container, false);
        instance = FiternityApplication.getInstance();
        TextView matchProfileName = (TextView)rootView.findViewById(R.id.match_profile_name);
        TextView matchProfileGender = (TextView)rootView.findViewById(R.id.match_profile_gender);
        TextView matchProfileAge = (TextView)rootView.findViewById(R.id.match_profile_age);
        ListView matchProfileTimes = (ListView)rootView.findViewById(R.id.match_profile_list);
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        String matchId = getActivity().getIntent().getStringExtra("facebookId");
        parseQuery.whereEqualTo("facebookId", matchId);
        try {
            List<ParseUser> matchUsers = parseQuery.find();
            ParseUser matchUser = matchUsers.get(0);
            matchProfileName.setText(matchUser.getString("name"));
            matchProfileGender.setText(matchUser.getString("gender"));
            matchProfileAge.setText(matchUser.getInt("age") + "");
            List<ParseObject> otherUserSchedule = matchUser.getList("event");
            for (int i = 0; i < otherUserSchedule.size(); i++) {
                ParseObject pointer = otherUserSchedule.get(i);
                otherUserSchedule.remove(i);
                otherUserSchedule.add(i, instance.convertPointerToObjectEvent(pointer));
            }
            List<ParseObject> matchEvents = instance.getMatchingSchedule(otherUserSchedule);
            matchProfileTimes.setAdapter(new MatchTimesArrayAdapter(getActivity(),
                    R.layout.match_times_row, android.R.id.text1,
                    /*replace the above text1 with proper material design*/ matchEvents));
        } catch (ParseException pe) {
            Log.e("MatchProfileFragment", "Either user isn't in Facebook or doesn't exist");
        }
        return rootView;
    }

}
