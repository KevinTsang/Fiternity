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

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kevin on 5/19/15.
 */
public class ExerciseRequestBroadcastReceiver extends ParsePushBroadcastReceiver {
    @Override
    protected void onPushReceive(Context context, Intent intent) {
        int notificationId = 51;
        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            // replace openAppIntent with event modification
//            Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, );
//            Intent modifyEventIntent = new Intent(Intent.ACTION_EDIT)
//                    .setData(uri)
//                    .putExtra();




            Intent openAppIntent = new Intent(context, MatchesActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MatchesActivity.class);
            stackBuilder.addNextIntent(openAppIntent);
            // TODO send push notification back when event has been accepted and set alarm
            PendingIntent declineIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_login)
                    .setContentTitle("Fiternity")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(json.get("alert").toString()))
                    // TODO Need to replace the icons for better ones below
                    .addAction(android.R.drawable.ic_input_add, "Accept", declineIntent)
                    .addAction(android.R.drawable.ic_input_delete, "Decline", declineIntent);
            notificationManager.notify(notificationId, notificationBuilder.build());
        } catch (JSONException e) {
            Log.e("ReceiverIssue", "JSONException: " + e.getMessage());
        }
    }

    // Sets the alarm manager to go to the feedback activity
    private void setAlarmManager(Context context, long endDate) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent feedbackIntent = new Intent(context, FeedbackActivity.class);
        PendingIntent feedbackPendingIntent = PendingIntent.getActivity(context, 101, feedbackIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC, endDate, feedbackPendingIntent);
    }
}
