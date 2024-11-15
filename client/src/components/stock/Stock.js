import { useEffect } from "react";
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import MessageList from "../messages/MessageList";
import Graph from "../graph/Graph";
import { useUser } from "../context/UserContext";


function Stock() {

    const navigate = useNavigate();
    const stockId = useParams().stockId;

    const [stock, setStock] = useState(null);
    const url = `http://localhost:8080/api/stocks/${stockId}`;

    const {userId} = useUser();
    const [admin, setAdmin] = useState(false);

    

  useEffect(() => {
    if (userId) {
      fetch(`http://localhost:8080/api/user/${userId}`)
        .then((response) => response.json())
        .then((data) => {
          console.log(data)
          if (data.roleId == 1) {
            setAdmin(true);
          }
        })
        .catch(console.error);
      }
    }, [userId]);
    

    useEffect(() => {
        fetch(url)
            .then(response => response.json())
            .then(data => setStock(data))
            .catch(console.error);
    },[stockId])
    

    const handleDeleteStock = () => {
      const isConfirmed = window.confirm("Are you sure you want to delete this stock?");
      if (isConfirmed) {
          fetch(`http://localhost:8080/api/stocks/${stockId}`, {
              method: 'DELETE',
          })
              .then((response) => {
                  if (response.ok) {
                      console.log(`Stock with ID ${stockId} deleted successfully`);
                      navigate('/');
                  } else {
                      console.error("Failed to delete stock");
                  }
              })
              .catch((error) => {
                  console.error("Error deleting stock:", error);
              });
      } else {
          console.log("Stock deletion canceled.");
      }
  };
  

    if (!stock) {
        return <div>Loading...</div>
    }

    const handleDelete = async () => {
        const init = {
            method: 'DELETE',
        }

        const response = await fetch(`http://localhost:8080/api/stocks/${stockId}`, init);
        if (response.ok) {
            navigate("/stock");
        }
    }

    return(<div className="main-content p-4" style={{ flex: 1 }}>
        <div className="stock-price mb-4">
            {admin ? (<button className="btn btn-primary" onClick={() => navigate(`/stock/${stockId}/edit`)}>Edit</button>) : (null)}
            {admin ? (<button className="btn btn-danger ml-2" onClick={() => handleDeleteStock(stockId)}>Delete</button>) : (null)}
        </div>

        <div className="stock-chart mb-4">
          <Graph stock={stock.ticker}></Graph>
        </div>

        <div className="posts">
          <MessageList stockId={stockId} />
        </div>
      </div>)
}

export default Stock;
