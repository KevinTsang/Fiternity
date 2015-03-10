package kevts.washington.edu.fiternity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.app.LoaderManager.LoaderCallbacks;
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

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;


public class FiternityLogin extends FragmentActivity {

    private FacebookFragment facebookFragment;
    private static final String TAG = "FiternityLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiternity_login);
        final FiternityInstance instance = (FiternityInstance)getApplication();
        if (savedInstanceState == null) {
            facebookFragment = new FacebookFragment().newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, facebookFragment)
                    .commit();
        } else {
            facebookFragment = (FacebookFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.content);
        }
        LoginButton authButton = (LoginButton)findViewById(R.id.authButton);
        authButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseFacebookUtils.logIn(FiternityLogin.this, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser == null) {
                            Log.d(TAG, "User canceled Facebook Login");
                        } else if (parseUser.isNew()) {
                            Log.d(TAG, "User registered and logged in through Facebook!");
                            // Set user here
                            // put them through start up processes
                        } else {
                            Log.d(TAG, "User logged in through Facebook!");
                            instance.setUser(parseUser);
                            // Set user here
                        }
                    }
                });
            }
        });
//        authButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));
//        authButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Session session = Session.getActiveSession();
//                if (!session.isOpened() && !session.isClosed()) {
//                    session.openForRead(new Session.OpenRequest(FiternityLogin.this)
//                            .setPermissions(Arrays.asList("public_profile"))
//                            .setCallback(statusCallback));
//                } else {
//                    Session.openActiveSession(FiternityLogin.this, true, statusCallback);
//                    // might possibly need to add a permissions list?
//                }
//            }
//        });
        Button tempLogin = (Button)findViewById(R.id.email_sign_in_button);
        tempLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FiternityLogin.this, CalendarActivity.class);
                startActivity(intent);
            }
        });
    }

    private Session.StatusCallback statusCallback =
            new SessionStatusCallback();

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            // Respond to session state changes, ex: updating the view
        }
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
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode,data);
    }
}



