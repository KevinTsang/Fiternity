package kevts.washington.edu.fiternity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.ParseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    private ParseUser user = FiternityApplication.getInstance().getParseUser();

    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        setUpAgeLimitsAndGenderChoices(rootView);
        fillFieldsWithUserData(rootView);
        Button button = (Button)rootView.findViewById(R.id.toExerciseButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save profile here
                if (validInput()) {
                    Log.d("valid input", " true");
                    // Insert relevant text fields here
                    user.saveInBackground();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new ExerciseFragment())
                            .commit();
                } else {
                    Toast.makeText(getActivity(), "Please fill out the forms correctly", Toast.LENGTH_SHORT).show();
                    Log.d("valid input", " false");
                }
            }
        });
        return rootView;
    }

    private void fillFieldsWithUserData(View rootView) {
        EditText userName = (EditText)rootView.findViewById(R.id.user_name);
        EditText userEmail = (EditText)rootView.findViewById(R.id.user_email);
        EditText userPhone = (EditText)rootView.findViewById(R.id.user_phone_number);
        EditText userZip = (EditText)rootView.findViewById(R.id.user_zip_code);
        NumberPicker agePicker = (NumberPicker)rootView.findViewById(R.id.user_age);
        Spinner genderSpinner = (Spinner)rootView.findViewById(R.id.user_gender_selector);
        CheckBox genderPreference = (CheckBox)rootView.findViewById(R.id.same_gender_toggle_button);

        if (user.get("first_name") != null && user.get("last_name") != null)
            userName.setText(user.get("first_name") + " " + user.get("last_name"));
        if (user.getEmail() != null)
            userEmail.setText(user.getEmail());

//        user.setPhoneNumber(userPhone.getText().toString());
//        user.setZipCode(Integer.parseInt(userZip.getText().toString()));
//        user.setAge(agePicker.getValue());
//        user.setGender(genderSpinner.getSelectedItem().toString().charAt(0));
//        user.setSameGenderPreference(genderPreference.isChecked());
    }

    private void setUpAgeLimitsAndGenderChoices(View rootView) {
        NumberPicker agePicker = (NumberPicker)rootView.findViewById(R.id.user_age);
        agePicker.setMinValue(18);
        agePicker.setMaxValue(99);

        Spinner genderSpinner = (Spinner)rootView.findViewById(R.id.user_gender_selector);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, new String[] {"M", "F"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
    }

    private boolean validInput() {
        boolean valid = true;
        EditText userName = (EditText)getActivity().findViewById(R.id.user_name);
        EditText userEmail = (EditText)getActivity().findViewById(R.id.user_email);
        EditText userPhone = (EditText)getActivity().findViewById(R.id.user_phone_number);
        EditText userZip = (EditText)getActivity().findViewById(R.id.user_zip_code);
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
}
