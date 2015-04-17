package kevts.washington.edu.fiternity;

import android.content.ContentUris;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Set;


public class UserProfileActivity extends ActionBarActivity {

    private static final String TAG = "UserProfileActivity";
    private static final int calendarRequestCode = 17;
    private FiternityApplication instance;
    private String[] mActivityNames;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        instance = FiternityApplication.getInstance();
        if (getIntent().getBooleanExtra("loadUserProfileFragment", true)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new UserProfileFragment())
                    .commit();
            mTitle = "Profile";
            setTitle(mTitle);
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new ExerciseFragment())
                    .commit();
            mTitle = "Activities";
            setTitle(mTitle);
        }
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.content_frame, new UserProfileFragment())
//                    .commit();
//        }
        mActivityNames = new String[] {"Profile", "Matches", "Activities", "View Schedule", "Schedule Exercise", "Log out"};
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mActivityNames));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Fragment fragment = null;
                switch (position) {
                    case 0: fragment = new UserProfileFragment();
                        break;
                    case 1:
                        Intent matchesIntent = new Intent(UserProfileActivity.this, MatchesActivity.class);
                        startActivity(matchesIntent);
                        break;
                    case 2: fragment = new ExerciseFragment();
                        break;
                    case 3:
                        long startMillis = Calendar.getInstance().getTimeInMillis();
                        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                        builder.appendPath("time");
                        ContentUris.appendId(builder, startMillis);
                        Intent calendarIntent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
                        startActivity(calendarIntent);
                        break;
                    case 4:
                        Calendar beginTime = Calendar.getInstance();
//        beginTime.set(); set a time here
                        Calendar endTime = Calendar.getInstance();
//        endTime.set();  set a time here
                        Intent createEventIntent = new Intent(Intent.ACTION_INSERT)
                                .setData(CalendarContract.Events.CONTENT_URI)
//                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
//                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                                .putExtra(CalendarContract.Events.TITLE, "Fiternity")
                                .putExtra(CalendarContract.Events.DESCRIPTION, "Insert activity here")
                                .putExtra(CalendarContract.Events.EVENT_LOCATION, "Insert location here")
                                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);
//                                .putExtra(Intent.EXTRA_EMAIL, "email address here, another email address here");
                        startActivity(createEventIntent);
                        break;
                    case 5:
                        instance.getParseUser().logOutInBackground();
                        Intent logoutIntent = new Intent(UserProfileActivity.this, FiternityLogin.class);
                        startActivity(logoutIntent);
                        break;
                }
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .commit();
                    mDrawerLayout.closeDrawer(mDrawerList);
                }

            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == calendarRequestCode) {
            if (resultCode == RESULT_CANCELED) {
                Set<FreeEvent> userEvents = instance.readEvents();
                ParseUser user = instance.getParseUser();
                for (FreeEvent fe : userEvents) {
                    user.add("events", fe);
                }
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.i(TAG, "Successfully saved events to the cloud!");
                    }
                });
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }
}
