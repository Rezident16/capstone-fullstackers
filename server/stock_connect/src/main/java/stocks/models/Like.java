package stocks.models;

public class Like {
    private int likeId;
    private boolean isLiked;
    private User user;
    private Message message;

    public Like(){}

    public Like(int likeId, boolean isLiked, User user, Message message) {
        this.likeId = likeId;
        this.isLiked = isLiked;
        this.user = user;
        this.message = message;
    }

    public int getLikeId() {
        return likeId;
    }

    public void setLikeId(int likeId) {
        this.likeId = likeId;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
