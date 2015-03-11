package kevts.washington.edu.fiternity;

import android.app.Application;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by kevin on 3/7/15.
 */
public class FiternityInstance extends Application /*implements UserDocInterface*/ {

    private static final String[] projection = new String[] {
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.NAME,
            CalendarContract.Calendars.ACCOUNT_NAME,
            CalendarContract.Calendars.ACCOUNT_TYPE
    };

    private static final String TAG = "FiternityInstance";
    private static String PARSE_APPLICATION_ID;
    private static String PARSE_CLIENT_KEY;
    private static FiternityInstance instance;
    private ParseUser user;
    private ParseUser otherUser; // take this out later.

    public FiternityInstance() {
        if (instance == null) {
            instance = this;
        } else {
            throw new RuntimeException("Error with singleton");
        }
    }

    public static FiternityInstance instance() {
        return instance;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(ParseUser.class);
        ParseObject.registerSubclass(Feedback.class);
        PARSE_APPLICATION_ID = getString(R.string.parse_application_id);
        PARSE_CLIENT_KEY = getString(R.string.parse_client_key);
        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
        // WARNING: THIS IS MOST LIKELY A PRIVATE KEY.
        // TAKE THIS OUT LATER, POTENTIALLY.
        user = FakeData.createAnnie().userToParseUser();
        Log.d("User created:", user.get("name").toString());
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
//                    Intent signInIntent = new Intent(getApplicationContext(), FiternityLogin.class);
//                    signInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(signInIntent);
                }
            }
        });
        createCalendar();
    }

    public ParseUser getUser() {
        return user;
    }
    public ParseUser getOtherUser() {
        return otherUser;
    }
    public void setOtherUser(ParseUser u) {
        otherUser = u;
    }

    public void setUser(ParseUser parseUser) {
        user = parseUser;
    }

    public void createCalendar() {
        // Look into CalendarContract
//        Uri calUri = Calendars.CONTENT_URI;
//        ContentValues cv = new ContentValues();
//        cv.put(Calendars.ACCOUNT_NAME, user.getUsername());
//        cv.put(Calendars.NAME, R.string.app_name);
//        cv.put(Calendars.CALENDAR_DISPLAY_NAME, "Fiternity Calendar");
//        cv.put(Calendars.CALENDAR_COLOR, "#2095F2"); // REPLACE WITH REAL COLOR
//        cv.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_OWNER);
//        cv.put(Calendars.OWNER_ACCOUNT, true);
//        cv.put(Calendars.VISIBLE, 1);
//        cv.put(Calendars.SYNC_EVENTS, 1);
//
//        calUri = calUri.buildUpon()
//                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
//                .appendQueryParameter(Calendars.ACCOUNT_NAME, user.getUsername())
//                .appendQueryParameter(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
//                .build();
//        Uri result = this.getContentResolver().insert(calUri, cv);
    }

    private long getCalendarId() {
        String[] selectParameters = new String[] {Calendars._ID};
        String selection = Calendars.ACCOUNT_NAME + " = ? " +
                Calendars.ACCOUNT_TYPE + " = ? ";
        String[] selArgs = new String[] { user.getUsername() };
        Cursor cursor = getContentResolver().query(Calendars.CONTENT_URI,
                projection, selection, selArgs, null);
        if (cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        return -1;
    }

    public void createEvent() {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(Events.CONTENT_URI)
                .putExtra(Events.TITLE, "Fiternity")
                .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
                .putExtra(Intent.EXTRA_EMAIL, user.getEmail());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);  // replace with startActivityForResult
    }

    public void editEvent() {
        long eventId = 1; // find a way to get the event ID they want to edit and put it here
        Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, eventId);
        Intent intent = new Intent(Intent.ACTION_EDIT).setData(uri).putExtra(Events.TITLE, "Fiternity");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent); // replace with startActivityForResult
    }

    // NEED TO COPY AND PASTE THIS CODE INTO EVERYWHERE THAT USES IT RATHER THAN CALLING IT HERE
    public void viewCalendar() {
        long startMillis = Calendar.getInstance().getTimeInMillis();
        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, startMillis);
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent); // replace with startActivityForResult
    }

    /*
    public int getUserId() {

    }

    public void setUserId(int userId) {

    }
    public String getName() {

    }
    public void setName(String name) {

    }
    public int getAge() {

    }
    public void setAge(int age) {

    }
    public String getGender() {

    }
    public void setGender(String gender) {

    }
    public boolean getGenderPreference() {

    }
    public void setSameGenderPreference(boolean preference) {

    }
    public String getEmail() {

    }
    public void setEmail(String email) {

    }
    public String getPhoneNumber() {

    }
    public void setPhoneNumber(String phoneNumber) {

    }
    public int getZipCode() {

    }
    public void setZipCode(int zipCode) {

    }
    public ArrayList<Exercise> getActivities() {

    }

    public void addActivity() {

    }*/
}
