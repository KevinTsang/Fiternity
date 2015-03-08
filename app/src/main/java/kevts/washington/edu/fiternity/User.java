package kevts.washington.edu.fiternity;

import java.io.Serializable;

/**
 * Created by kevin on 3/7/15.
 */
public class User implements Serializable {
    private int userId;
    private String name;
    private int age;
    private String gender;
    private boolean genderPreference;
    private String email;
    private String phoneNumber;
    private int zipCode;
    public User() {
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
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

}
