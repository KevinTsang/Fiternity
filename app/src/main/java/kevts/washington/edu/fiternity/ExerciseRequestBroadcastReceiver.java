package kevts.washington.edu.fiternity;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import java.util.Date;
import java.util.HashMap;

/**
 * Created by kevin on 5/19/15.
 */
public class ExerciseRequestBroadcastReceiver extends ParsePushBroadcastReceiver {

    public ExerciseRequestBroadcastReceiver() {

    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        int notificationId = 51;
        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            Intent openAppIntent = new Intent(context, ExerciseResponseBroadcastReceiver.class);
            if (json.has("requestStartDate")) {
                openAppIntent.putExtra("startDate", json.getLong("requestStartDate"));
            }
            if (json.has("requestEndDate")) {
                openAppIntent.putExtra("endDate", json.getLong("requestEndDate"));
            }
            if (json.has("matchId")) {
                openAppIntent.putExtra("facebookId", json.getString("matchId"));
            }
            if (json.has("exerciseName")) {
                openAppIntent.putExtra("exercise", json.getString("exerciseName"));
            }
            PendingIntent acceptIntent = PendingIntent.getBroadcast(context, 51, openAppIntent, PendingIntent.FLAG_ONE_SHOT);
            // TODO switch out this intent for one that isn't null
            PendingIntent declineIntent = null;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_login)
                    .setContentTitle("Fiternity")
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(json.get("alert").toString()))
                            // TODO Need to replace the icons for better ones below
                    .addAction(R.drawable.ic_done_white_36dp, "Accept", acceptIntent)
                    .addAction(R.drawable.ic_clear_white_36dp, "Decline", declineIntent);
            notificationManager.notify(notificationId, notificationBuilder.build());
        } catch (JSONException e) {
            Log.e("ReceiverIssue", "JSONException: " + e.getMessage());
        }
    }
}
