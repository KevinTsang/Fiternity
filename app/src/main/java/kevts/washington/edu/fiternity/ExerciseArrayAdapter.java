package kevts.washington.edu.fiternity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
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
        final ParseObject parseObject = exerciseList.get(position);
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
                LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View dialogLayout = layoutInflater.inflate(R.layout.fragment_exp_level_setter, null);
                SeekBar seekTop = (SeekBar) dialogLayout.findViewById(R.id.user_exp_level);
                SeekBar seekBottom = (SeekBar) dialogLayout.findViewById(R.id.partner_exp_level);
                int user_level = parseObject.getInt("userExpLevel");
                int partner_level = parseObject.getInt("partnerExpLevel");
                seekTop.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    private int user_level;

                    private SeekBar.OnSeekBarChangeListener init(int user_level) {
                        this.user_level = user_level;
                        return this;
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekTop, int progress, boolean fromUser) {
                        user_level = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekTop) { }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekTop) { }
                }.init(user_level));
                seekBottom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    private int partner_level;

                    private SeekBar.OnSeekBarChangeListener init(int partner_level) {
                        this.partner_level = partner_level;
                        return this;
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBottom, int progress, boolean fromUser) {
                        partner_level = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBottom) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBottom) {
                    }
                }.init(partner_level));
                seekTop.setProgress(user_level);
                seekBottom.setProgress(partner_level);
                builder.setTitle(parseObject.getString("name"));
                builder.setView(dialogLayout);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    private View view;
                    private SeekBar seekTop;
                    private SeekBar seekBottom;

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ParseObject editedActivity = new ParseObject("Exercise");
                        editedActivity.put("name", parseObject.get("name"));
                        editedActivity.put("userExpLevel", seekTop.getProgress());
                        editedActivity.put("partnerExpLevel", seekBottom.getProgress());
                        exerciseList.add(editedActivity);
                        exerciseList.remove(parseObject);
                        ExerciseArrayAdapter.this.notifyDataSetChanged();
                    }

                    private DialogInterface.OnClickListener init(View view, SeekBar seekTop, SeekBar seekBottom) {
                        this.view = view;
                        this.seekTop = seekTop;
                        this.seekBottom = seekBottom;
                        return this;
                    }
                }.init(v, seekTop, seekBottom));
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
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
