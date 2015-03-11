package kevts.washington.edu.fiternity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.app.Dialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AppEventsLogger;

import com.facebook.LoginActivity;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.security.Permission;
import java.util.Arrays;


public class FiternityLogin extends FragmentActivity {

//    private FacebookFragment facebookFragment;
    private static final String TAG = "FiternityLogin";
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiternity_login);
        final FiternityInstance instance = (FiternityInstance)getApplication();
        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null && ParseFacebookUtils.isLinked(currentUser)) {
            Intent intent = new Intent(FiternityLogin.this, FeedbackActivity.class);
            startActivity(intent);
        }

        LoginButton authButton = (LoginButton)findViewById(R.id.authButton);
        authButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(FiternityLogin.this, "", "Logging in...", true);

                ParseFacebookUtils.logIn(Arrays.asList("public_profile"),
                        FiternityLogin.this, new LogInCallback() {
                            @Override
                            public void done(ParseUser parseUser, ParseException e) {
                                progressDialog.dismiss();
                                if (parseUser == null) {
                                    Log.d(TAG, "User canceled Facebook Login");
                                } else if (parseUser.isNew()) {
                                    Log.d(TAG, "User registered and logged in through Facebook!");
                                    instance.setUser(parseUser);
                                    Intent intent = new Intent(FiternityLogin.this, ProfileActivity.class);
                                    startActivity(intent);
                                    // Set user here
                                    // put them through start up processes
                                    // fire intent to profile screen
                                } else {
                                    Log.d(TAG, "User logged in through Facebook!");
                                    instance.setUser(parseUser);
                                    Intent intent = new Intent(FiternityLogin.this, Matches.class);
                                    startActivity(intent);
                                    // Set user here
                                    // fire intent to matches screen
                                }
                            }
                        });
            }
        });

        Button tempLogin = (Button)findViewById(R.id.email_sign_in_button);
        tempLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // replace this with Cvetan's profile activity
                Intent intent = new Intent(FiternityLogin.this, Matches.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Logs 'app deactivate' AppEvent
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }
}



