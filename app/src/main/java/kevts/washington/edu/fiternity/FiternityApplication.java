package kevts.washington.edu.fiternity;

import android.app.Application;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.CalendarContract;
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
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by kevin on 3/31/15.
 */
public class FiternityApplication extends Application {

    private static final String TAG = "FiternityApplication";
    private static ParseUser parseUser;
    private static FiternityApplication instance;
    private static Set<Exercise> exerciseSet;
    private ArrayList<Integer> friendIds;

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
        exerciseSet = new HashSet<>();
        friendIds = new ArrayList<>();
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
                        try {
                            parseUser.save();
                        } catch (ParseException e) {
                            Log.e(TAG, "Failed to get save parse user data.");
                        }
                    }
                }
            }).executeAsync();
        }
    }

    public void getFacebookProfilePicture() {
        if (AccessToken.getCurrentAccessToken() != null) {
            try {
                URL imageURL = new URL("https://graph.facebook.com/" + parseUser.getUsername() + "/picture?type=large");
                Bitmap profilePicture = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                parseUser.put("profilePicture", profilePicture);
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.i(TAG, "Profile picture successfully saved!");
                    }
                });
            } catch (MalformedURLException e) {
                Log.e(TAG, "ParseUser did not use Facebook to login");
            } catch (IOException e) {
                Log.e(TAG, "Failed to connect to Facebook to retrieve profile picture");
            }
        }
    }

    public Set<Exercise> getExercises() {
        return exerciseSet;
    }

    public void getFriends() {
        if (AccessToken.getCurrentAccessToken() != null) {
            GraphRequest.newMyFriendsRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONArrayCallback() {
                        @Override
                        public void onCompleted(JSONArray jsonArray, GraphResponse graphResponse) {
                            try {
                                jsonArray.toString();  // use this to get the ID then use it for querying times
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject personAndId = jsonArray.getJSONObject(i);
                                    personAndId.getString("name");
                                    personAndId.getString("id");
                                }
                            } catch (JSONException jsone) {
                                Log.e(TAG, "JSON did not parse correctly when getting the name and ID from Facebook");
                            }
                        }
                    }).executeAsync();
        }
    }

    public void viewSchedule() {
        long startMillis = Calendar.getInstance().getTimeInMillis();
        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, startMillis);
        Intent calendarIntent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
        startActivity(calendarIntent);
    }

    public void createEvent() {
        Calendar beginTime = Calendar.getInstance();
//        beginTime.set(); set a time here
        Calendar endTime = Calendar.getInstance();
//        endTime.set();  set a time here
        Intent createEventIntent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
//                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
//                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, "Fiternity")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Insert activity here")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "Insert location here")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE)
                .putExtra(Intent.EXTRA_EMAIL, "email address here, another email address here");
        startActivity(createEventIntent);
    }

    public void editEvent() {

    }

    public ArrayList<FreeEvent> readEvents() {
        Cursor cursor = getApplicationContext().getContentResolver()
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
            FreeEvent freeEvent = new FreeEvent(startDate, endDate);
            freeEvent.setDescription(cursor.getString(2));
            CNames[i] = cursor.getString(1);
            cursor.moveToNext();
            eventsList.add(freeEvent);
        }
        return eventsList;
    }
}
