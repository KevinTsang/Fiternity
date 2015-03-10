package kevts.washington.edu.fiternity;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cvetand on 3/10/15.
 */
public class FreeEvent {
    private ArrayList<User> users;
    private Date startTime;
    private Date endTime;

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

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void addUser(User user){ this.users.add(user);}
}
