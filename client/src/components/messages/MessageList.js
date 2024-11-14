import React, { useEffect, useState } from "react";
import MessageInput from "./MessageInput"; // Assuming you have a MessageInput component
import { w3cwebsocket as W3CWebSocket } from "websocket";
import { useUser } from "../context/UserContext";

const MessageList = ({ stockId }) => {
  const [messages, setMessages] = useState([]);
  const [recieved, setRecieved] = useState(null);
  const [isUpdate, setIsUpdate] = useState(false);
  const [updateMessageId, setupdateMessageId] = useState(0);
  const [messageContent, setMessageContent] = useState("");

  // Fetch messages only if stockId is provided
  const { userId, jwtToken } = useUser();
  useEffect(() => {
    if (stockId) {
      const url = `http://localhost:8080/api/message/stock/${stockId}`;
      fetch(url)
        .then((response) => response.json())
        .then((data) => {
          const sortedMessages = data.sort(
            (a, b) => new Date(b.dateOfPost) - new Date(a.dateOfPost)
          );
          setMessages(sortedMessages);
        })
        .catch(console.error);
    }
  }, [stockId, recieved]);

  const handleNewMessage = (newMessage) => {
    setMessages((prevMessages) => [newMessage, ...prevMessages]);
  };

  useEffect(() => {
    const client = new W3CWebSocket("ws://localhost:8080/stocks");

    client.onopen = () => {
      console.log("Connected to the WebSocket server");
    };

    client.onmessage = async (message) => {
      setRecieved(message);
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

  const handleDelete = async (messageId) => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this message?"
    );
    if (!confirmed) {
      return;
    }

    const init = {
      method: "DELETE",
    };

    const response = await fetch(
      `http://localhost:8080/api/message/${messageId}`,
      init
    );
    if (response.ok) {
      setMessages((prevMessages) =>
        prevMessages.filter((message) => message.messageId !== messageId)
      );
    }
  };

  const handleChange = (event) => {
    setMessageContent(event.target.value);
  };

  const handleUpdate = async (event) => {
    event.preventDefault();
    const updatedMessage = {
      content: messageContent,
      stockId,
      userId,
      messageId: updateMessageId,
      dateOfPost: new Date().toISOString(),
    };

    const init = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(updatedMessage),
    };

    const response = await fetch(
      `http://localhost:8080/api/message/${updateMessageId}`,
      init
    );
    if (response.ok) {
      setIsUpdate(false);
      setupdateMessageId(0);
      setMessageContent("");
    } else {
      const errors = await response.json();
      console.error(errors);
    }
  }

  return (
    <section className="container-fluid pl-0">
      <MessageInput
        stockId={stockId}
        userId={userId}
        onMessagePosted={handleNewMessage}
      />

      {messages.length > 0 ? (
        <div className="message-list">
          {messages.map((message) => (
            <div key={message.messageId} className="card mb-3">
              {isUpdate & (message.messageId == updateMessageId) ? (
                <div className="message-input-container p-3">
                  <form onSubmit={handleUpdate}>
                    <div className="form-group">
                      <textarea
                        className="form-control"
                        placeholder="Post your thoughts here..."
                        value={messageContent}
                        onChange={handleChange}
                        rows="4"
                        required
                      />
                    </div>
                    <button
                      type="submit"
                      className="btn btn-primary mb-5 pl-4 pr-4"
                    >
                      Update
                    </button>
                  </form>
                </div>
              ) : (
                <div className="card-body">
                  {message.appUser && message.appUser.username ? (
                    <h5 className="card-title">@{message.appUser.username}</h5>
                  ) : null}
                  <p className="card-text">{message.content}</p>
                  <p className="card-text">
                    <small className="text-muted">
                      Posted on {new Date(message.dateOfPost).toLocaleString()}
                    </small>
                  </p>
                  <div>
                    {message.userId == userId ? (
                      <button
                        className="btn btn-warning mr-4"
                        onClick={() => {
                          setIsUpdate(true);
                          setupdateMessageId(message.messageId);
                          setMessageContent(message.content);
                        }}
                      >
                        Edit
                      </button>
                    ) : null}
                    {message.userId == userId ? (
                      <button
                        className="btn btn-danger"
                        onClick={() => handleDelete(message.messageId)}
                      >
                        Delete
                      </button>
                    ) : null}
                  </div>
                </div>
              )}
            </div>
          ))}
        </div>
      ) : null}
    </section>
  );
};

export default MessageList;
