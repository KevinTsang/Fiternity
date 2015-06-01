package kevts.washington.edu.fiternity;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by kevin on 5/19/15.
 */
public class ExerciseRequestBroadcastReceiver extends ParsePushBroadcastReceiver {

    private FiternityApplication instance;

    @Override
    public void onReceive(Context context, Intent intent) {
        instance = FiternityApplication.getInstance();
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent feedbackIntent = new Intent(context, FeedbackActivity.class);
        feedbackIntent.putExtra("facebookId", intent.getStringExtra("facebookId"));
        PendingIntent feedbackPendingIntent = PendingIntent.getActivity(context, 101, feedbackIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC, intent.getLongExtra("endDate", Calendar.getInstance().getTimeInMillis()), feedbackPendingIntent);
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", instance.getParseUser().getString("name"));
        params.put("facebookId", instance.getParseUser().getString("facebookId"));
        ParseCloud.callFunctionInBackground("pushResponse", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object o, ParseException e) {

            }
        });
//      TODO Modify calendar with the smaller event object here
//      Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, );
//      Intent modifyEventIntent = new Intent(Intent.ACTION_EDIT)
//      .setData(uri)
//      .putExtra();
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        int notificationId = 51;
        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            Intent openAppIntent = new Intent(context, ExerciseRequestBroadcastReceiver.class);
            openAppIntent.putExtra("endDate", json.getLong("requestEndDate"));
            openAppIntent.putExtra("facebookId", json.getString("matchId"));
            openAppIntent.putExtra("exercise", json.getString("exerciseName"));
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MatchesActivity.class);
            stackBuilder.addNextIntent(openAppIntent);
            // TODO send push notification back when event has been accepted and set alarm
            PendingIntent acceptIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent declineIntent = null;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_login)
                    .setContentTitle("Fiternity")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(json.get("alert").toString()))
                    // TODO Need to replace the icons for better ones below
                    .addAction(android.R.drawable.ic_input_add, "Accept", acceptIntent)
                    .addAction(android.R.drawable.ic_input_delete, "Decline", declineIntent);
            notificationManager.notify(notificationId, notificationBuilder.build());
        } catch (JSONException e) {
            Log.e("ReceiverIssue", "JSONException: " + e.getMessage());
        }
    }

    // Sets the alarm manager to go to the feedback activity
    private void setAlarmManager(Context context, long endDate) {
    }
}
