import { useState } from "react";
import { useUser } from "../context/UserContext";

function MessageInput({ stockId, onMessagePosted }) {
  const [messageContent, setMessageContent] = useState("");
  const { userId, jwtToken } = useUser(); 


  const handleChange = (event) => {
    setMessageContent(event.target.value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
  
    if (messageContent.trim() && userId) {
      const newMessage = {
        content: messageContent,
        stockId,
        userId, 
        dateOfPost: new Date().toISOString(), 
      };
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
            console.log(data);
            onMessagePosted(data); 
          }
          setMessageContent(""); 
        })
        .catch(console.error);
    }
  };
  

  return (
    <div>
      {jwtToken ? (
        <div className="message-input-container">
          <form onSubmit={handleSubmit}>
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
            <button type="submit" className="btn btn-primary mb-5 pl-4 pr-4">
              Post
            </button>
          </form>
        </div>
      ) : (null)
      }
    </div>
  );
}

export default MessageInput;