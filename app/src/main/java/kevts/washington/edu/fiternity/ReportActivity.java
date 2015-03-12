package kevts.washington.edu.fiternity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


public class ReportActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Button menuButton = (Button)findViewById(R.id.report_menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMenu = new Intent(ReportActivity.this, MenuDrawer.class);
                startActivity(toMenu);
            }
        });

        final EditText reportText = (EditText) findViewById(R.id.feedback_report_edit_text);

        Button reportButton = (Button) findViewById(R.id.feedback_send_feedback_btn);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleReport(reportText);
                setResult(2);
                finish();
            }
        });
    }

    private void handleReport(EditText reportText) {
        final Feedback feedback = new Feedback();
        final String feedbackText = reportText.getText().toString();
        ParseUser curUser = FiternityInstance.instance().getUser();
        final ParseUser otherUser = FiternityInstance.instance().getOtherUser();

        //set the objectID for the current user
        ParseQuery<ParseUser> queryFrom = ParseUser.getQuery();
        queryFrom.whereEqualTo("username", curUser.getUsername());
        queryFrom.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (e == null) {
                    String fromUserID = (String) parseUsers.get(0).getObjectId();
                    Log.d("fromUser ID: ", fromUserID);
                    feedback.setUserFromId(fromUserID);
                    //set the objectID for the other user
                    ParseQuery<ParseUser> queryTo = ParseUser.getQuery();
                    queryTo.whereEqualTo("username", otherUser.getUsername());
                    queryTo.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> parseUsers, ParseException e) {
                            if (e == null) {
                                String toUserID = (String) parseUsers.get(0).getObjectId();
                                Log.d("toUser ID: ", toUserID);
                                feedback.setUserToId(toUserID);
                                //saves feedback into server
                                feedback.setFeedbackText(feedbackText);
                                feedback.setRating(1);
                                feedback.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Log.d("feedback saved:", " success");
                                        } else {
                                            Log.d("feedback saved:", " error");
                                        }
                                    }
                                });
                            } else {
                                Log.d("users retrieved", " Error: " + e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.d("users retrieved", " Error: " + e.getMessage());
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
