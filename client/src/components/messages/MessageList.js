import React, { useEffect, useState } from "react";
import MessageInput from "./MessageInput";
import { w3cwebsocket as W3CWebSocket } from "websocket";
import { useUser } from "../context/UserContext";
import thumbsUp from '../../assets/thumbsUp.png';
import thumbsUpF from '../../assets/thumbsUpF.png';
import thumbsDown from '../../assets/thumbsDown.png';
import thumbsDownF from '../../assets/thumbsDownF.png';

const MessageList = ({ stockId }) => {
  const [messages, setMessages] = useState([]);
  const [userLikes, setUserLikes] = useState([]);
  const [received, setReceived] = useState(null);
  const [isUpdate, setIsUpdate] = useState(false);
  const [updateMessageId, setupdateMessageId] = useState(0);
  const [messageContent, setMessageContent] = useState("");

  const { userId, jwtToken } = useUser();

  // Fetch messages only if stockId is provided
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
  }, [stockId, received]);

  // Fetch user likes on component mount
  useEffect(() => {
    if (userId) {
      const url = `http://localhost:8080/api/message/like/user/${userId}`;
      fetch(url, {
        headers: { Authorization: `Bearer ${jwtToken}` },
      })
        .then((response) => response.json())
        .then((data) => setUserLikes(data))
        .catch(console.error);
    }
  }, [userId, jwtToken]);

  const handleNewMessage = (newMessage) => {
    setMessages((prevMessages) => [newMessage, ...prevMessages]);
  };

  useEffect(() => {
    const client = new W3CWebSocket("ws://localhost:8080/stocks");

    client.onopen = () => {
      console.log("Connected to the WebSocket server");
    };

    client.onmessage = async (message) => {
      setReceived(message);
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
  };

  const isLikedByUser = (messageId) => {
    const like = userLikes.find((like) => like.messageId === messageId);
    return like ? like.liked : null; // returns true for like, false for dislike, and null if no action
  };
  
  const toggleLike = async (messageId, likedStatus) => {
    const existingLike = userLikes.find((like) => like.messageId === messageId);
  
    if (existingLike) {
      if (likedStatus === "like") {
        if (existingLike.liked === true) {
          // If message is liked, and user clicks like again, delete the like
          const response = await fetch(
            `http://localhost:8080/api/message/like/id/${existingLike.likeId}`,
            {
              method: "DELETE",
              headers: {
                Authorization: `Bearer ${jwtToken}`,
              },
            }
          );
  
          if (response.ok) {
            // Remove the like from frontend state
            setUserLikes((prevLikes) =>
              prevLikes.filter((like) => like.likeId !== existingLike.likeId)
            );
          }
        } else if (existingLike.liked === false) {
          // If message is disliked, and user clicks like, update the like to true (like the message)
          const updatedLike = { ...existingLike, liked: true };
  
          const response = await fetch(
            `http://localhost:8080/api/message/like/id/${existingLike.likeId}`,
            {
              method: "PUT",
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${jwtToken}`,
              },
              body: JSON.stringify(updatedLike),
            }
          );
  
          if (response.ok) {
            // Update the like status in frontend state
            setUserLikes((prevLikes) =>
              prevLikes.map((like) =>
                like.likeId === existingLike.likeId ? { ...like, liked: true } : like
              )
            );
          }
        }
      } else if (likedStatus === "dislike") {
        if (existingLike.liked === false) {
          // If message is disliked, and user clicks dislike again, delete the dislike
          const response = await fetch(
            `http://localhost:8080/api/message/like/id/${existingLike.likeId}`,
            {
              method: "DELETE",
              headers: {
                Authorization: `Bearer ${jwtToken}`,
              },
            }
          );
  
          if (response.ok) {
            // Remove the dislike from frontend state
            setUserLikes((prevLikes) =>
              prevLikes.filter((like) => like.likeId !== existingLike.likeId)
            );
          }
        } else if (existingLike.liked === true) {
          // If message is liked, and user clicks dislike, update the like to false (dislike the message)
          const updatedLike = { ...existingLike, liked: false };
  
          const response = await fetch(
            `http://localhost:8080/api/message/like/id/${existingLike.likeId}`,
            {
              method: "PUT",
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${jwtToken}`,
              },
              body: JSON.stringify(updatedLike),
            }
          );
  
          if (response.ok) {
            // Update the like status in frontend state
            setUserLikes((prevLikes) =>
              prevLikes.map((like) =>
                like.likeId === existingLike.likeId ? { ...like, liked: false } : like
              )
            );
          }
        }
      }
    } else {
      // If no like exists, create a new like or dislike
      const newLike = {
        userId,
        messageId,
        liked: likedStatus === "like" ? true : false,
      };
  
      const response = await fetch(`http://localhost:8080/api/message/like`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwtToken}`,
        },
        body: JSON.stringify(newLike),
      });
  
      if (response.ok) {
        const addedLike = await response.json();
        setUserLikes((prevLikes) => [...prevLikes, addedLike]);
      }
    }
  };
  


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
                  {/* Update form code */}
                </div>
              ) : (
                <>
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
  
                  {/* Subcard section for like and dislike */}
                  <div
                    className="subcard d-flex justify-content-start align-items-center p-2"
                    style={{
                      borderTop: "1px solid #ddd",
                      backgroundColor: "#f8f9fa",
                      borderBottomLeftRadius: "4px",
                      borderBottomRightRadius: "4px",
                    }}
                  >
                  <img
                    src={
                      isLikedByUser(message.messageId) === true
                        ? thumbsUpF
                        : thumbsUp
                    }
                    alt="Like"
                    style={{ width: "24px", cursor: "pointer", marginRight: "16px" }}
                    onClick={() => toggleLike(message.messageId, "like")}
                  />
                  <img
                    src={
                      isLikedByUser(message.messageId) === false
                        ? thumbsDownF
                        : thumbsDown
                    }
                    alt="Dislike"
                    style={{ width: "24px", cursor: "pointer" }}
                    onClick={() => toggleLike(message.messageId, "dislike")}
                  />
                  </div>
                </>
              )}
            </div>
          ))}
        </div>
      ) : null}
    </section>
  );
}  
export default MessageList;
