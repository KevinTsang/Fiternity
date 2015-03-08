package kevts.washington.edu.fiternity;

import android.app.Application;
import android.content.Intent;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by kevin on 3/7/15.
 */
public class FiternityInstance extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "vdyKL0gcEtfovrPj4WIoLZFaIHa3PLlND8wTblJ6", "K6KtiuCuW5tmZjz6uehRdG7Lc9Sec1ddPvSWi4s2");
        // WARNING: THIS IS MOST LIKELY A PRIVATE KEY.
        // TAKE THIS OUT LATER, POTENTIALLY.
        ParseUser user = new ParseUser();
        user.setUsername("test");
        user.setPassword("password");
        user.setEmail("conscientiaexnihilo@mailinator.com");
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // allow them to go directly to matches
                } else {
                    // push the user to login screen
                    Intent signInIntent = new Intent(getApplicationContext(), FiternityLogin.class);
                    startActivity(signInIntent);
                }
            }
        });
    }
}
