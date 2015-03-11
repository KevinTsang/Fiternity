package kevts.washington.edu.fiternity;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class UserProfile extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        User user = (User) getIntent().getSerializableExtra("userClicked");
        Log.i("UserProfile", user.getName());

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
