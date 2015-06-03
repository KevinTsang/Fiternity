package kevts.washington.edu.fiternity;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;


public class FiternityLogin extends Activity {

    private LoginButton loginButton;
    private static final String TAG = "FiternityLogin";
    private FiternityApplication instance = FiternityApplication.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiternity_login);
        if (AccessToken.getCurrentAccessToken() != null) {
            instance.setParseUser(ParseUser.getCurrentUser());
            try {
                ParseUser.getCurrentUser().fetchIfNeeded();
                ParseInstallation.getCurrentInstallation().put("facebookId", instance.getParseUser().get("facebookId"));
                ParseInstallation.getCurrentInstallation().save();
            } catch (ParseException pe) {
                Log.e(TAG, "Failed to update user from cloud.");
            }
            instance.getFriendEvents();
            Intent intent = new Intent(FiternityLogin.this, MatchesActivity.class);
            startActivity(intent);
        }
        Button emailSignInButton = (Button)findViewById(R.id.email_sign_in_button);
        emailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParseUser user = new ParseUser();
                String username = ((AutoCompleteTextView)findViewById(R.id.user_name_login)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();
                user.setUsername(username);
                user.setPassword(password);
                user.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser pu, ParseException e) {
                        FiternityApplication.getInstance().setParseUser(pu);
                        Log.i(TAG, "Logged in via non Facebook!");
//                        FiternityApplication.getInstance().getFriendEvents();
                        Intent intent = new Intent(FiternityLogin.this, MatchesActivity.class);
                        startActivity(intent);
                    }
                });
                try {
                    user.save();
                } catch (ParseException pe) {
                    Log.e(TAG, "Saving email and password from non-Facebook login failed");
                }
                FiternityApplication.getInstance().setParseUser(user);
                Intent intent = new Intent(FiternityLogin.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccessToken.getCurrentAccessToken() == null) {
                    ArrayList<String> permissions = new ArrayList<String>();
                    permissions.add("public_profile");
                    permissions.add("user_friends");
                    permissions.add("email");
                    //permissions.add("user_about_me");  TOO MUCH TROUBLE
                    //permissions.add("user_actions.fitness"); MAY IMPLEMENT LATER
                    //permissions.add("user_location"); ZIP CODE MAY BE SUFFICIENT
                    ParseFacebookUtils.logInWithReadPermissionsInBackground(FiternityLogin.this,
                            permissions, new LogInCallback() {
                                @Override
                                public void done(ParseUser parseUser, ParseException e) {
                                    if (parseUser == null) {
                                        // User canceled Facebook login
                                        Log.d(TAG, "User canceled Facebook login");
                                    } else if (parseUser.isNew()) {
                                        // create new parse user in cloud
                                        Log.d(TAG, "User signed up and logged in through Facebook!");
                                        FiternityApplication.getInstance().setParseUser(parseUser);
                                        FiternityApplication.getInstance().signUpProcess();
//                                        FiternityApplication.getInstance().getFacebookProfilePicture();
                                        FiternityApplication.getInstance().saveFriends();
                                        Intent intent = new Intent(FiternityLogin.this, UserProfileActivity.class);
                                        intent.putExtra("loadUserProfileFragment", true);
                                        startActivity(intent);
                                    } else {
                                        // parse user successfully logged in
                                        Log.d(TAG, "User logged in through Facebook!");
                                        FiternityApplication.getInstance().setParseUser(ParseUser.getCurrentUser());
                                        Intent intent = new Intent(FiternityLogin.this, MatchesActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                } else {
                    ParseUser user = FiternityApplication.getInstance().getParseUser();
                    user.logOutInBackground();
                }
            }
        });
        Button registerButton = (Button)findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FiternityLogin.this, EmailSignUpActivity.class);
                startActivity(intent);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}
