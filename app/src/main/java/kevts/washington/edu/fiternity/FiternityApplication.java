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
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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
import java.util.List;
import java.util.Set;

/**
 * Created by kevin on 3/31/15.
 */
public class FiternityApplication extends Application {

    private static final String TAG = "FiternityApplication";
    private static ParseUser parseUser;
    private static FiternityApplication instance;
    private static Set<Exercise> exerciseSet;
    private ArrayList<Long> friendIds;

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
                        try {
                            parseUser.signUp();
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

    public void saveFriends() {
        if (AccessToken.getCurrentAccessToken() != null) {
            GraphRequest.newMyFriendsRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONArrayCallback() {
                        @Override
                        public void onCompleted(JSONArray jsonArray, GraphResponse graphResponse) {
                            try {  // use this to get the ID then use it for querying times
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject personAndId = jsonArray.getJSONObject(i);
                                    ParseObject friendsList = new ParseObject("FriendsList");
                                    friendsList.put("name", personAndId.getString("name"));
                                    friendsList.put("friendId", personAndId.getString("id"));
                                    parseUser.addUnique("FriendsList", friendsList);
                                }
                            } catch (JSONException jsone) {
                                Log.e(TAG, "JSON did not parse correctly when getting the name and ID from Facebook");
                            }
                        }
                    }).executeAsync();
            parseUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Log.i(TAG, "Saved friend IDs successfully!");
                }
            });
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

    public void readEvents() {
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

        for (int i = 0; i < CNames.length; i++) {
            long startMillis = Long.parseLong(cursor.getString(3));
            long endMillis = Long.parseLong(cursor.getString(4));
//            Date startDate = new Date(startMillis);
//            Date endDate = new Date(endMillis);
            ParseObject freeEvent = new ParseObject("FreeEvent");
            freeEvent.put("startDate", startMillis);
            freeEvent.put("endDate", endMillis);
            try {
                freeEvent.put("exercises", cursor.getString(2));
                CNames[i] = cursor.getString(1);
            } catch (IllegalStateException ise) {
                Log.i(TAG, "Location or description/exercises not specified");
            }
            cursor.moveToNext();
            parseUser.addUnique("event", freeEvent);
        }

        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i(TAG, "Saved events to cloud successfully.");
            }
        });
    }

// Goes through every friend with this person and checks if they have an overlapping schedule
    public void getFriendEvents() {
        List<ParseObject> friendsList = parseUser.getList("FriendsList");
        for (final ParseObject friendId : friendsList) {
            ParseQuery<ParseObject> friendQuery = ParseQuery.getQuery("ParseUser");
            // NEED TO CHANGE THIS FOR NON-FACEBOOK USERS
            friendQuery.whereEqualTo("username", friendId.get("id"));
            friendQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    for (ParseObject friend : parseObjects) {
                        List<ParseObject> friendEvents = friend.getList("event");
                        List<ParseObject> events = getMatchingSchedule(friendEvents);
                        if (events.size() > 0) {
                            friendIds.add(Long.parseLong(friendId.get("id").toString()));
                        }
                    }
                }
            });
        }
    }

// Given 2 users' schedules compare them and return a matching schedule here
    public List<ParseObject> getMatchingSchedule(List<ParseObject> otherSchedule) {
        List<ParseObject> commonSchedule = new ArrayList<>();
        List<ParseObject> userSchedule = parseUser.getList("event");

        int userScheduleIndex = 0;
        int otherScheduleIndex = 0;
        while (userScheduleIndex < userSchedule.size() || otherScheduleIndex < otherSchedule.size()) {
            ParseObject userEvent = userSchedule.get(userScheduleIndex);
            ParseObject otherUserEvent = otherSchedule.get(otherScheduleIndex);
            ParseObject event = getSmallerEvent(userEvent, otherUserEvent);
            if (event == null) {
                if (userEvent.getLong("endDate") < otherUserEvent.getLong("startDate")) {
                    userScheduleIndex++;
                } else {
                    otherScheduleIndex++;
                }
            } else {
                commonSchedule.add(event);
            }
        }

        return commonSchedule;
    }

// Takes two events and returns the smaller match between the two, null if there's no overlap
    public ParseObject getSmallerEvent(ParseObject userEvent, ParseObject otherEvent) {
        ParseObject event = new ParseObject("FreeEvent");
        long userEventStartDate = userEvent.getLong("startDate");
        long userEventEndDate = userEvent.getLong("endDate");
        long otherEventStartDate = otherEvent.getLong("startDate");
        long otherEventEndDate = otherEvent.getLong("endDate");

        if (userEventEndDate > otherEventStartDate && userEventEndDate <= otherEventEndDate) {
            if (userEventStartDate < otherEventStartDate)
                event.put("startDate", otherEventStartDate);
            else event.put("startDate", userEventStartDate);
            event.put("endDate", userEventEndDate);
        } else if (otherEventEndDate > userEventStartDate && otherEventEndDate <= userEventEndDate) {
            if (otherEventStartDate < userEventStartDate)
                event.put("startDate", userEventStartDate);
            else event.put("startDate", otherEventStartDate);
            event.put("endDate", otherEventEndDate);
        } else return null;
        return event;
    }
}
