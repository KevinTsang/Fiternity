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

    private ParseUser user;
    private View rootView;
    private EditText userName;
    private EditText userEmail;
    private EditText userPhone;
    private EditText userZip;
    private NumberPicker agePicker;
    private Spinner genderSpinner;
    private CheckBox genderPreference;

    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        user = FiternityApplication.getInstance().getParseUser();
        userName = (EditText)rootView.findViewById(R.id.user_name);
        userEmail = (EditText)rootView.findViewById(R.id.user_email);
        userPhone = (EditText)rootView.findViewById(R.id.user_phone_number);
        userZip = (EditText)rootView.findViewById(R.id.user_zip_code);
        agePicker = (NumberPicker)rootView.findViewById(R.id.user_age);
        genderSpinner = (Spinner)rootView.findViewById(R.id.user_gender_selector);
        genderPreference = (CheckBox)rootView.findViewById(R.id.same_gender_toggle_button);

        setUpAgeLimitsAndGenderChoices(rootView);
        fillFieldsWithUserData(rootView);
        Button button = (Button)rootView.findViewById(R.id.toExerciseButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save profile here
                if (validInput()) {
                    Log.d("valid input", " true");
                    String[] nameField = userName.getText().toString().split(" ");
                    if (nameField.length == 2) {
                        user.put("first_name", nameField[0]);
                        user.put("last_name", nameField[1]);
                    } else if (nameField.length == 1) {
                        user.put("first_name", nameField[0]);
                    }
                    user.put("phone", userPhone.getText().toString());
                    user.put("zip", userZip.getText().toString());
                    user.put("age", agePicker.getValue());
                    user.put("gender", genderSpinner.getSelectedItem().toString());
                    user.put("genderPreference", genderPreference.isChecked());
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

        if (user.getString("first_name") != null && user.getString("last_name") != null)
            userName.setText(user.getString("first_name") + " " + user.getString("last_name"));
        if (user.getEmail() != null)
            userEmail.setText(user.getEmail());
        if (user.getString("phone") != null)
            userPhone.setText(user.get("phone").toString());
        if (user.get("zip") != null)
            userZip.setText((int)user.get("zip"));
        if (user.get("age") != null)
            agePicker.setValue((int)user.get("age"));
        if (user.getString("gender") != null) {
            if (user.getString("gender").equals("M"))
                genderSpinner.setSelection(0);
            else genderSpinner.setSelection(1);
        }
        genderPreference.setChecked(user.getBoolean("genderPreference"));
    }

    private void setUpAgeLimitsAndGenderChoices(View rootView) {
        NumberPicker agePicker = (NumberPicker)rootView.findViewById(R.id.user_age);
        agePicker.setMinValue(15);
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
//        if (userEmail.getText().length() == 0) {
//            valid = false;
//        }
//        if (userPhone.getText().length() == 0) {
//            valid = false;
//        }

        if (userZip.getText().length() == 0) {
            valid = false;
        }
        return valid;
    }
}
