package stocks.models;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class Message {
    private int messageId;
    private int userId;
    private int stockId;
    private String content;
    private Timestamp dateOfPost;
<<<<<<< HEAD
=======
    private AppUser appUser;
    
    
    public Message() {
    }

    public Message(int messageId, int userId, int stockId, String content, Date dateOfPost) {
        this.messageId = messageId;
        this.userId = userId;
        this.stockId = stockId;
        this.content = content;
        this.dateOfPost = (Timestamp) dateOfPost;
    }

    private List<Like> likes;
>>>>>>> ccc62f791f13e9925773795a2887f8746d198a43

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getDateOfPost() {
        return dateOfPost;
    }

    public void setDateOfPost(Timestamp dateOfPost) {
        this.dateOfPost = dateOfPost;
    }
<<<<<<< HEAD
=======

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
>>>>>>> ccc62f791f13e9925773795a2887f8746d198a43
}
