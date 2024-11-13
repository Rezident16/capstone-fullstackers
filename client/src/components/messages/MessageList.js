import { useEffect, useState } from "react";

function MessageList({ stockId }) {
  const [messages, setMessages] = useState([]);
  
  // Only fetch messages if stockId is provided
  useEffect(() => {
    if (stockId) {
      const url = `http://localhost:8080/api/message/stock/${stockId}`;
      fetch(url)
        .then(response => response.json())
        .then(data => setMessages(data))
        .catch(console.error);
    }
  }, [stockId]);

  return (
    <section className="container-fluid pl-0 ">
      <h4>Messages for Stock ID: {stockId}</h4>
      {messages.length > 0 ? (
        <table className="table table-striped">
          <thead>
            <tr>
              <th>User ID</th>
              <th>Content</th>
              <th>Date of Post</th>
            </tr>
          </thead>
          <tbody>
            {messages.map(message => (
              <tr key={message.messageId}>
                <td>{message.userId}</td>
                <td>{message.content}</td>
                <td>{new Date(message.dateOfPost).toLocaleString()}</td>
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
