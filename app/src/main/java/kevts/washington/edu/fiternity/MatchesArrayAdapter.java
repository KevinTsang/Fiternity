package kevts.washington.edu.fiternity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by cvetand on 3/10/15.
 */
public class MatchesArrayAdapter extends ArrayAdapter<FreeEvent> {

    Context context;
    int layoutResourceId;
    private FreeEvent[] eventArr;

    public MatchesArrayAdapter(Context context, int layoutResourceId, FreeEvent[] eventArr) {
        super(context, layoutResourceId, eventArr);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.eventArr = eventArr;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MatchHolder holder = null;

        if(row == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MatchHolder();
            holder.icon = (ImageView)row.findViewById(R.id.match_icon);
            holder.name = (TextView)row.findViewById(R.id.match_name);
            holder.startTime = (TextView)row.findViewById(R.id.match_start_time);
            holder.endTime = (TextView)row.findViewById(R.id.match_end_time);

            row.setTag(holder);
        }else{
            holder = (MatchHolder)row.getTag();
        }

        FreeEvent freeEvent = eventArr[position];

        holder.icon.setImageResource(R.drawable.com_facebook_profile_default_icon);
        holder.name.setText(freeEvent.getUser(0).getName());
        holder.startTime.setText(freeEvent.getStartTime().toString());
        holder.endTime.setText(freeEvent.getEndTime().toString());

        return row;
    }


    static class MatchHolder {
        ImageView icon;
        TextView name;
        TextView startTime;
        TextView endTime;
    }
}
