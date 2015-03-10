package kevts.washington.edu.fiternity;

import com.parse.Parse;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 3/7/15.
 */
public class User implements Serializable {
    private int userId;
    private String name;
    private int age;
    private char gender;
    private boolean genderPreference;
    private String email;
    private String phoneNumber;
    private int zipCode;
    private List<Exercise> exerciseList;
    public User() {
        exerciseList = new ArrayList<Exercise>();
        // construct using Facebook's information
        // once I figure out how to pull from there
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public boolean getGenderPreference() {
        return genderPreference;
    }

    public void setSameGenderPreference(boolean preference) {
        this.genderPreference = preference;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public void addExercise(Exercise exercise) {
        exerciseList.add(exercise);
    }

    public void removeExercise(Exercise exercise) {
        exerciseList.remove(exercise);
    }

    public ParseUser userToParseUser() {
        ParseUser parseUser = new ParseUser();
        parseUser.put("id", getUserId());
        parseUser.put("name", getName());
        parseUser.put("age", getAge());
        parseUser.put("gender", getGender() + "");
        parseUser.put("genderpreference", getGenderPreference());
        parseUser.put("phonenumber", getPhoneNumber());
        parseUser.put("zipcode", getZipCode());
        return parseUser;
    }

    public User parseUserToUser() {
        User user = new User();
        ParseUser parseUser = new ParseUser();
        /*
        user.setUserId((int)parseUser.get("id"));
        user.setName((String)parseUser.getString("name"));
        user.setAge((int)parseUser.get("age"));
        user.setGender((char)parseUser.get("gender"));
        user.setSameGenderPreference((boolean)parseUser.get("genderpreference"));
        user.setPhoneNumber((String)parseUser.getString("phonenumber"));
        user.setZipCode((int)parseUser.get("zipcode"));
        */
        return user;
    }

}
