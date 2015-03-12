package kevts.washington.edu.fiternity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MatchProfile extends ActionBarActivity {
    private FiternityInstance fiternityInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_profile);
        Button menuButton = (Button)findViewById(R.id.matchprofile_menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMenu = new Intent(MatchProfile.this, MenuDrawer.class);
                startActivity(toMenu);
            }
        });
        User user = (User) getIntent().getSerializableExtra("userClicked");
        Log.i("UserProfile", user.getName());
        fiternityInstance = (FiternityInstance) getApplicationContext();


        TextView name, gender, age;
        name = (TextView) this.findViewById(R.id.userName);
        gender = (TextView) this.findViewById(R.id.userGender);
        age = (TextView) this.findViewById(R.id.userAge);

        name.setText(user.getName());
        switch (user.getGender()){
            case 'M':
                gender.setText("Male");
                break;
            case 'F':
                gender.setText("Female");
                break;
        }

        age.setText(""+user.getAge());

        //Matching Schedules
        Date now = new Date();

        List<FreeEvent> userFreeTime = user.getFreeTimes();
        final FreeEvent[] matchingSchedules = new FreeEvent[userFreeTime.size()];
        userFreeTime.toArray(matchingSchedules);

        /*
        FreeEvent[] matchingSchedules = new FreeEvent[]{
                new FreeEvent(user, now, now),
                new FreeEvent(user, now, now),
        };
        */

        //need to make another arrayAdapter for this
        MatchProfileArrayAdapter adapter = new MatchProfileArrayAdapter(getApplicationContext(), R.layout.listview_match_profile_row, matchingSchedules);
        ListView matchList = (ListView)findViewById(R.id.matchProfileList);
        matchList.setAdapter(adapter);
    }

    public void addEventToSingleton(FreeEvent event){
        fiternityInstance.addRequestedFreeEvent(event);
        Log.i("addEventToSingleton", "Added "+event.getUser(0).getName()+"'s event");
    }

    public void removeEventFromSingleton(FreeEvent event){
        fiternityInstance.removeRequestedFreeEvent(event);
        Log.i("rmEventFromSingleton", "Removed "+event.getUser(0).getName()+"'s event");
    }


    public boolean isEventRequested(FreeEvent event){
        List<FreeEvent> events = fiternityInstance.getRequestedFreeEvents();
        if(events.size() == 0){
            Log.i("Requested events", "no events");
        }else {
            for (int i = 0; i < events.size(); i++) {
                Log.i("Requested events", events.get(i).toString());
            }
        }
        Log.i("Requested events", event.toString());
        return fiternityInstance.getRequestedFreeEvents().contains(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
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
