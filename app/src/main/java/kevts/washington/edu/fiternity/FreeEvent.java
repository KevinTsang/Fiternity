package kevts.washington.edu.fiternity;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kevin on 4/15/15.
 */
public class FreeEvent extends ParseObject implements Serializable {
    private FiternityApplication instance;
    private ArrayList<ParseUser> users;
    private Date startTime;
    private Date endTime;
    private String description;

    public FreeEvent(Date startTime, Date endTime){
        instance = FiternityApplication.getInstance();
        this.users = new ArrayList<ParseUser>();
        this.users.add(instance.getParseUser());
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public ArrayList<ParseUser> getUsers() {
        return users;
    }

    public ParseUser getUser(int index){return users.get(index);}

    public void setUsers(ArrayList<ParseUser> users) {
        this.users = users;
    }

    public void addUser(ParseUser user){ this.users.add(user);}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FreeEvent getSmallerEvent(FreeEvent freeEvent) {
        Date overlapStart;
        Date overlapEnd;

        if (startTime.before(freeEvent.getStartTime())) {
            overlapStart = freeEvent.getStartTime();
        } else if (startTime.after(freeEvent.getStartTime())) {
            overlapStart = startTime;
        } else {
            overlapStart = startTime;
        }

        if (endTime.after(freeEvent.getEndTime())) {
            overlapEnd = freeEvent.getEndTime();
        } else if (endTime.before(freeEvent.getEndTime())) {
            overlapEnd = endTime;
        } else {
            overlapEnd = endTime;
        }

        return new FreeEvent(overlapStart, overlapEnd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FreeEvent freeEvent = (FreeEvent) o;

        if (endTime != null ? !endTime.equals(freeEvent.endTime) : freeEvent.endTime != null)
            return false;
        if (startTime != null ? !startTime.equals(freeEvent.startTime) : freeEvent.startTime != null)
            return false;
        if (users != null ? !users.equals(freeEvent.users) : freeEvent.users != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = users != null ? users.hashCode() : 0;
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        return result;
    }
}