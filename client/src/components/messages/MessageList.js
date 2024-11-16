import React, { useEffect, useState } from "react";
import MessageInput from "./MessageInput";
import { w3cwebsocket as W3CWebSocket } from "websocket";
import { useUser } from "../context/UserContext";
import thumbsUp from "../../assets/thumbsUp.png";
import thumbsUpF from "../../assets/thumbsUpF.png";
import thumbsDown from "../../assets/thumbsDown.png";
import thumbsDownF from "../../assets/thumbsDownF.png";
import { useNavigate } from "react-router-dom";

const MessageList = ({ stockId }) => {
  const [messages, setMessages] = useState([]);
  const [userLikes, setUserLikes] = useState([]);
  const [messageLikes, setMessageLikes] = useState({});
  const [received, setReceived] = useState(null);
  const [isUpdate, setIsUpdate] = useState(false);
  const [updateMessageId, setupdateMessageId] = useState(0);
  const [messageContent, setMessageContent] = useState("");
  const navigate = useNavigate();
  const baseUrl = process.env.NODE_ENV === "production" ? "https://stockconnect.onrender.com" : "http://localhost:8080";
  
  const { userId, jwtToken } = useUser();

  // Fetch messages only if stockId is provided
  useEffect(() => {
    if (stockId) {
      const url = `${baseUrl}/api/message/stocks/${stockId}`;
      console.log(baseUrl)
      console.log(url)
      fetch(url)
        .then((response) => response.json())
        .then((data) => {
          console.log(data)
          if (data && data.length) {
          const sortedMessages = data.sort(
            (a, b) => new Date(b.dateOfPost) - new Date(a.dateOfPost)
          )
          setMessages(sortedMessages);
        };
        })
        .catch(console.error);
    }
  }, [stockId, received]);

  // Fetch user likes on component mount
  useEffect(() => {
    if (userId) {
      const url = `${baseUrl}/api/message/like/user/${userId}`;
      fetch(url, {
        headers: { Authorization: `Bearer ${jwtToken}` },
      })
        .then((response) => response.json())
        .then((data) => setUserLikes(data))
        .catch(console.error);
    }
  }, [userId, jwtToken]);

  // Fetch likes count for each message
  useEffect(() => {
    const fetchLikesForMessages = async () => {
      const likesData = {};
      for (const message of messages) {
        const response = await fetch(
          `${baseUrl}/api/message/like/${message.messageId}`
        );
        if (response.ok) {
          const data = await response.json();
          const likeCount = data.filter((like) => like.liked).length;
          const dislikeCount = data.filter((like) => !like.liked).length;
          likesData[message.messageId] = { likeCount, dislikeCount };
        }
      }
      setMessageLikes(likesData);
    };

    if (messages.length) {
      fetchLikesForMessages();
    }
  }, [messages]);

  const handleNewMessage = (newMessage) => {
    setMessages((prevMessages) => [newMessage, ...prevMessages]);
  };

  useEffect(() => {
    const client = process.env.NODE_ENV === "production" ? new W3CWebSocket(`wss://stockconnect.onrender.com/stocks`) : new W3CWebSocket(`ws://localhost:8080/stocks`);

    client.onopen = () => {
      console.log("Connected to the WebSocket server");
    };

    client.onmessage = async (message) => {
      setReceived(message);
      navigate(`/stock/${stockId}`);
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
      `${baseUrl}/api/message/${messageId}`,
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
      `${baseUrl}/api/message/${updateMessageId}`,
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
    let updatedLikesData = { ...messageLikes };

    if (existingLike) {
      if (likedStatus === "like") {
        if (existingLike.liked === true) {
          // If message is liked, and user clicks like again, delete the like
          const response = await fetch(
            `${baseUrl}/api/message/like/id/${existingLike.likeId}`,
            {
              method: "DELETE",
              headers: {
                Authorization: `Bearer ${jwtToken}`,
              },
            }
          );

          if (response.ok) {
            setUserLikes((prevLikes) =>
              prevLikes.filter((like) => like.likeId !== existingLike.likeId)
            );
            updatedLikesData[messageId].likeCount--;
          }
        } else if (existingLike.liked === false) {
          // If message is disliked, and user clicks like, update to like
          const updatedLike = { ...existingLike, liked: true };

          const response = await fetch(
            `${baseUrl}/api/message/like/id/${existingLike.likeId}`,
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
            setUserLikes((prevLikes) =>
              prevLikes.map((like) =>
                like.likeId === existingLike.likeId
                  ? { ...like, liked: true }
                  : like
              )
            );
            updatedLikesData[messageId].likeCount++;
            updatedLikesData[messageId].dislikeCount--;
          }
        }
      } else if (likedStatus === "dislike") {
        if (existingLike.liked === false) {
          // If message is disliked, and user clicks dislike again, delete the dislike
          const response = await fetch(
            `${baseUrl}/api/message/like/id/${existingLike.likeId}`,
            {
              method: "DELETE",
              headers: {
                Authorization: `Bearer ${jwtToken}`,
              },
            }
          );

          if (response.ok) {
            setUserLikes((prevLikes) =>
              prevLikes.filter((like) => like.likeId !== existingLike.likeId)
            );
            updatedLikesData[messageId].dislikeCount--;
          }
        } else if (existingLike.liked === true) {
          // If message is liked, and user clicks dislike, update to dislike
          const updatedLike = { ...existingLike, liked: false };

          const response = await fetch(
            `${baseUrl}/api/message/like/id/${existingLike.likeId}`,
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
            setUserLikes((prevLikes) =>
              prevLikes.map((like) =>
                like.likeId === existingLike.likeId
                  ? { ...like, liked: false }
                  : like
              )
            );
            updatedLikesData[messageId].likeCount--;
            updatedLikesData[messageId].dislikeCount++;
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

      const response = await fetch(`${baseUrl}/api/message/like`, {
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

        if (likedStatus === "like") {
          updatedLikesData[messageId].likeCount++;
        } else {
          updatedLikesData[messageId].dislikeCount++;
        }
      }
    }

    setMessageLikes(updatedLikesData);
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
                <>
                  <div className="card-body">
                    {message.appUser && message.appUser.username ? (
                      <h5 className="card-title">
                        @{message.appUser.username}
                      </h5>
                    ) : null}
                    <p className="card-text">{message.content}</p>
                    <p className="card-text">
                      <small className="text-muted">
                        Posted on{" "}
                        {new Date(message.dateOfPost).toLocaleString()}
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
                  {userId ? (
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
                      style={{
                        width: "24px",
                        cursor: "pointer",
                        marginLeft: "16px",
                        marginRight: "8px",
                      }}
                      onClick={() => toggleLike(message.messageId, "like")}
                    />
                    <span>
                      {messageLikes[message.messageId]?.likeCount || 0}
                    </span>

                    <img
                      src={
                        isLikedByUser(message.messageId) === false
                          ? thumbsDownF
                          : thumbsDown
                      }
                      alt="Dislike"
                      style={{
                        width: "24px",
                        cursor: "pointer",
                        marginLeft: "16px",
                      }}
                      onClick={() => toggleLike(message.messageId, "dislike")}
                    />
                    <span style={{ marginLeft: "8px", marginRight: "16px" }}>
                      {messageLikes[message.messageId]?.dislikeCount || 0}
                    </span>
                  </div>
                  ) : (
                    null
                  )}
                  
                </>
              )}
            </div>
          ))}
        </div>
      ) : null}
    </section>
  );
};
export default MessageList;
