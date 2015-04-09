package kevts.washington.edu.fiternity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackShareFragment extends Fragment {


    public FeedbackShareFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_feedback_share, container, false);
        Button doneButton = (Button) rootView.findViewById(R.id.feedback_positive_done_btn);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackActivity feedbackActivity = (FeedbackActivity)getActivity();
                Intent intent = new Intent(getActivity(), MatchesActivity.class);
                feedbackActivity.finish();
                startActivity(intent);
            }
        });
        return rootView;
    }


}
