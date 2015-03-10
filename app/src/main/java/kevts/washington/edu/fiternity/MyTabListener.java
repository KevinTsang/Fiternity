package kevts.washington.edu.fiternity;

//import android.app.Fragment;
//import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;

/**
 * Created by cvetand on 3/10/15.
 */

public class MyTabListener implements TabListener {
    Fragment fragment;

    public MyTabListener(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        ft.replace(R.id.FragContainer, fragment);
    }

    @Override
    public void onTabUnselected(Tab tab, android.support.v4.app.FragmentTransaction ft) {
        ft.remove(fragment);
    }

    @Override
    public void onTabReselected(Tab tab, android.support.v4.app.FragmentTransaction ft) {
        // nothing done here
    }
}
