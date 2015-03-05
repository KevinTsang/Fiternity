package kevts.washington.edu.fiternity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedbackShareFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedbackShareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */public class FeedbackShareFragment extends Fragment {

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
        return rootView;
    }

}
