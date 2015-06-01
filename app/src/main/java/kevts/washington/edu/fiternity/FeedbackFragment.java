package kevts.washington.edu.fiternity;


import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends android.support.v4.app.Fragment {

    public FeedbackFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
        ProfilePictureView feedbackProfilePictureView = (ProfilePictureView)rootView.findViewById(R.id.feedback_profile_picture);
        TextView feedbackExercise = (TextView)rootView.findViewById(R.id.feedback_exercise);
        TextView feedbackPartner = (TextView)rootView.findViewById(R.id.feedback_match);
        Button positiveBtn = (Button)rootView.findViewById(R.id.feedback_positive_btn);
        Button negativeBtn = (Button)rootView.findViewById(R.id.feedback_negative_btn);

        String facebookId = getActivity().getIntent().getStringExtra("facebookId");
        String exercise = getActivity().getIntent().getStringExtra("exercise");
        if (exercise != null) {
            feedbackExercise.setText(exercise);
        }
        feedbackProfilePictureView.setProfileId(facebookId);
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("facebookId", facebookId);
        try {
            ParseUser matchUser = parseQuery.getFirst();
            feedbackPartner.setText(matchUser.getString("name"));
        } catch (ParseException pe) {
            Log.e("FeedbackActivity", "Failed to find user matching that ID");
        }

        //positive button will go to share feedback fragment
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackShareFragment feedbackShareFragment = new FeedbackShareFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.feedback_fragment, feedbackShareFragment)
                        .addToBackStack("")
                        .commit();
            }
        });

        //negative button will go to negative activity
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent negativeActivity = new Intent(getActivity(), FeedbackNegativeActivity.class);
                getActivity().startActivityForResult(negativeActivity, 2);
            }
        });

        return rootView;
    }


}
