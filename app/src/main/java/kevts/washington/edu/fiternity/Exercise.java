package kevts.washington.edu.fiternity;

/**
 * Created by kevin on 3/7/15.
 */
public class Exercise {

    private String exerciseName;
    private int userExperienceLevel;
    private int preferredPartnerExperienceLevel;

    public Exercise() {

    }

    public Exercise(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public Exercise(String exerciseName, int userExperienceLevel, int preferredPartnerExperienceLevel) {
        this.exerciseName = exerciseName;
        this.userExperienceLevel = userExperienceLevel;
        this.preferredPartnerExperienceLevel = preferredPartnerExperienceLevel;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getUserExperienceLevel() {
        return userExperienceLevel;
    }

    public void setUserExperienceLevel(int userExperienceLevel) {
        this.userExperienceLevel = userExperienceLevel;
    }

    public int getPreferredPartnerExperienceLevel() {
        return preferredPartnerExperienceLevel;
    }

    public void setPreferredPartnerExperienceLevel(int preferredPartnerExperienceLevel) {
        this.preferredPartnerExperienceLevel = preferredPartnerExperienceLevel;
    }
}
