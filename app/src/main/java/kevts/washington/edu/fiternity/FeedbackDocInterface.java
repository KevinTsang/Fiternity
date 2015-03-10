package kevts.washington.edu.fiternity;

import com.parse.ParseUser;

/**
 * Created by Theo on 3/9/2015.
 */
public interface FeedbackDocInterface {
    public void setUserTo(ParseUser user);
    public void setUserFrom(ParseUser user);
    public void setRating(int rating);
    public void setFeedbackText(String text);
    public ParseUser getUserTo();
    public ParseUser getUserFrom();
    public int getRating();
    public String getFeedbackText();
}
