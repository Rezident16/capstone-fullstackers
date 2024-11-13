import { useEffect, useState } from "react";

function StockList({ onSelectStock }) {
  const [stocks, setStocks] = useState([]);
  const url = `http://localhost:8080/api/stocks`;

  // Fetch all stocks
  useEffect(() => {
    fetch(url)
      .then(response => response.json())
      .then(data => setStocks(data))
      .catch(console.error);
  }, []);

  return (
    <ul className="list-group">
      {stocks.map(stock => (
        <li key={stock.stockId} className="list-group-item d-flex justify-content-between align-items-center">
          <button
            onClick={() => onSelectStock(stock.stockId)}
            className="btn btn-primary btn-block text-left"
            style={{ textAlign: 'left', width: '100%' }}
          >
            {stock.stockName}
          </button>
        </li>
      ))}
    </ul>
  );
}

export default StockList;
