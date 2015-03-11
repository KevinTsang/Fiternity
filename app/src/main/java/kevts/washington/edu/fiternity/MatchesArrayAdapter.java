package kevts.washington.edu.fiternity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

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
        //Context context = row.getContext();

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
/*
            Drawable drawable = getContext().getDrawable(R.drawable.com_facebook_button_blue);
.setImageDrawable(placeholder);
            img=(ImageView)findViewById(R.id.imageview1);
            Drawable myDrawable = getResources().getDrawable(R.drawable.imageView1);
            img.setImageDrawable(myDrawable);
*/
        }else{
            holder = (MatchHolder)row.getTag();
            /*
            holder.icon.setImageResource(R.drawable.com_facebook_logo);
            holder.name.setText("empty");
            holder.startTime.setText("none");
            holder.endTime.setText("never");
            */
        }

        FreeEvent freeEvent = eventArr[position];
        //holder.icon.setImageResource(freeEvent.getUser(0).getIcon());
        //Drawable placeholder = context.getDrawable(R.drawable.com_facebook_button_blue);

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
