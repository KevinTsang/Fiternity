package kevts.washington.edu.fiternity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class ReportActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        final EditText reportText = (EditText) findViewById(R.id.feedback_report_edit_text);

        Button reportButton = (Button) findViewById(R.id.feedback_send_feedback_btn);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleReport(reportText);
                setResult(2);
//                finish();
            }
        });
    }

    private void handleReport(EditText reportText) {
        Feedback feedback = new Feedback();
        String feedbackText = reportText.getText().toString();
        ParseUser curUser = FiternityInstance.instance().getUser();
        ParseUser otherUser = FiternityInstance.instance().getOtherUser();

        feedback.setFeedbackText(feedbackText);
        feedback.setRating(1);
        /********************************
         * I want to do curUser.getObjectID() and replace Annie with it
         * otherUser.getObjectID() should replace Jenny
         *
         */
        feedback.setUserFromId("Annie");
        feedback.setUserToId("Jenny");
        feedback.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                } else {

                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
