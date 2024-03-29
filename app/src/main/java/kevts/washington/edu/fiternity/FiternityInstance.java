package kevts.washington.edu.fiternity;

import android.app.Application;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
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
import java.util.Date;
import java.util.List;

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
    private User user;
    private User otherUser; // take this out later.
    private List<FreeEvent> requestedFreeEvents;

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
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Feedback.class);
        PARSE_APPLICATION_ID = getString(R.string.parse_application_id);
        PARSE_CLIENT_KEY = getString(R.string.parse_client_key);
        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
        // WARNING: THIS IS MOST LIKELY A PRIVATE KEY.
        // TAKE THIS OUT LATER, POTENTIALLY.
        if (user == null) {
            user = FakeData.createMarshall();
            user.setUsername("MarshallTest");
            user.setPassword("password");
            user.setEmail("fake@email.com");
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        // allow them to go directly to matches
                    } else {
                        // push the user to login screen
                        Intent signInIntent = new Intent(getApplicationContext(), FiternityLogin.class);
                        signInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(signInIntent);
                    }
                }
            });
        }
        requestedFreeEvents = new ArrayList<FreeEvent>();
        createCalendar();
    }

    public User getUser() {
        return user;
    }
    public void setUser() {

    }
    public ParseUser getOtherUser() {
        return otherUser;
    }
    public void setOtherUser(User u) {
        otherUser = u;
    }

    public void setUser(User user) {
        this.user = user;
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

    public long getCalendarId() {
        String[] selectParameters = new String[] {Calendars._ID};
        String selection = Calendars.ACCOUNT_NAME + " = ? " +
                Calendars.ACCOUNT_TYPE + " = ? ";
        String[] selArgs = new String[] { user.getUsername(), CalendarContract.ACCOUNT_TYPE_LOCAL };
        Cursor cursor = getContentResolver().query(Calendars.CONTENT_URI,
                selectParameters, selection, selArgs, null);
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

    public ArrayList<FreeEvent> getEvents(Context context, User user) {
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation" }, "title=?",
                        new String[] {"Fiternity"}, null);

        // explanation: first String[] is the columns to return
        // second argument (String) is filter by a column name
        // third is what values they're supposed to have
        // fourth is the order by clause
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        ArrayList<FreeEvent> eventsList = new ArrayList<FreeEvent>();
        for (int i = 0; i < CNames.length; i++) {
            long startMillis = Long.parseLong(cursor.getString(3));
            long endMillis = Long.parseLong(cursor.getString(4));
            Date startDate = new Date(startMillis);
            Date endDate = new Date(endMillis);
            FreeEvent freeEvent = new FreeEvent(user, startDate, endDate);
            freeEvent.setDescription(cursor.getString(2));
            CNames[i] = cursor.getString(1);
            cursor.moveToNext();

        }
        return eventsList;
    }

    public void addRequestedFreeEvent(FreeEvent freeEvent){
        if(!this.requestedFreeEvents.contains(freeEvent)){
            this.requestedFreeEvents.add(freeEvent);
        }
    }

    public void removeRequestedFreeEvent(FreeEvent freeEvent){

        Log.i("rmReqFreeEvent", "before size ="+this.requestedFreeEvents.size());

        this.requestedFreeEvents.remove(freeEvent);
        Log.i("rmReqFreeEvent", "after size ="+this.requestedFreeEvents.size());

    }

    public List<FreeEvent> getRequestedFreeEvents(){ return this.requestedFreeEvents;}

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
