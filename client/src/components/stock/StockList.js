import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useUser } from "../context/UserContext";

function StockList({ onSelectStock }) {
  const [stocks, setStocks] = useState([]);
  const url = `http://localhost:8080/api/stocks`;
  const navigate = useNavigate();

  const {userId} = useUser();
  const [admin, setAdmin] = useState(false);

  useEffect(() => {
    if (userId) {
      fetch(`http://localhost:8080/api/user/${userId}`)
        .then((response) => response.json())
        .then((data) => {
          console.log(data)
          setAdmin(data.roleId == 1);
        })
        .catch(console.error);
      }
    }, [userId]);
    
    console.log(admin)
  // Fetch all stocks
  useEffect(() => {
    fetch(url)
      .then((response) => response.json())
      .then((data) => setStocks(data))
      .catch(console.error);
  }, []);

  return (
    <ul className="list-group nav nav-pills mb-auto">
      {admin ? (
      <div className="list-group-item nav-item">
        <Link to="/stock/add" className="btn btn-info btn-block text-left">
          Add Stock
        </Link>
      </div>
      ) : (null)}
      {stocks.map((stock) => (
        <li key={stock.stockId} className="list-group-item nav-item">
          <button
            onClick={() => navigate(`/stock/${stock.stockId}`)}
            className="btn btn-primary btn-block text-left"
            style={{ textAlign: "left", width: "100%" }}
          >
            {stock.stockName}
          </button>
        </li>
      ))}
    </ul>
  );
}

export default StockList;
