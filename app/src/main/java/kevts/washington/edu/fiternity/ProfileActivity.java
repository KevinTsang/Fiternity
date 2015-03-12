package kevts.washington.edu.fiternity;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
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
import android.widget.Toast;
import android.widget.ToggleButton;


public class ProfileActivity extends Activity {

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
        final FiternityInstance instance = (FiternityInstance)getApplication();

        Button button = (Button)findViewById(R.id.toExerciseButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save profile here
                if (validInput()) {
                    Log.d("valid input", " true");
                    User user = instance.getUser();
                    setUser(user);
                    Intent intent = new Intent(ProfileActivity.this, ExerciseActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ProfileActivity.this, "Please fill out the forms correctly", Toast.LENGTH_SHORT).show();
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

    private void setUser(User user) {
        EditText userName = (EditText) findViewById(R.id.user_name);
        EditText userEmail = (EditText) findViewById(R.id.user_email);
        EditText userPhone = (EditText) findViewById(R.id.user_phone_number);
        EditText userZip = (EditText) findViewById(R.id.user_zip_code);
        NumberPicker agePicker = (NumberPicker)findViewById(R.id.user_age);
        Spinner genderSpinner = (Spinner)findViewById(R.id.user_gender_selector);
        ToggleButton genderPreference = (ToggleButton) findViewById(R.id.same_gender_toggle_button);

        user.setName(userName.getText().toString());
        user.setEmail(userEmail.getText().toString());
        user.setPhoneNumber(userPhone.getText().toString());
//        user.setZipCode(Integer.parseInt(userZip.getText().toString()));
        user.setAge(agePicker.getValue());
        user.setGender(genderSpinner.getSelectedItem().toString().charAt(0));
        user.setSameGenderPreference(genderPreference.isChecked());
    }

    private boolean validInput() {
        boolean valid = true;
        EditText userName = (EditText) findViewById(R.id.user_name);
        EditText userEmail = (EditText) findViewById(R.id.user_email);
        EditText userPhone = (EditText) findViewById(R.id.user_phone_number);
        EditText userZip = (EditText) findViewById(R.id.user_zip_code);
        if (userName.getText().length() == 0) {
            valid = false;
        }
        if (userEmail.getText().length() == 0) {
            valid = false;
        }
        if (userPhone.getText().length() == 0) {
            valid = false;
        }
        if (userZip.getText().length() == 0) {
            valid = false;
        }
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
