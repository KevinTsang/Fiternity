package kevts.washington.edu.fiternity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by cvetand on 3/10/15.
 */
public class MatchProfileArrayAdapter extends ArrayAdapter<FreeEvent> {
    Context context;
    int layoutResourceId;
    private FreeEvent[] eventArr;

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
                        mh.requestButton.setText("Cancel");
                        parentContext.addEventToSingleton(eventArr[position]);
                    }else{
                        mh.requestButton.setText("Request");
                    }
                }
            });

            row.setTag(holder);
        }else{
            holder = (MatchHolder)row.getTag();
        }

        FreeEvent freeEvent = eventArr[position];

        holder.startTime.setText(freeEvent.getStartTime().toString());
        holder.endTime.setText(freeEvent.getEndTime().toString());
        holder.requestButton.setTag(holder);

        return row;
    }


    static class MatchHolder {
        TextView startTime;
        TextView endTime;
        Button requestButton;
    }
}
