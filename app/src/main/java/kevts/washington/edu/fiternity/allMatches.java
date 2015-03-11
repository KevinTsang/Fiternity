package kevts.washington.edu.fiternity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Date;

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
        Date now = new Date();
        final FreeEvent[] exercises = new FreeEvent[]{
                new FreeEvent(FakeData.createAnnie(), now, now),
                new FreeEvent(FakeData.createJenny(), now, now),
                new FreeEvent(FakeData.createMarshall(), now, now),
                new FreeEvent(FakeData.createMichael(), now, now),
        };
        MatchesArrayAdapter adapter = new MatchesArrayAdapter(hostActivity.getApplicationContext(), R.layout.listview_match_row, exercises);

        ListView matchList = (ListView)view.findViewById(R.id.allMatchesList);
        matchList.setAdapter(adapter);
        matchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User userClicked = exercises[position].getUser(0);
                String userClickedName = userClicked.getName();
                Log.i("allMatches", userClickedName);
                Intent intent = new Intent(hostActivity, UserProfile.class);
                intent.putExtra("userClicked", userClicked);
                startActivity(intent);

            }
        });

        return view;
    }

    private void goToProfile(User user){
        Log.i("goToProfile", "listener works");
        Intent intent = new Intent(hostActivity.getApplicationContext(), UserProfile.class);
        this.startActivity(intent);
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
