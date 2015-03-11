package kevts.washington.edu.fiternity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ExerciseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, getExercises());
        final AutoCompleteTextView searchBox = (AutoCompleteTextView)findViewById(R.id.searchBox);
        searchBox.setAdapter(adapter);
        searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String exercise = searchBox.getAdapter().getItem(position).toString();
                searchBox.setText(exercise);
                createDialog(exercise);
            }
        });
        Button nextButton = (Button)findViewById(R.id.toCalendarButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiternityInstance instance = (FiternityInstance)getApplication();
                instance.viewCalendar();
            }
        });
    }

    private void createDialog(String exercise) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(exercise);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.exp_level_setter_fragment, null);
        builder.setView(dialogLayout);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final GridLayout exercises = (GridLayout)findViewById(R.id.exercises);
                final LinearLayout ll = new LinearLayout(ExerciseActivity.this);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                TextView exercise = new TextView(ExerciseActivity.this);
                exercise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String exerciseName = ((TextView)v).getText().toString();
                        createDialog(exerciseName);
                    }
                });
                ll.addView(exercise);
                Button button = new Button(ExerciseActivity.this);
                button.setText("x");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exercises.removeView(ll);
                    }
                });
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

    public String[] getExercises() {

        return new String[] {"swimming", "tennis", "baseball", "football", "soccer", "kendo", "karate"};
    }
}