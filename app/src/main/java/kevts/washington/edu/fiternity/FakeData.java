package kevts.washington.edu.fiternity;

/**
 * Created by kevin on 3/8/15.
 */
public class FakeData {
    public static User createJenny() {
        User jenny = new User();
        jenny.setName("Jennifer Crawford");
        jenny.setAge(24);
        jenny.setGender('F');
        jenny.setSameGenderPreference(true);
        jenny.setUserId(0);
        jenny.setZipCode(98105);
        return jenny;
    }

    public static User createMichael() {
        User michael = new User();
        michael.setName("Michael Scott");
        michael.setAge(28);
        michael.setGender('M');
        michael.setSameGenderPreference(false);
        michael.setUserId(1);
        michael.setZipCode(98006);
        return michael;
    }

    public static User createAnnie() {
        User annie = new User();
        annie.setName("Annie Tam");
        annie.setAge(20);
        annie.setGender('F');
        annie.setSameGenderPreference(false);
        annie.setUserId(2);
        annie.setZipCode(98195);
        return annie;
    }

    public static User createMarshall() {
        User marshall = new User();
        marshall.setName("Marshall Brown");
        marshall.setAge(32);
        marshall.setGender('M');
        marshall.setSameGenderPreference(true);
        marshall.setUserId(3);
        marshall.setZipCode(98195);
        return marshall;
    }
}
