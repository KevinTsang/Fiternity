package kevts.washington.edu.fiternity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MenuDrawer extends Activity {
    private String[] mTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_drawer);

        mTitles = new String[]{"Profile", "Calendar", "Activities", "Matches", "Feedback"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, mTitles));

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                /* Create intents with classes, add/delete cases as needed */
                Intent intent;
                switch(position) {
                    // Profile
                    case 0:
                        intent = new Intent();//MenuDrawer.this, ProfileActivity.class);
                        mDrawerLayout.closeDrawer(mDrawerList);
                        startActivity(intent);
                        break;

                    // Calendar
                    case 1:
                        mDrawerLayout.closeDrawer(mDrawerList);
                        FiternityInstance.instance().viewCalendar();
                        break;

                    // Activities
                    case 2:
                        intent = new Intent();//MenuDrawer.this, ActivitiesActivity.class);
                        mDrawerLayout.closeDrawer(mDrawerList);
                        startActivity(intent);
                        break;

                    // Matches
                    case 3:
                        intent = new Intent();//MenuDrawer.this, MatchesActivity.class);
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_drawer, menu);
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
