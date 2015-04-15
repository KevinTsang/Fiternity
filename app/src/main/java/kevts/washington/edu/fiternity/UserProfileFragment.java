package kevts.washington.edu.fiternity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.ParseUser;

import java.net.URISyntaxException;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    private ParseUser user;
    private LayoutInflater layoutInflater;
    private View rootView;
    private EditText userName;
    private ImageView profilePic;
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
        layoutInflater = inflater;
        rootView = layoutInflater.inflate(R.layout.fragment_profile, container, false);
        user = FiternityApplication.getInstance().getParseUser();
        userName = (EditText)rootView.findViewById(R.id.user_name);
        profilePic = (ImageView)rootView.findViewById(R.id.profile_picture);
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
                    user.put("zip", Integer.parseInt(userZip.getText().toString()));
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

    private void setUpAgeRange() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogLayout = layoutInflater.inflate(R.layout.fragment_match_age_range, null);
        builder.setTitle("Please pick a preferred age range.");
        // set the values of the numberpickers here from saved data
        builder.setView(dialogLayout);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user.put("minAge", minAgePicker.getValue());
                user.put("maxAge", maxAgePicker.getValue());
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

    private void fillFieldsWithUserData(View rootView) {

        if (user.getString("first_name") != null && user.getString("last_name") != null)
            userName.setText(user.getString("first_name") + " " + user.getString("last_name"));
        if (user.getEmail() != null)
            userEmail.setText(user.getEmail());
        if (user.getString("phone") != null)
            userPhone.setText(user.get("phone").toString());
        if (user.getInt("zip") != 0)
            userZip.setText(user.getInt("zip") + "");
        if (user.getInt("age") != 0)
            agePicker.setValue(user.getInt("age"));
        if (user.getString("gender") != null) {
            if (user.getString("gender").equals("M"))
                genderSpinner.setSelection(0);
            else genderSpinner.setSelection(1);
        }
        genderPreference.setChecked(user.getBoolean("genderPreference"));
        profilePic.setImageURI(Uri.parse("https://graph.facebook.com/" + user.getUsername() + "/picture?type=large"));
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

        if (userZip.getText().length() == 0)
            valid = false;
        else {
            try {
                Integer.parseInt(userZip.getText().toString());
            } catch (NumberFormatException e) {
                valid = false;
            }
        }
        return valid;
    }
}
