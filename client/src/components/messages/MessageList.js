import { useEffect, useState } from "react";
import MessageInput from "./MessageInput";
import thumbsUp from '../../assets/thumbsUp.png'; 
import thumbsUpFill from '../../assets/thumbsUpFill.png'; 
import { useUser } from "../context/UserContext";

function MessageList({ stockId }) {
  const [messages, setMessages] = useState([]);
  const [userLikes, setUserLikes] = useState([]);
  const { userId, jwtToken } = useUser();

  // Fetch messages for the stock
  useEffect(() => {
    if (stockId) {
      const url = `http://localhost:8080/api/message/stock/${stockId}`;
      fetch(url)
        .then(response => response.json())
        .then(data => setMessages(data))
        .catch(console.error);
    }
  }, [stockId]);

  // Fetch user likes on component mount
  useEffect(() => {
    if (userId) {
      const url = `http://localhost:8080/api/message/like/user/${userId}`;
      fetch(url, {
        headers: { Authorization: `Bearer ${jwtToken}` }
      })
        .then(response => response.json())
        .then(data => setUserLikes(data))
        .catch(console.error);
    }
  }, [userId, jwtToken]);

  // Handle new message post
  const handleNewMessage = (newMessage) => {
    setMessages(prevMessages => [newMessage, ...prevMessages]);
  };

  // Check if a message is liked by the user
  const isLikedByUser = (messageId) => {
    const like = userLikes.find(like => like.messageId === messageId);
    return like && like.liked === true;
  };


  // Toggle like status
const toggleLike = async (messageId) => {
  const existingLike = userLikes.find(like => like.messageId === messageId);

  if (existingLike) {
    // Toggle the current like status
    const newLikedStatus = !existingLike.liked;

    // Update the like status in the backend
    const response = await fetch(`http://localhost:8080/api/message/like/id/${existingLike.likeId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${jwtToken}`
      },
      body: JSON.stringify({ ...existingLike, liked: newLikedStatus })
    });

    if (response.ok) {
      // Update the like status in the frontend state
      setUserLikes(prevLikes => 
        prevLikes.map(like => 
          like.likeId === existingLike.likeId ? { ...like, liked: newLikedStatus } : like
        )
      );
    }
  } else {
    // Add a new like with liked: true
    const response = await fetch(`http://localhost:8080/api/message/like`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${jwtToken}`
      },
      body: JSON.stringify({ userId, messageId, liked: true })
    });
    
    if (response.ok) {
      const newLike = await response.json();
      setUserLikes(prevLikes => [...prevLikes, newLike]);
    }
  }
};


  return (
    <section className="container-fluid pl-0">
      <h4>Messages for Stock ID: {stockId}</h4>

      <MessageInput stockId={stockId} userId={userId} onMessagePosted={handleNewMessage} />

      {messages.length > 0 ? (
        <table className="table table-striped">
          <thead>
            <tr>
              <th>User ID</th>
              <th></th>
              <th>Content</th>
              <th>Date of Post</th>
              <th>Likes</th>
            </tr>
          </thead>
          <tbody>
            {messages.map(message => (
              <tr key={message.messageId}>
                <td>{message.userId}</td>
                <td>
                  <img 
                    src={isLikedByUser(message.messageId) ? thumbsUpFill : thumbsUp} 
                    alt="like"
                    style={{ width: '30px', height: '30px' }}
                    onClick={() => toggleLike(message.messageId)}
                  />
                </td>
                <td>{message.content}</td>
                <td>{new Date(message.dateOfPost).toLocaleString()}</td>
                <td>{message.likes ? message.likes.length : 0}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>No messages found for this stock.</p>
      )}
    </section>
  );
}

export default MessageList;
