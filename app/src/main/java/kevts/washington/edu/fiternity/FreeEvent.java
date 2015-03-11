package kevts.washington.edu.fiternity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cvetand on 3/10/15.
 */
public class FreeEvent implements Serializable {
    private ArrayList<User> users;
    private Date startTime;
    private Date endTime;

    public FreeEvent(User user, Date startTime, Date endTime){
        this.users = new ArrayList<User>();
        this.users.add(user);
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

    public ArrayList<User> getUsers() {
        return users;
    }

    public User getUser(int index){return users.get(index);}

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void addUser(User user){ this.users.add(user);}
}
