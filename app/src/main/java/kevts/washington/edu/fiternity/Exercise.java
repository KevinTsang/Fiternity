package kevts.washington.edu.fiternity;

/**
 * Created by kevin on 4/12/15.
 */
public class Exercise {

    private String exerciseName;
    private int ownExpLevel;
    private int prefExpLevel;

    public Exercise() {

    }

    public Exercise(String name, int uExpLevel, int partExpLevel) {
        exerciseName = name;
        ownExpLevel = uExpLevel;
        prefExpLevel = partExpLevel;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String name) {
        exerciseName = name;
    }

    public int getOwnExpLevel() {
        return ownExpLevel;
    }

    public void setOwnExpLevel(int uExpLevel) {
        ownExpLevel = uExpLevel;
    }

    public int getPrefExpLevel() {
        return prefExpLevel;
    }

    public void setPrefExpLevel(int partExpLevel) {
        prefExpLevel = partExpLevel;
    }

}
