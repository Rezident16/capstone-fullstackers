import React, { useEffect, useState } from "react";
import MessageInput from "./MessageInput"; // Assuming you have a MessageInput component

const MessageList = ({ stockId, userId }) => {
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

  if (!messages) {
    return <div>Loading...</div>;
  }

  if (messages.length) {
    console.log(messages[0]['appUser']['username'])
  
  }


  return (
    <section className="container-fluid pl-0">
      <MessageInput stockId={stockId} userId={userId} onMessagePosted={handleNewMessage} />

      {messages.length > 0 ? (
        <div className="message-list">
          {messages.map(message => (
            <div key={message.messageId} className="card mb-3">
              <div className="card-body">
                {message.appUser && message.appUser.username ? (
                  <h5 className="card-title">@{message.appUser.username}</h5>
                ) : null}
                <p className="card-text">{message.content}</p>
                <p className="card-text">
                  <small className="text-muted">Posted on {new Date(message.dateOfPost).toLocaleString()}</small>
                </p>
              </div>
            </div>
          ))}
        </div>
      ) : (
        null
      )}
    </section>
  );
};

export default MessageList;
