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

import java.util.List;


public class recentlyContacted extends Fragment {
    private Matches hostActivity;

    //private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment recentlyContacted.
     */
    // TODO: Rename and change types and number of parameters
    public static recentlyContacted newInstance(String param1, String param2) {
        recentlyContacted fragment = new recentlyContacted();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public recentlyContacted() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_matches, container, false);

        List<FreeEvent> recent = hostActivity.getRecentEvents();
        if(recent.size()>0) {
            final FreeEvent[] freeEvents = new FreeEvent[recent.size()];
            recent.toArray(freeEvents);

            MatchesArrayAdapter adapter = new MatchesArrayAdapter(hostActivity.getApplicationContext(), R.layout.listview_match_row, freeEvents);

            ListView matchList = (ListView) view.findViewById(R.id.allMatchesList);
            matchList.setAdapter(adapter);
            matchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    User userClicked = freeEvents[position].getUser(0);
                    String userClickedName = userClicked.getName();
                    Log.i("allMatches", userClickedName);
                    Intent intent = new Intent(hostActivity, MatchProfile.class);
                    intent.putExtra("userClicked", userClicked);
                    startActivity(intent);

                }
            });
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        //if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
        //}
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
