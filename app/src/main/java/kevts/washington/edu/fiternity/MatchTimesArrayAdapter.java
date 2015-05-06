package kevts.washington.edu.fiternity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

/**
 * Created by kevin on 5/5/15.
 */
public class MatchTimesArrayAdapter extends ArrayAdapter {
    private List<ParseObject> eventsList;
    private Context context;
    private int resource;

    public MatchTimesArrayAdapter(Context context, int resource, int textViewResourceId, List<ParseObject> events) {
        super(context, textViewResourceId, events);
        this.context = context;
        this.resource = resource;
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

            row.setTag(holder);
        } else {
            holder = (MatchHolder)row.getTag();
        }

        ParseObject matchEvent = eventsList.get(position);
//        holder.exercise.setText(matchUser.getString("exercise"));
        holder.startDate.setText(new Date(matchEvent.getLong("startDate")).toString());
        holder.endDate.setText(new Date(matchEvent.getLong("endDate")).toString());

        return row;
    }
    static class MatchHolder {
        TextView exercise;
        TextView startDate;
        TextView endDate;
    }
}
