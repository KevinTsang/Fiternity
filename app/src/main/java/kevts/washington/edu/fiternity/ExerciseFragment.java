package kevts.washington.edu.fiternity;


import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


public class ExerciseFragment extends Fragment {

    static final int CALENDAR_ACCESSED = 21;
    private LayoutInflater layoutInflater;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> exerciseArrayList; // replace with custom exercise type later
    private SeekBar seekTop;
    private SeekBar seekBottom;
    int user_level = 0;
    int partner_level = 0;

    public ExerciseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize saved exercises here
        layoutInflater = inflater;
        final AutoCompleteTextView searchBox = (AutoCompleteTextView)getActivity().findViewById(R.id.searchBox);
        searchBox.setAdapter(adapter);
        searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String exercise = searchBox.getAdapter().getItem(position).toString();
                searchBox.setText("");
                createDialog(exercise);
            }
        });

        Button nextButton = (Button)getActivity().findViewById(R.id.toCalendarButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long startMillis = Calendar.getInstance().getTimeInMillis();
                Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                builder.appendPath("time");
                ContentUris.appendId(builder, startMillis);
                saveExercises();
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
                startActivityForResult(intent, CALENDAR_ACCESSED);
            }
        });

        return layoutInflater.inflate(R.layout.fragment_exercise, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CALENDAR_ACCESSED) {
//            if (resultCode == RESULT_CANCELED) {
//                // read all Fiternity events here
//                FiternityApplication instance = (FiternityApplication)getApplication();
//                ArrayList<FreeEvent> events = instance.getEvents(this, instance.getUser());
//                instance.getUser().setFreeTimes(events);
//                startActivity(new Intent(this, Matches.class));
//            }
        }
    }

    private void saveExercises() {
        FiternityApplication instance = (FiternityApplication)getActivity().getApplication();
//        User user = instance.getUser();
//        GridLayout exercisesHolderLayout = (GridLayout)findViewById(R.id.exercises);
        // Max, you need to modify the createDialog method below to get data from
        // the seekbars within exp_level_setter_fragment.xml and add them to the
        // private arraylist variable up there by creating a new exercise and
        // saving it when the user clicks the positive dialog button
//        user.getExercises().clear(); // clears all user exercises
        // will come up with a way to compare diffs later, but for now this is fine
//        for (Exercise e : exerciseArrayList) {
//            user.addExercise(e);
//        }
    }

    private void createDialog(final String exercise) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        user_level = 0;
        partner_level = 0;
        builder.setTitle(exercise);
        View dialogLayout = layoutInflater.inflate(R.layout.fragment_exp_level_setter, null);
        builder.setView(dialogLayout);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GridLayout exercises = (GridLayout)getActivity().findViewById(R.id.exercises);
                LinearLayout ll = new LinearLayout(getActivity());
                ll.setOrientation(LinearLayout.HORIZONTAL);
//                exerciseArrayList.add(new Exercise(exercise, user_level, partner_level));
                int exerciseIndex = exerciseArrayList.size() - 1;
                TextView exerciseTextView = new TextView(getActivity());
                exerciseTextView.setText(exercise);
                exerciseTextView.setOnClickListener(createExerciseDialogListener(exercise));
                ll.addView(exerciseTextView);
                Button button = new Button(getActivity());
                button.setText("X");
                button.setOnClickListener(removeExerciseListener(exerciseIndex, ll, exercises));
                ll.addView(button);
                exercises.addView(ll);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create();
        builder.show();
        seekTop = (SeekBar) dialogLayout.findViewById(R.id.user_exp_level);
        seekBottom = (SeekBar) dialogLayout.findViewById(R.id.partner_exp_level);
        seekTop.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekTop, int progress, boolean fromUser) {
                user_level = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekTop) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekTop) { }
        });
        seekBottom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBottom, int progress, boolean fromUser) {
                partner_level = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBottom) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBottom) { }
        });
    }


    private View.OnClickListener createExerciseDialogListener(final String exerciseName) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(exerciseName);
            }
        };
    }

    private View.OnClickListener removeExerciseListener(final int exerciseIndex, final LinearLayout ll, final GridLayout exercises) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseArrayList.remove(exerciseIndex);
                ViewGroup parentView = (ViewGroup) v.getParent();
                parentView.removeView(v);
                exercises.removeView(ll);
            }
        };
    }
}
