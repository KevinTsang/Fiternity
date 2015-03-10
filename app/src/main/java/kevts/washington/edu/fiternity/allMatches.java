package kevts.washington.edu.fiternity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class allMatches extends Fragment {
    //private OnFragmentInteractionListener mListener;

    private Matches hostActivity;

    /*
    public static allMatches newInstance(String param1, String param2) {
        allMatches fragment = new allMatches();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public allMatches() {
        // Required empty public constructor
    }
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_matches, container, false);

        //populates exercises
        Exercise[] exercises = new Exercise[]{
                new Exercise("Swimming", FakeData.createAnnie()),
                new Exercise("Weight Lifting", FakeData.createJenny()),
                new Exercise("Running", FakeData.createMarshall()),
                new Exercise("Hiking", FakeData.createMichael()),
        };
        MatchesArrayAdapter adapter = new MatchesArrayAdapter(hostActivity.getApplicationContext(), R.layout.listview_match_row, exercises);

        ListView matchList = (ListView)view.findViewById(R.id.allMatchesList);
        matchList.setAdapter(adapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        //if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
       // }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.hostActivity = (Matches) activity;
        try {
            //mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
     */

}
