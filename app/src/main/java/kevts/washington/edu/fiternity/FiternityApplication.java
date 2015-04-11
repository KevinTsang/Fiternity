package kevts.washington.edu.fiternity;

import android.app.Application;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kevin on 3/31/15.
 */
public class FiternityApplication extends Application {

    private static final String TAG = "FiternityApplication";
    private ParseUser parseUser;
    private static FiternityApplication instance;

    public FiternityApplication() {
        if (instance == null)
            instance = this;
        else throw new RuntimeException("There cannot be multiple duplicates of this app running at once.");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        Parse.initialize(this);
        ParseFacebookUtils.initialize(this);
    }

    public static FiternityApplication getInstance() {
        return instance;
    }

    public void setParseUser(ParseUser user) {
        parseUser = user;
    }

    public ParseUser getParseUser() {
        return parseUser;
    }

    public void signUpProcess() {
        // TODO Implement grabbing data from the Facebook login and filling it in here
        if (AccessToken.getCurrentAccessToken() != null) {
            GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                    if (user != null) {
                        try {
                            parseUser.setUsername(user.get("id").toString());
                            parseUser.setEmail(user.get("email").toString());
                            parseUser.put("first_name", user.get("first_name").toString());
                            parseUser.put("last_name", user.get("last_name").toString());
                        } catch (JSONException e) {
                            Log.e(TAG, "Parsing the JSON object failed.");
                        }
                        parseUser.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.i(TAG, "Saved data to cloud!");
                            }
                        });
                        parseUser.saveInBackground();
                    }
                }
            }).executeAsync();
        }
    }

    public void saveProfile() {

    }

    public void loadProfile() {

    }
}
