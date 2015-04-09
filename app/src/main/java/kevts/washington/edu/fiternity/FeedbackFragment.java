package kevts.washington.edu.fiternity;


import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends android.support.v4.app.Fragment {

    public FeedbackFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feedback, container, false);ImageButton positiveBtn = (ImageButton) rootView.findViewById(R.id.feedback_positive_btn);
        ImageButton negativeBtn = (ImageButton) rootView.findViewById(R.id.feedback_negative_btn);
        TextView questionText = (TextView) rootView.findViewById(R.id.feedback_question);

        //setQuestion(questionText);

        //positive button will go to share feedback fragment
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackShareFragment feedbackShareFragment = new FeedbackShareFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        //.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left
                                //, R.animator.slide_in_left, R.animator.slide_out_right)
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
