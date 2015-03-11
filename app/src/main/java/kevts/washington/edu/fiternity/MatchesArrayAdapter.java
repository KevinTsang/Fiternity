package kevts.washington.edu.fiternity;

import android.app.Activity;
import android.content.Context;
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
public class MatchesArrayAdapter extends ArrayAdapter<Exercise> {

    Context context;
    int layoutResourceId;
    private Exercise[] exerciseArr;

    public MatchesArrayAdapter(Context context, int resource, Exercise[] exercises) {
        super(context, resource, exercises);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        exerciseArr = exercises;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        //Context context = row.getContext();

        MatchHolder holder = null;


        if(row == null){

        }else{
            holder = (MatchHolder)row.getTag();
        }

        Exercise exercise = exerciseArr[position];
//        holder.icon.setImageResource(exercise.getInitiator().getIcon());
//
//        holder.name.setText(exercise.getInitiator().getName());
        holder.excercise.setText(exercise.getExerciseName());
        holder.time.setText("Placeholder for actual time");

        return row;
    }


    static class MatchHolder {
        ImageView icon;
        TextView name;
        TextView excercise;
        TextView time;
    }
}
