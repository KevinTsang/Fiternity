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
    private ParseUser userTo;
    private ParseUser userFrom;
    private int rating;
    private String feedbackText;

    public Feedback() {

    }

    public void setUserTo(ParseUser user) {
        put("userTo", user);
        userTo = user;
    }
    public void setUserFrom(ParseUser user) {
        put("userFrom", user);
        userFrom = user;
    }
    public void setRating(int rating) {
        put("rating", rating);
        this.rating = rating;
    }
    public void setFeedbackText(String text) {
        put("feedbackText", text);
        feedbackText = text;
    }
    public ParseUser getUserTo() {
        return (ParseUser) get("userTo");
    }
    public ParseUser getUserFrom() {
        return (ParseUser) get("userFrom");
    }
    public int getRating() {
        return (int) get("rating");
    }
    public String getFeedbackText() {
        return (String) get("feedbackText");
    }
}
