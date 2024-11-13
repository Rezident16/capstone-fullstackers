import { useEffect } from "react";
import { useState } from "react";
import { useParams } from "react-router-dom";
import MessageList from "../messages/MessageList";
import Graph from "../graph/Graph";


function Stock() {

    const stockId = useParams().stockId;

    const [stock, setStock] = useState(null);
    const url = `http://localhost:8080/api/stocks/${stockId}`;

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
          <h2>{stock.stockName}</h2>
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
