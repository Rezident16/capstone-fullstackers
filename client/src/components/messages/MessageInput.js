import { useState, useEffect } from "react";

function MessageInput({ stockId, onMessagePosted }) {
  const [messageContent, setMessageContent] = useState("");
  const [userId, setUserId] = useState(null);

  // Extract userId from JWT token in localStorage or context
  useEffect(() => {
    const token = localStorage.getItem("jwt_token"); // Assuming token is stored in localStorage
    if (token) {
      const userIdFromToken = extractUserIdFromToken(token);
      setUserId(userIdFromToken); // Set userId
    }
  }, []);

  // Extract userId from JWT token
  const extractUserIdFromToken = (token) => {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const decodedData = JSON.parse(atob(base64));
    return decodedData.user_id; // Return the user_id as a string
  };

  // Handle change in input field
  const handleChange = (event) => {
    setMessageContent(event.target.value);
  };

  // Handle form submission to post a new message
  const handleSubmit = (event) => {
    event.preventDefault();

    if (messageContent.trim() && userId) {
      const newMessage = {
        content: messageContent,
        stockId,
        userId,
        dateOfPost: new Date().toISOString(), // Timestamp of the post
      };

      // Send POST request to the backend to create a new message
      fetch("http://localhost:8080/api/message", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newMessage),
      })
        .then((response) => response.json())
        .then((data) => {
          if (onMessagePosted) {
            onMessagePosted(data); // Notify parent component (MessageList) of the new message
          }
          setMessageContent(""); // Clear the input after posting
        })
        .catch(console.error);
    }
  };

  return (
    <div className="message-input-container">
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <textarea
            className="form-control"
            placeholder="Type your message here"
            value={messageContent}
            onChange={handleChange}
            rows="4"
            required
          />
        </div>
        <button type="submit" className="btn btn-primary">
          Post Message
        </button>
      </form>
    </div>
  );
}

export default MessageInput;
