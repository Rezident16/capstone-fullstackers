import { useEffect } from "react";
import { useState } from "react";
import { useParams } from "react-router-dom";
import MessageList from "../messages/MessageList";
import Graph from "../graph/Graph";
import { useUser } from "../context/UserContext";


function Stock() {

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

    if (!stock) {
        return <div>Loading...</div>
    }

    return(<div className="main-content p-4" style={{ flex: 1 }}>
        <div className="stock-price mb-4">
            {admin ? (<button className="btn btn-primary">Edit</button>) : (null)}
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
