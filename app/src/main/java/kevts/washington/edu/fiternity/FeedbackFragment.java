package kevts.washington.edu.fiternity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class FeedbackFragment extends Fragment {

    public static FeedbackFragment newInstance() {
        FeedbackFragment fragment = new FeedbackFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FeedbackFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
        ImageButton positiveBtn = (ImageButton) rootView.findViewById(R.id.feedback_positive_btn);
        ImageButton negativeBtn = (ImageButton) rootView.findViewById(R.id.feedback_negative_btn);

        //positive button will go to share feedback fragment
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackShareFragment feedbackShareFragment =
                        FeedbackShareFragment.newInstance();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction()
                    .setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left
                            , R.animator.slide_in_left, R.animator.slide_out_right)
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