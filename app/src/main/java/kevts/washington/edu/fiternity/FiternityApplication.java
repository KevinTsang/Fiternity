package kevts.washington.edu.fiternity;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

/**
 * Created by kevin on 3/31/15.
 */
public class FiternityApplication extends Application {

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
}
