package kevts.washington.edu.fiternity;


import android.content.ContentUris;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;


public class MenuDrawer extends ActionBarActivity {
    private String[] mTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;

    static final int CALENDAR_ACCESSED = 21;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_drawer);

        mTitle = "Menu";
        mTitles = new String[]{"Profile", "Calendar", "Activities", "Matches", "Feedback"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, mTitles);
        mDrawerList.setAdapter(adapter);

        int first = mDrawerList.getFirstVisiblePosition();
        int last = mDrawerList.getLastVisiblePosition();
        Log.i("info", "First: " + first + ", last: " + last);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                /* Create intents with classes */
                Intent intent;
                Log.i("info", "Holy shit, the click registered");
                switch(position) {
                    // Profile
                    case 0:
                        intent = new Intent(MenuDrawer.this, ProfileActivity.class);
                        mDrawerLayout.closeDrawer(mDrawerList);
                        startActivity(intent);
                        break;

                    // Calendar
                    case 1:
                        mDrawerLayout.closeDrawer(mDrawerList);
                        long startMillis = Calendar.getInstance().getTimeInMillis();
                        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                        builder.appendPath("time");
                        ContentUris.appendId(builder, startMillis);
                        intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
                        // Prepopulate user events if they don't exist?
                        startActivityForResult(intent, CALENDAR_ACCESSED);
                        break;

                    // Activities
                    case 2:
                        intent = new Intent(MenuDrawer.this, ExerciseActivity.class);
                        mDrawerLayout.closeDrawer(mDrawerList);
                        startActivity(intent);
                        break;

                    // Matches
                    case 3:
                        intent = new Intent(MenuDrawer.this, Matches.class);
                        mDrawerLayout.closeDrawer(mDrawerList);
                        startActivity(intent);
                        break;

                    // Feedback
                    case 4:
                        intent = new Intent(MenuDrawer.this, FeedbackActivity.class);
                        mDrawerLayout.closeDrawer(mDrawerList);
                        startActivity(intent);
                        break;
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
                getSupportActionBar().setTitle("Fiternity");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                Log.i("info", "Holy shit, the drawer is about to open");
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                Log.i("info", "Holy shit, the drawer opened");
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CALENDAR_ACCESSED) {
            if (resultCode == RESULT_CANCELED) {
                // read all Fiternity events here
                FiternityInstance instance = (FiternityInstance)getApplication();
                ArrayList<FreeEvent> events = instance.getEvents(this, instance.getUser());
                instance.getUser().setFreeTimes(events);
                startActivity(new Intent(this, Matches.class));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_drawer, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
}
