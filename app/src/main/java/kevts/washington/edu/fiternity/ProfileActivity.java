package kevts.washington.edu.fiternity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;


public class ProfileActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Button menuButton = (Button)findViewById(R.id.profile_menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMenu = new Intent(ProfileActivity.this, MenuDrawer.class);
                startActivity(toMenu);
            }
        });
        populatePredefinedFields();
        Button button = (Button)findViewById(R.id.toExerciseButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save profile here
                if (validInput()) {
                    Log.d("valid input", " true");
                    Intent intent = new Intent(ProfileActivity.this, ExerciseActivity.class);
                    startActivity(intent);
                } else {
                    Log.d("valid input", " false");
                }
            }
        });
    }

    private void populatePredefinedFields() {
        NumberPicker agePicker = (NumberPicker)findViewById(R.id.user_age);
        agePicker.setMinValue(18);
        agePicker.setMaxValue(99);

        Spinner genderSpinner = (Spinner)findViewById(R.id.user_gender_selector);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new String[] {"M", "F"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
    }

    private boolean validInput() {
        boolean valid = true;
        NumberPicker agePicker = (NumberPicker)findViewById(R.id.user_age);
        Spinner genderSpinner = (Spinner)findViewById(R.id.user_gender_selector);
        EditText userName = (EditText) findViewById(R.id.user_name);
        EditText userEmail = (EditText) findViewById(R.id.user_email);
        EditText userPhone = (EditText) findViewById(R.id.user_phone_number);
        EditText userZip = (EditText) findViewById(R.id.user_zip_code);
//        if (agePicker.getValue()) {
//
//        }
        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
}
