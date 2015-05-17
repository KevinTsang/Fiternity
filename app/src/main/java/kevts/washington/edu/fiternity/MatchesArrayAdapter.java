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

import com.facebook.login.widget.ProfilePictureView;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
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
            holder.matchRowProfilePic = (ProfilePictureView)row.findViewById(R.id.match_icon);
            holder.name = (TextView)row.findViewById(R.id.match_name);
            holder.exercise = (TextView)row.findViewById(R.id.match_exercise);
            holder.startDate = (TextView)row.findViewById(R.id.match_start_time);
            holder.endDate = (TextView)row.findViewById(R.id.match_end_time);

            row.setTag(holder);
        } else {
            holder = (MatchHolder)row.getTag();
        }

        final ParseUser matchUser = parseUserList.get(position);
        holder.matchRowProfilePic.setProfileId(matchUser.getString("facebookId"));
        holder.name.setText(matchUser.getString("name"));
        if (matchUser.getString("exercise") != null) {
            holder.exercise.setText(matchUser.getString("exercise"));
        }
        List<ParseObject> eventList = matchUser.getList("event");
        final ParseObject event = FiternityApplication.getInstance().convertPointerToObjectEvent(eventList.get(0));
        holder.startDate.setText(new Date(event.getLong("startDate")).toString());
        holder.endDate.setText(new Date(event.getLong("endDate")).toString());

        return row;
    }
    static class MatchHolder {
        TextView name;
        TextView exercise;
        TextView startDate;
        TextView endDate;
        ProfilePictureView matchRowProfilePic;
    }
}


