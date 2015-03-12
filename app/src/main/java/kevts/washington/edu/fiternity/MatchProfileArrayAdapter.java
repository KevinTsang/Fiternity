package kevts.washington.edu.fiternity;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by cvetand on 3/10/15.
 */
public class MatchProfileArrayAdapter extends ArrayAdapter<FreeEvent> {
    Context context;
    int layoutResourceId;
    private FreeEvent[] eventArr;
    long eventId;

    public MatchProfileArrayAdapter(Context context, int layoutResourceId, FreeEvent[] eventArr) {
        super(context, layoutResourceId, eventArr);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.eventArr = eventArr;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MatchHolder holder = null;
        final MatchProfile parentContext = (MatchProfile) parent.getContext();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MatchHolder();
            holder.startTime = (TextView)row.findViewById(R.id.match_start_time);
            holder.endTime = (TextView)row.findViewById(R.id.match_end_time);
            holder.requestButton = (Button)row.findViewById(R.id.requestButton);
            Log.i("MatchProfileAdapter", Boolean.toString(parentContext.isEventRequested(eventArr[position])));
            if(parentContext.isEventRequested(eventArr[position])){
                holder.requestButton.setText("Cancel");
            }
            holder.requestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MatchHolder mh = (MatchHolder) v.getTag();
                    if(mh.requestButton.getText().equals("Request")){
                        /*FiternityInstance instance = (FiternityInstance)context.getApplicationContext();
                        long calendarId = instance.getCalendarId();
                        if (calendarId != -1) {
                            Calendar calendar = Calendar.getInstance();
                            FreeEvent event = eventArr[position];
                            ContentValues values = new ContentValues();
                            values.put(Events.DTSTART, event.getStartTime().getTime());
                            values.put(Events.DTEND, event.getEndTime().getTime());
                            values.put(Events.TITLE, "Fiternity");
                            values.put(Events.DESCRIPTION, event.getDescription());
                            Uri uri = context.getContentResolver().insert(Events.CONTENT_URI, values);
                            eventId =  new Long(uri.getLastPathSegment());
                        }*/
                        mh.requestButton.setText("Cancel");
                        parentContext.addEventToSingleton(eventArr[position]);
                    }else{
                        /*if (eventId != 0) {
                            String[] selArgs = new String[] { eventId + "" };
                            int deleted = context.getContentResolver().delete(Events.CONTENT_URI,
                                    Events._ID + " =? ",
                                    selArgs);
                        }*/
                        mh.requestButton.setText("Request");
                        parentContext.removeEventFromSingleton(eventArr[position]);

                    }
                }
            });

            row.setTag(holder);
        }else{
            holder = (MatchHolder)row.getTag();
        }

        FreeEvent freeEvent = eventArr[position];
        SimpleDateFormat format = new SimpleDateFormat("h:mm a 'on' EEE, MMM d");
        String start = "" + format.format(freeEvent.getStartTime());
        String end = "" + format.format(freeEvent.getEndTime());

        holder.startTime.setText(start);
        holder.endTime.setText(end);
        holder.requestButton.setTag(holder);

        return row;
    }


    static class MatchHolder {
        TextView startTime;
        TextView endTime;
        Button requestButton;
    }
}
