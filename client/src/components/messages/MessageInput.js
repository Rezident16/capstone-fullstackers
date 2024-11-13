import { useState } from "react";
import { useUser } from "../context/UserContext";

function MessageInput({ stockId, onMessagePosted }) {
  const [messageContent, setMessageContent] = useState("");
  const { userId, jwtToken } = useUser(); 


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
        dateOfPost: new Date().toISOString(), 
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
            onMessagePosted(data); 
          }
          setMessageContent(""); 
        })
        .catch(console.error);
    }
  };
  

  console.log("User Context: ", { userId, jwtToken });

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
