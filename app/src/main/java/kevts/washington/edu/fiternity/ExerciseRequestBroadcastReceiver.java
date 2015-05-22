package kevts.washington.edu.fiternity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
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
            String action = intent.getAction();
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            // replace openAppIntent with event modification
            Intent openAppIntent = new Intent(context, MatchesActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MatchesActivity.class);
            stackBuilder.addNextIntent(openAppIntent);
            PendingIntent acceptIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent declineIntent = null;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_login)
                    .setContentTitle("Fiternity")
                    .setContentText(intent.getDataString())
                    .addAction(android.R.drawable.ic_input_add, "Accept", acceptIntent)
                    .addAction(android.R.drawable.ic_input_delete, "Decline", declineIntent);
            notificationManager.notify(notificationId, notificationBuilder.build());
        } catch (JSONException e) {
            Log.e("ReceiverIssue", "JSONException: " + e.getMessage());
        }
    }
}
