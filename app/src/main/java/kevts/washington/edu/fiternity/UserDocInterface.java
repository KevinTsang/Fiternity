package kevts.washington.edu.fiternity;

import java.util.ArrayList;

/**
 * Created by kevin on 3/7/15.
 */
public interface UserDocInterface {
    public int getUserId();
    public void setUserId(int userId);
    public String getName();
    public void setName(String name);
    public int getAge();
    public void setAge(int age);
    public String getGender();
    public void setGender(String gender);
    public boolean getGenderPreference();
    public void setSameGenderPreference(boolean preference);
    public String getEmail();
    public void setEmail(String email);
    public String getPhoneNumber();
    public void setPhoneNumber(String phoneNumber);
    public int getZipCode();
    public void setZipCode(int zipCode);
    public ArrayList<Exercise> getActivities();
    public void addActivity();
}
