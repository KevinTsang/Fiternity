package kevts.washington.edu.fiternity;

//import android.app.Fragment;
import android.app.Notification;
import android.app.TabActivity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.app.WindowDecorActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.List;


public class Matches extends ActionBarActivity {

    private WindowDecorActionBar.Tab allTab, recentTab;
    private Fragment allFragmentTab = new allMatches();
    private Fragment recentFragmentTab = new recentlyContacted();
    private FiternityInstance fiternityInstance;
    private FreeEvent[] exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        //sets up the tabs
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        allTab = actionBar.newTab().setText("All");
        recentTab = actionBar.newTab().setText("Recently Contacted");

        allTab.setTabListener(new MyTabListener(allFragmentTab));
        recentTab.setTabListener(new MyTabListener(recentFragmentTab));

        actionBar.addTab(allTab);
        actionBar.addTab(recentTab);

        fiternityInstance = (FiternityInstance) getApplication();


    }

    public List<FreeEvent> getRecentEvents(){
        return fiternityInstance.getRequestedFreeEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_matches, menu);
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
