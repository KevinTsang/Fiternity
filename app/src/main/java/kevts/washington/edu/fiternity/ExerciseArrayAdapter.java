package kevts.washington.edu.fiternity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by isaac on 5/25/15.
 */
public class ExerciseArrayAdapter extends ArrayAdapter<ParseObject> {

    private List<ParseObject> exerciseList;
    private Context context;
    private int resource;


    public ExerciseArrayAdapter(Context context, int resource, int textViewResourceId,
                                List<ParseObject> sports) {
        super(context, textViewResourceId, sports);
        this.context = context;
        this.resource = resource;
        exerciseList = sports;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ParseObject parseObject = exerciseList.get(position);
        View row = convertView;
        ExerciseHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);
            holder = new ExerciseHolder();
            holder.exercise = (TextView)row.findViewById(R.id.exercise_name);
            holder.removeExercise = (Button)row.findViewById(R.id.exercise_remove_button);
            row.setTag(holder);
        } else {
            holder = (ExerciseHolder)row.getTag();
        }
        holder.exercise.setText(parseObject.getString("name"));
        holder.exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.removeExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseList.remove(position);
                ExerciseArrayAdapter.this.notifyDataSetChanged();
            }
        });
        return row;
    }

    static class ExerciseHolder {
        TextView exercise;
        Button removeExercise;
    }
}
