package kevts.washington.edu.fiternity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import bolts.Task;

/**
 * Created by kevin on 5/5/15.
 */
public class MatchTimesArrayAdapter extends ArrayAdapter {
    private List<ParseObject> eventsList;
    private Context context;
    private int resource;
    private String userId;

    public MatchTimesArrayAdapter(Context context, int resource, int textViewResourceId, List<ParseObject> events, String userId) {
        super(context, textViewResourceId, events);
        this.context = context;
        this.resource = resource;
        this.userId = userId;
        eventsList = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MatchHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);
            holder = new MatchHolder();
            holder.exercise = (TextView)row.findViewById(R.id.match_exercise);
            holder.startDate = (TextView)row.findViewById(R.id.match_start_time);
            holder.endDate = (TextView)row.findViewById(R.id.match_end_time);
            holder.requestButton = (Button)row.findViewById(R.id.request_button);
            row.setTag(holder);
        } else {
            holder = (MatchHolder)row.getTag();
        }

        final ParseObject matchEvent = eventsList.get(position);

//        holder.exercise.setText(matchUser.getString("exercise"));
        holder.startDate.setText(new Date(matchEvent.getLong("startDate")).toString());
        holder.endDate.setText(new Date(matchEvent.getLong("endDate")).toString());

        holder.requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                parseQuery.whereEqualTo("facebookId", userId);
                HashMap<String, Object> params = new HashMap<String, Object>();
                try {
                    ParseUser matchUser = parseQuery.getFirst();
                    params.put("name", matchUser.getString("name"));
                    params.put("facebookId", matchUser.getString("facebookId"));
                    params.put("startDate", new Date(matchEvent.getLong("startDate")).toString());
                    params.put("endDate", new Date(matchEvent.getLong("endDate")).toString());
                } catch (ParseException pe) {
                    Log.e("MatchTimesArrayAdapter", "The match user is null");
                }
                ParseCloud.callFunctionInBackground("validatePush", params, new FunctionCallback<String>() {
                    @Override
                    public void done(String s, ParseException e) {
                        if (e == null) {
                            // Push sent successfully
                            Toast.makeText(context, "Request sent successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        return row;
    }
    static class MatchHolder {
        TextView exercise;
        TextView startDate;
        TextView endDate;
        Button requestButton;
    }
}
