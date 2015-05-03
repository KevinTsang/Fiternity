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
 * Created by iguest on 5/2/15.
 */
public class MatchesArrayAdapter extends ArrayAdapter<ParseUser> {

    private List<ParseUser> parseUserList;
    private Context context;
    private int resource;

    public MatchesArrayAdapter(Context context, int resource, int textViewResourceId, List<ParseUser> parseUsers) {
        super(context, textViewResourceId, parseUsers);
        this.context = context;
        this.resource = resource;
        parseUserList = parseUsers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MatchHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);
            holder = new MatchHolder();
            holder.name = (TextView)row.findViewById(R.id.match_name);
            holder.exercise = (TextView)row.findViewById(R.id.match_exercise);
            holder.startDate = (TextView)row.findViewById(R.id.match_start_time);
            holder.endDate = (TextView)row.findViewById(R.id.match_end_time);

            row.setTag(holder);
        } else {
            holder = (MatchHolder)row.getTag();
        }

        ParseUser matchUser = parseUserList.get(position);
        holder.name.setText(matchUser.getString("name"));
//        holder.exercise.setText(matchUser.getString("exercise"));
        List<ParseObject> eventList = matchUser.getList("event");
        ParseObject event = FiternityApplication.getInstance().convertPointerToObjectEvent(eventList.get(0));
        holder.startDate.setText(new Date(event.getLong("startDate")).toString());
        holder.endDate.setText(new Date(event.getLong("endDate")).toString());

        return row;
    }
    static class MatchHolder {
        TextView name;
        TextView exercise;
        TextView startDate;
        TextView endDate;
    }
}


