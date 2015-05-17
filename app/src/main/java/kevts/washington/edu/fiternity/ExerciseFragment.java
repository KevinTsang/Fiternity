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
import android.util.Log;
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

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ExerciseFragment extends Fragment {

    static final int CALENDAR_ACCESSED = 21;
    private View rootView;
    private LayoutInflater layoutInflater;
    private ArrayAdapter<String> adapter;
    private Set<ParseObject> exerciseArrayList;
    private SeekBar seekTop;
    private SeekBar seekBottom;
    private int user_level = 0;
    private int partner_level = 0;

    public ExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize saved exercises here
        layoutInflater = inflater;
        rootView = inflater.inflate(R.layout.fragment_exercise, container, false);
        final AutoCompleteTextView searchBox = (AutoCompleteTextView)rootView.findViewById(R.id.searchBox);
        String[] allSports = new String[] {"aerobatics", "air racing", "ballooning", "hang gliding",
        "parachuting", "BASE jumping", "skydiving", "paragliding", "archery", "badminton",
        "tennis", "pickleball", "volleyball", "wallyball", "basketball", "baseball", "cricket",
        "softball", "t-ball", "baton twirling", "skateboarding", "scootering", "longboarding",
        "luging", "snowboarding", "skiing", "wakeboarding", "surfing", "dodgeball", "quidditch",
        "handball", "rock climbing", "bouldering", "ice climbing", "mountaineering", "hiking",
        "cycling", "mountain biking", "bmx biking", "unicycling", "aikido", "jujitsu", "judo",
        "sumo", "wrestling", "boxing", "capoeira", "karate", "kenpo", "kickboxing", "muay thai",
        "taekwondo", "wing chun", "pankration", "jeet kune do", "krav maga", "kendo", "fencing",
        "iaido", "swordfighting", "airsoft", "laser tag", "paintball", "billiards", "pool",
        "polo", "equestrian sports", "fishing", "ultimate frisbee", "football", "soccer",
        "american football", "rugby", "kickball", "golf", "gymnastics", "acrobatics", "aerobics",
        "trampolining", "water polo", "hunting", "broomball", "curling", "ice hockey", "skating",
        "figure skating", "parasailing", "geocaching", "four square", "lacrosse", "racquetball",
        "squash", "table tennis", "ping-pong", "running", "sailing", "shooting", "free running",
        "parkour", "tag", "capture the flag", "hide and seek", "backpacking", "walking", "canoeing",
        "kayaking", "rafting", "rowing", "swimming", "weightlifting", "lifting", "demolition derby",
        "racing", "drag racing", "off-road racing", "monster truck", "motocross", "boat racing",
        "cross-country", "pole vaulting", "long jumping", "speed skating", "roller blading",
        "tetherball", "hacky sack", "jogging", "sandboarding", "pole dancing", "abseiling", "aquathlon",
        "arm wrestling", "artistic billiards", "autocross", "bagatelle", "ballroom dancing", "beach volleyball", 
        "biathlon", "bowling", "camping", "darts", "diving", "flag football", "foosball", "quad biking", "river rafting",
        "scuba diving", "shuffleboard", "snooker", "triathlon", "tug of war", "water skiing", "wind surfing", "yoga"};

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, allSports);
        if (FiternityApplication.getInstance().getExercises() == null)
            exerciseArrayList = new HashSet<>();
        exerciseArrayList = FiternityApplication.getInstance().getExercises();
        for (ParseObject parseObject : exerciseArrayList) {
            LinearLayout ll = new LinearLayout(getActivity());
            ll.setOrientation(LinearLayout.HORIZONTAL);
            TextView exerciseTextView = new TextView(getActivity());
            exerciseTextView.setText(parseObject.getString("name"));
            exerciseTextView.setOnClickListener(editExerciseDialogListener(parseObject));
            ll.addView(exerciseTextView);
            GridLayout exercises = (GridLayout)rootView.findViewById(R.id.exercises);
            Button button = new Button(getActivity());
            button.setText("X");
            button.setOnClickListener(removeExerciseListener(parseObject, ll, exercises));
            ll.addView(button);
            exercises.addView(ll);
        }
        searchBox.setAdapter(adapter);
        searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String exercise = searchBox.getAdapter().getItem(position).toString();
                searchBox.setText("");
                createDialog(exercise);
            }
        });

        Button nextButton = (Button)rootView.findViewById(R.id.toCalendarButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long startMillis = Calendar.getInstance().getTimeInMillis();
                Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                builder.appendPath("time");
                ContentUris.appendId(builder, startMillis);
                saveExercises();
                Calendar beginTime = Calendar.getInstance();
//        beginTime.set(); set a time here
                Calendar endTime = Calendar.getInstance();
//        endTime.set();  set a time here
                Intent createEventIntent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
//                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
//                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                        .putExtra(CalendarContract.Events.TITLE, "Fiternity")
                        .putExtra(CalendarContract.Events.DESCRIPTION, "Insert activity here")
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, "Insert location here")
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);
//                                .putExtra(Intent.EXTRA_EMAIL, "email address here, another email address here");
                Intent intent = new Intent(Intent.ACTION_INSERT).setData(builder.build());
                startActivityForResult(intent, CALENDAR_ACCESSED);
            }
        });

        return rootView;
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
        ParseUser user = instance.getParseUser();
        for (ParseObject exercise : exerciseArrayList) {
            user.add("Exercise", exercise);
        }
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i("Exercise Fragment", "Saved exercises successfully!");
            }
        });

    }

    private void createDialog(String exercise) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        user_level = 0;
        partner_level = 0;
        builder.setTitle(exercise);
        View dialogLayout = layoutInflater.inflate(R.layout.fragment_exp_level_setter, null);
        builder.setView(dialogLayout);
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
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            private String exercise;

            @Override
            public void onClick(DialogInterface dialog, int which) {
                GridLayout exercises = (GridLayout)rootView.findViewById(R.id.exercises);
                LinearLayout ll = new LinearLayout(getActivity());
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ParseObject activity = new ParseObject("Exercise");
                activity.put("name", exercise);
                activity.put("userExpLevel", user_level);
                activity.put("partnerExpLevel", partner_level);
                exerciseArrayList.add(activity);
                TextView exerciseTextView = new TextView(getActivity());
                exerciseTextView.setText(exercise);
                exerciseTextView.setOnClickListener(editExerciseDialogListener(activity));
                ll.addView(exerciseTextView);
                Button button = new Button(getActivity());
                button.setText("X");
                button.setOnClickListener(removeExerciseListener(activity, ll, exercises));
                ll.addView(button);
                exercises.addView(ll);
            }

            private DialogInterface.OnClickListener init(String exercise) {
                this.exercise = exercise;
                return this;
            }
        }.init(exercise));
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }

    private void editDialog(String exercise, ParseObject activity, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogLayout = layoutInflater.inflate(R.layout.fragment_exp_level_setter, null);
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
        user_level = activity.getInt("userExpLevel");
        partner_level = activity.getInt("partnerExpLevel");
        seekTop.setProgress(user_level);
        seekBottom.setProgress(partner_level);
        builder.setTitle(exercise);
        builder.setView(dialogLayout);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            private View view;
            private String exercise;

            @Override
            public void onClick(DialogInterface dialog, int which) {
                GridLayout exercises = (GridLayout)rootView.findViewById(R.id.exercises);
                LinearLayout ll = (LinearLayout)view.getParent();
                ParseObject editedActivity = new ParseObject("Exercise");
                editedActivity.put("name", exercise);
                editedActivity.put("userExpLevel", user_level);
                editedActivity.put("partnerExpLevel", partner_level);
                exerciseArrayList.add(editedActivity);
                TextView exerciseTextView = new TextView(getActivity());
                exerciseTextView.setText(exercise);
                exerciseTextView.setOnClickListener(editExerciseDialogListener(editedActivity));
                ll.addView(exerciseTextView);
                Button button = new Button(getActivity());
                button.setText("X");
                button.setOnClickListener(removeExerciseListener(editedActivity, ll, exercises));
                ll.addView(button);
                exercises.addView(ll);
            }

            private DialogInterface.OnClickListener init(View view, String exercise) {
                this.view = view;
                this.exercise = exercise;
                return this;
            }
        }.init(view, exercise));
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }

    private View.OnClickListener removeExerciseListener(ParseObject activity, LinearLayout ll, GridLayout exercises) {
        return new View.OnClickListener() {
            private ParseObject activity;
            private LinearLayout ll;
            private GridLayout exercises;

            @Override
            public void onClick(View v) {
                exerciseArrayList.remove(activity);
                ViewGroup parentView = (ViewGroup) v.getParent();
                parentView.removeView(v);
                exercises.removeView(ll);
            }
            private View.OnClickListener init(ParseObject activity, LinearLayout ll, GridLayout exercises) {
                this.activity = activity;
                this.ll = ll;
                this.exercises = exercises;
                return this;
            }
        }.init(activity, ll, exercises);
    }

    private View.OnClickListener editExerciseDialogListener(ParseObject activity) {
        return new View.OnClickListener() {
            private ParseObject activity;

            @Override
            public void onClick(View v) {
                exerciseArrayList.remove(activity);
                editDialog(activity.getString("name"), activity, v);
            }

            private View.OnClickListener init(ParseObject activity) {
                this.activity = activity;
                return this;
            }
        }.init(activity);
    }
}
