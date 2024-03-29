package kevts.washington.edu.fiternity;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


public class ExerciseActivity extends ActionBarActivity {

    static final int CALENDAR_ACCESSED = 21;
    private ArrayAdapter<String> adapter;
    private ArrayList<Exercise> exerciseArrayList;
    private SeekBar seekTop;
    private SeekBar seekBottom;
    int user_level = 0;
    int partner_level = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        Button menuButton = (Button)findViewById(R.id.exercise_menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMenu = new Intent(ExerciseActivity.this, MenuDrawer.class);
                startActivity(toMenu);
            }
        });
        User user = ((FiternityInstance)getApplication()).getUser();
        if (user.getExercises().size() == 0) {
            exerciseArrayList = new ArrayList<Exercise>();
        } else {
            // todo initialize existing exercises when user comes back to activity based on
            // saved exercises
            exerciseArrayList = (ArrayList<Exercise>) user.getExercises();
            for (Exercise e : exerciseArrayList) {
                String exerciseName = e.getExerciseName();
                GridLayout exercises = (GridLayout) findViewById(R.id.exercises);
                LinearLayout ll = new LinearLayout(ExerciseActivity.this);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                int exerciseIndex = exerciseArrayList.size() - 1;
                TextView exercise = new TextView(ExerciseActivity.this);
                exercise.setText(exerciseName);
                exercise.setOnClickListener(createExerciseDialogListener(exerciseName));
                ll.addView(exercise);
                Button button = new Button(ExerciseActivity.this);
                button.setText("X");
                button.setOnClickListener(removeExerciseListener(exerciseIndex, ll, exercises));
                ll.addView(button);
                exercises.addView(ll);

            }
        }
        exerciseArrayList = new ArrayList<Exercise>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, getExercises());
        final AutoCompleteTextView searchBox = (AutoCompleteTextView)findViewById(R.id.searchBox);
        searchBox.setAdapter(adapter);
        searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String exercise = searchBox.getAdapter().getItem(position).toString();
                searchBox.setText("");
                createDialog(exercise);
            }
        });
        Button nextButton = (Button)findViewById(R.id.toCalendarButton);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CALENDAR_ACCESSED) {
            if (resultCode == RESULT_CANCELED) {
                // read all Fiternity events here
                FiternityInstance instance = (FiternityInstance)getApplication();
                ArrayList<FreeEvent> events = instance.getEvents(this, instance.getUser());
                instance.getUser().setFreeTimes(events);
                startActivity(new Intent(this, Matches.class));
            }
        }
    }

    private void saveExercises() {
        FiternityInstance instance = (FiternityInstance)getApplication();
        User user = instance.getUser();
        GridLayout exercisesHolderLayout = (GridLayout)findViewById(R.id.exercises);
        // Max, you need to modify the createDialog method below to get data from
        // the seekbars within exp_level_setter_fragment.xml and add them to the
        // private arraylist variable up there by creating a new exercise and
        // saving it when the user clicks the positive dialog button
        user.getExercises().clear(); // clears all user exercises
        // will come up with a way to compare diffs later, but for now this is fine
        for (Exercise e : exerciseArrayList) {
            user.addExercise(e);
        }
    }

    private void createDialog(final String exercise) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        user_level = 0;
        partner_level = 0;
        builder.setTitle(exercise);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.exp_level_setter_fragment, null);
        builder.setView(dialogLayout);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GridLayout exercises = (GridLayout) findViewById(R.id.exercises);
                LinearLayout ll = new LinearLayout(ExerciseActivity.this);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                exerciseArrayList.add(new Exercise(exercise, user_level, partner_level));
                int exerciseIndex = exerciseArrayList.size() - 1;
                TextView exerciseTextView = new TextView(ExerciseActivity.this);
                exerciseTextView.setText(exercise);
                exerciseTextView.setOnClickListener(createExerciseDialogListener(exercise));
                ll.addView(exerciseTextView);
                Button button = new Button(ExerciseActivity.this);
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
        seekTop.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekTop, int progress, boolean fromUser) {
                user_level = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekTop) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekTop) { }
        });
        seekBottom.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    public String[] getExercises() {

        return new String[] {"swimming", "tennis", "baseball", "football", "soccer", "kendo", "karate", "aerobic", "cartwheels", "dancing"};
    }
}
