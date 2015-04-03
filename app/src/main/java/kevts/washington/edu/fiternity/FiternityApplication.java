package kevts.washington.edu.fiternity;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by kevin on 3/31/15.
 */
public class FiternityApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        Parse.initialize(this);
        ParseFacebookUtils.initialize(this);
    }
}
