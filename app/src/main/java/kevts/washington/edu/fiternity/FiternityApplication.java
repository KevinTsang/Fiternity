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
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kevin on 3/31/15.
 */
public class FiternityApplication extends Application {

    private static final String TAG = "FiternityApplication";
    private ParseUser parseUser;
    private static FiternityApplication instance;
    private static Set<ParseObject> exerciseSet;
    private HashSet<String> friendIds;

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
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Successfully subscribed to the broadcast channel.");
                } else {
                    Log.e(TAG, "Failed to subscribe for push notifications.");
                }
            }
        });
        exerciseSet = new HashSet<>();
        friendIds = new HashSet<>();
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
        if (AccessToken.getCurrentAccessToken() != null) {
            GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                    if (user != null) {
                        try {
                            parseUser.put("facebookId", user.get("id").toString());
                            ParseInstallation.getCurrentInstallation().put("facebookId", user.get("id"));
                            ParseInstallation.getCurrentInstallation().saveInBackground();
                            parseUser.setEmail(user.get("email").toString());
                            parseUser.put("name", user.get("name").toString());
                        } catch (JSONException e) {
                            Log.e(TAG, "Parsing the JSON object failed.");
                        }
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

    public Set<ParseObject> getExercises() {
        List<ParseObject> exercisesPointers = parseUser.getList("Exercise");
        if (exercisesPointers != null) {
            for (ParseObject exercisePointer : exercisesPointers) {
                ParseObject converter = convertPointerToObjectExercise(exercisePointer);
                exerciseSet.add(converter);
            }
        }
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
                                    parseUser.addUnique("FriendsList", personAndId.getString("id"));
                                }
                            } catch (JSONException jsone) {
                                Log.e(TAG, "JSON did not parse correctly when getting the name and ID from Facebook");
                            }
                            parseUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                Log.i(TAG, "Saved friend IDs successfully!");
                                }
                            });
                        }
                    }).executeAsync();
        }
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
            List<ParseObject> existingEvents = parseUser.getList("event");
            boolean eventExists = false;
            if (existingEvents != null) {
                for (ParseObject event : existingEvents) {
                    if (event.getLong("startDate") == startMillis && event.getLong("endDate") == endMillis) {
                        eventExists = true;
                    }
                }
            }
            if (!eventExists) {
                ParseObject freeEvent = new ParseObject("FreeEvent");
                freeEvent.put("startDate", startMillis);
                freeEvent.put("endDate", endMillis);
                try {
                    freeEvent.put("exercises", cursor.getString(2));
                    CNames[i] = cursor.getString(1);
                    freeEvent.save();
                } catch (IllegalStateException ise) {
                    Log.i(TAG, "Location or description/exercises not specified");
                } catch (ParseException pe) {
                    Log.e(TAG, "Event did not save properly.");
                }
                cursor.moveToNext();
                parseUser.addUnique("event", freeEvent);
            }
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
        List<String> friendsList = parseUser.getList("FriendsList");
        for (final String friendId : friendsList) {
            ParseQuery<ParseUser> friendQuery = ParseUser.getQuery();
            // NEED TO CHANGE THIS FOR NON-FACEBOOK USERS
            friendQuery.whereEqualTo("facebookId", friendId);
            try {
                List<ParseUser> parseUsers = friendQuery.find();
                for (ParseObject friend : parseUsers) {
                    List<ParseObject> friendPointerEvents = friend.getList("event");
                    if (friendPointerEvents != null) {
                        List<ParseObject> friendEvents = new ArrayList<>();
                        for (ParseObject pointerEvent : friendPointerEvents) {
                            friendEvents.add(convertPointerToObjectEvent(pointerEvent));
                        }
                        List<ParseObject> events = getMatchingSchedule(friendEvents);
                        if (events.size() > 0) {
                            friendIds.add(friendId);
                        }
                    }
                }
            } catch (ParseException pe) {
                Log.e(TAG, "Failed to add friends to local friend list");
            }
        }
    }

// Given 2 users' schedules compare them and return a matching schedule here
    public List<ParseObject> getMatchingSchedule(List<ParseObject> otherSchedule) {
        List<ParseObject> commonSchedule = new ArrayList<>();
        List<ParseObject> userSchedule = parseUser.getList("event");
        for (int i = 0; i < userSchedule.size(); i++) {
            ParseObject pointer = userSchedule.get(i);
            userSchedule.remove(i);
            userSchedule.add(i, convertPointerToObjectEvent(pointer));
        }
        int userScheduleIndex = 0;
        int otherScheduleIndex = 0;
        if (userSchedule != null && otherSchedule != null) {
            while (userScheduleIndex < userSchedule.size() && otherScheduleIndex < otherSchedule.size()) {
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
                    userScheduleIndex++;
                    otherScheduleIndex++;
                    commonSchedule.add(event);
                }
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

    public HashSet<String> getFriendIds() {
        return friendIds;
    }

    public ParseObject convertPointerToObjectEvent(ParseObject pointer) {
        ParseQuery<ParseObject> pointerQuery = ParseQuery.getQuery("FreeEvent");
        try {
            ParseObject obj = pointerQuery.get(pointer.getObjectId());
            return obj;
        } catch(ParseException pe) {
            Log.e(TAG, "Not a valid objectID");
        }
        return null;
    }
    public ParseObject convertPointerToObjectExercise(ParseObject pointer) {
        ParseQuery<ParseObject> pointerQuery = ParseQuery.getQuery("Exercise");
        try {
            ParseObject obj = pointerQuery.get(pointer.getObjectId());
            return obj;
        } catch(ParseException pe) {
            Log.e(TAG, "Not a valid objectID");
        }
        return null;
    }
}
