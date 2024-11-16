import { useEffect, useState } from "react";
import { useUser } from "../context/UserContext";

const DEFAULT_LIKE = {
    userId: 0,
    messageId: 0,
    isLiked: false,
    
}


function Likes({ message }) {
    const [likes, setLikes] = useState(message.likes);
    const [liked, setLiked] = useState(false);
    const [userHasLike, setUserHasLike] = useState(false);
    const [likeColor, setLikeColor] = useState("grey");
    const [dislikeColor, setDislikeColor] = useState("grey");
    const [curr, setCurr] = useState(DEFAULT_LIKE);
    const baseUrl = process.env.NODE_ENV === "production" ? "https://stockconnect.onrender.com" : "http://localhost:8080";


  const { userId, jwtToken } = useUser();

  const [isLikedCount, setIsLikedCount] = useState(0)
    const [isDislikedCount, setIsDislikedCount] = useState(0)

    
    
    useEffect(() => {
        if (likes && likes.length) {
            for (let like of likes) {
                if (like.userId == userId) {
                    setUserHasLike(true);
                    setLiked(like.liked);
                }
            }
        }
    }, [likes, userId]);
    
    useEffect(() => {
        if (likes) {
            setIsDislikedCount(likes.filter(like => like.liked == false).length)
            setIsLikedCount(likes.filter(like => like.liked == true).length)
        }
        if (userHasLike) {
            if (liked) {
                setLikeColor("green");
      } else {
            setDislikeColor("red");
      }
    }
  }, [userHasLike, liked]);

  console.log(isDislikedCount, isLikedCount)

  const handleCreateLike = (likeType) => {
    const requestBody = {
        liked: likeType == true,
        messageId: parseInt(message.messageId),
        userId,
      };

      console.log("Request Body:", requestBody); // Log the request body

    fetch(`${baseUrl}/api/message/like`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestBody),
    })
      .then((response) => response.json())
      .then((data) => {
        setLikes(data.likes);
        setLiked(likeType);
        setUserHasLike(true);
      })
      .catch(console.error);
  };

  const handleUpdateLike = (isLiked) => {
    console.log("Updating like");
    fetch(`${baseUrl}/api/message/${message.messageId}/like/${userId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        userId,
      }),
    })
    //   .then((response) => response.json())
      .then((data) => {
        setLikes(data.likes);
        setLiked(isLiked);
      })
      .catch(console.error);
  };

  const handleDelete = () => {
    console.log("Delete like");
    fetch(`${baseUrl}/api/message/${message.messageId}/like/${userId}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        userId: userId
      }),
    })
  };

  
  const handleClick = (isTrue) => {
    if (!userHasLike) {
      handleCreateLike(isTrue);
      if (isTrue) {
        setIsLikedCount(isLikedCount + 1)
      } else {
        setIsDislikedCount(isDislikedCount + 1)
      }
    } else {
      if (liked == isTrue) {
        handleDelete();
        setUserHasLike(false)
        if (likeColor == "green") {
            setIsLikedCount(isLikedCount - 1)
        } else {
            setIsDislikedCount(isDislikedCount - 1)
        }
        setLikeColor("grey")
        setDislikeColor("grey")
      } else {
        handleUpdateLike(isTrue);
        if (likeColor == "green") {
            setLikeColor('grey')
            setDislikeColor('red')
            setIsDislikedCount(isDislikedCount + 1)
            setIsLikedCount(isLikedCount - 1)
        } else {
            setDislikeColor('grey')
            setLikeColor('green')
            setIsDislikedCount(isDislikedCount - 1)
            setIsLikedCount(isLikedCount + 1)
        }
      }
    }
  };

  return (
    <div className="d-flex gap-3">
      <i
        className="fas fa-thumbs-down"
        style={{ cursor: "pointer", color: dislikeColor }}
        onClick={() => handleClick(false)}
      ></i>
        <span>{isDislikedCount}</span>

      <i
        className="fas fa-thumbs-up"
        style={{ cursor: "pointer", color: likeColor }}
        onClick={() => handleClick(true)}
      ></i>
        <span>{isLikedCount}</span>
    </div>
  );
}

export default Likes;
