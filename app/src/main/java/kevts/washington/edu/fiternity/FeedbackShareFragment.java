package kevts.washington.edu.fiternity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


 public class FeedbackShareFragment extends Fragment {

    public static FeedbackShareFragment newInstance() {
        FeedbackShareFragment fragment = new FeedbackShareFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FeedbackShareFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                FeedbackActivity feedbackActivity = (FeedbackActivity) getActivity();
                feedbackActivity.finish();
            }
        });
        return rootView;
    }

}
