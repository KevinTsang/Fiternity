package kevts.washington.edu.fiternity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.Calendar;
import java.util.HashMap;

public class ExerciseResponseBroadcastReceiver extends BroadcastReceiver {
    public ExerciseResponseBroadcastReceiver() {

    }

    private FiternityApplication instance;
    private Context context;

    public Context getContext() {
        return context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        instance = FiternityApplication.getInstance();
        this.context = context;
        // This section sets the alarm manager for the feedback activity
        long endDate = intent.getLongExtra("endDate", Calendar.getInstance().getTimeInMillis());
        String facebookId = intent.getStringExtra("facebookId");
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent feedbackIntent = new Intent(context, FeedbackActivity.class);
        feedbackIntent.putExtra("facebookId", facebookId);
        PendingIntent feedbackPendingIntent = PendingIntent.getActivity(context, 101, feedbackIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC, endDate, feedbackPendingIntent);

        // This section sends a push notification back with an accept message.
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", instance.getParseUser().getString("name"));
        params.put("facebookId", facebookId);
        ParseCloud.callFunctionInBackground("pushResponse", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object o, ParseException e) {

            }
        });

        // This section modifies the user's calendar with the new event
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(endDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startRange = calendar.getTimeInMillis();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(endDate + 86400000);
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);
        long endRange = calendar2.getTimeInMillis();

        long startDate = intent.getLongExtra("startDate", Calendar.getInstance().getTimeInMillis());
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[]{"calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation"}, "title=? AND dtstart>=? AND dtend<=?",
                        new String[]{"Fiternity", startRange + "", endRange + ""}, null);

        cursor.moveToLast();
        String cNames[] = new String[cursor.getCount()];
        for (int i = cNames.length - 1; i >= 0; i--) {
            long eventStartTime = Long.parseLong(cursor.getString(3));
            long eventEndTime = Long.parseLong(cursor.getString(4));
            if (startDate >= eventStartTime && endDate <= eventEndTime) {
                Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(cursor.getString(0)));
                Intent modifyEventIntent = new Intent(Intent.ACTION_EDIT)
                        .setData(uri)
                        .putExtra(CalendarContract.Events.TITLE, "Fiternity")
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, eventStartTime)
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, eventEndTime)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(modifyEventIntent);
                break;
            }
        }
    }
}
