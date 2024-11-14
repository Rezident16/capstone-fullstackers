package stocks.models;

public class Like {
    private int likeId;
    private boolean isLiked;
    private int userId;
    private int messageId;

    public Like(int likeId, boolean isLiked, int userId, int messageId) {
        this.likeId = likeId;
        this.isLiked = isLiked;
        this.userId = userId;
        this.messageId = messageId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}
