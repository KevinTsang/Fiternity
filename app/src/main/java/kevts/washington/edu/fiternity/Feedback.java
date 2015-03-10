package kevts.washington.edu.fiternity;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;

/**
 * Created by Theo on 3/9/2015.
 */
@ParseClassName("Feedback")
public class Feedback extends ParseObject implements Serializable {
    private String userToId;
    private String userFromId;
    private int rating;
    private String feedbackText;

    public Feedback() {

    }

    public void setUserToId(String user) {
<<<<<<< HEAD
        put("userToId", (String) user);
        userToId = user;
    }
    public void setUserFromId(String user) {
        put("userFromId", (String) user);
        userFromId = user;
=======
        if (user != null) {
            put("userToId", (String)user);
            userToId = user;
        } else {
            throw new IllegalArgumentException("UserTo cannot be null");
        }
    }
    public void setUserFromId(String user) {
        if (user != null) {
            put("userFromId", (String)user);
            userFromId = user;
        } else {
            throw new IllegalArgumentException("UserFrom cannot be null");
        }
>>>>>>> origin/master
    }
    public void setRating(int rating) {
        put("rating", rating);
        this.rating = rating;
    }
    public void setFeedbackText(String text) {
        put("feedbackText", text);
        feedbackText = text;
    }
    public String getUserTo() {
        return (String) get("userToId");
    }
    public String getUserFrom() {
        return (String) get("userFromId");
    }
    public int getRating() {
        //return (int) get("rating");
        return 0;
    }
    public String getFeedbackText() {
        return (String) get("feedbackText");
    }
}
