import React, { useEffect, useState } from "react";
import MessageInput from "./MessageInput"; // Assuming you have a MessageInput component
import { w3cwebsocket as W3CWebSocket } from "websocket";


const MessageList = ({ stockId, userId }) => {
  const [messages, setMessages] = useState([]);
  const [recieved, setRecieved] = useState(null);

  // Fetch messages only if stockId is provided
  useEffect(() => {
    if (stockId) {
      const url = `http://localhost:8080/api/message/stock/${stockId}`;
      fetch(url)
        .then(response => response.json())
        .then(data => {
          const sortedMessages = data.sort((a, b) => new Date(b.dateOfPost) - new Date(a.dateOfPost));
          setMessages(sortedMessages);
        })
        .catch(console.error);
    }
  }, [stockId, recieved]);

  const handleNewMessage = (newMessage) => {
    setMessages(prevMessages => [newMessage, ...prevMessages]);
  };

  useEffect(() => {
    const client = new W3CWebSocket('ws://localhost:8080/stocks');

    client.onopen = () => {
      console.log("Connected to the WebSocket server");
    };

    client.onmessage = async (message) => {
      setRecieved(message)
    };

    client.onclose = () => {
      console.log("Disconnected from the WebSocket server");
    };

    return () => {
      client.close();
    };
  }, [stockId]);

  if (!messages) {
    return <div>Loading...</div>;
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
