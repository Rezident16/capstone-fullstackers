import { useEffect, useState } from "react";
import MessageInput from "./MessageInput";

function MessageList({ stockId, userId }) {
  const [messages, setMessages] = useState([]);

  // Fetch messages only if stockId is provided
  useEffect(() => {
    if (stockId) {
      const url = `http://localhost:8080/api/message/stock/${stockId}`;
      fetch(url)
        .then(response => response.json())
        .then(data => setMessages(data))
        .catch(console.error);
    }
  }, [stockId]);

  // Update message list with new message after posting
  const handleNewMessage = (newMessage) => {
    setMessages(prevMessages => [newMessage, ...prevMessages]); // Add the new message to the top of the list
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
              <th>Content</th>
              <th>Date of Post</th>
              <th>Likes</th>
            </tr>
          </thead>
          <tbody>
            {messages.map(message => (
              <tr key={message.messageId}>
                <td>{message.userId}</td>
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
