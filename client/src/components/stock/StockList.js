import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useUser } from "../context/UserContext";

function StockList({ onSelectStock }) {
  const { userId } = useUser();
  const [stocks, setStocks] = useState([]);
  const [favoritedStocks, setFavoritedStocks] = useState([]);
  const [admin, setAdmin] = useState(false);

  const url = `http://localhost:8080/api/stocks`;
  const favoritesUrl = `http://localhost:8080/api/user-stocks/favorites/${userId}`;
  const favoriteStockUrl = `http://localhost:8080/api/user-stocks`;
  const unfavoriteStockUrl = `http://localhost:8080/api/user-stocks`;

  const navigate = useNavigate();

  // Fetch user role for admin privileges

  useEffect(() => {
    if (userId) {
      fetch(`http://localhost:8080/api/user/${userId}`)
        .then((response) => response.json())
        .then((data) => {
          console.log(data);
          if (data.roleId == 1) {
            setAdmin(true);
          }
        })
        .catch(console.error);
    }
  }, [userId]);

  useEffect(() => {
    fetch(url)
      .then((response) => response.json())
      .then((data) => setStocks(data))
      .catch(console.error);
  }, []);

  // Fetch favorited stocks for the logged-in user
  useEffect(() => {
    if (userId) {
      fetch(favoritesUrl)
        .then((response) => response.json())
        .then((data) => {
          const formattedFavorites = data.map((item) => ({
            userStockId: item[0],
            stockId: item[1],
            stockName: item[2],
            stockDescription: item[3],
            ticker: item[4],
          }));
          setFavoritedStocks(formattedFavorites);
        })
        .catch(console.error);
    }
  }, [userId]);

  // Handle favoriting a stock
  const handleFavorite = (stockId) => {
    if (!userId) {
      console.log("User is not logged in");
      return;
    }

    const stockToFavorite = { userId, stockId };

    fetch(favoriteStockUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(stockToFavorite),
    })
      .then((response) => response.json())
      .then((data) => {
        setFavoritedStocks((prev) => [...prev, data]); // Add to favorited stocks
        setStocks((prevStocks) =>
          prevStocks.map((stock) =>
            stock.stockId === data.stockId
              ? { ...stock, isFavorited: true }
              : stock
          )
        ); // Mark stock as favorited in all stocks
      })
      .catch(console.error);
    window.location.reload();
  };

  // Handle unfavoriting a stock
  const handleUnfavorite = (userStockId) => {
    fetch(`${unfavoriteStockUrl}/${userStockId}`, {
      method: "DELETE",
    })
      .then((response) => {
        if (response.ok) {
          setFavoritedStocks((prev) =>
            prev.filter((stock) => stock.userStockId !== userStockId)
          ); // Remove from favorited stocks
          setStocks((prevStocks) =>
            prevStocks.map((stock) =>
              stock.userStockId === userStockId
                ? { ...stock, isFavorited: false }
                : stock
            )
          ); // Mark stock as unfavorited in all stocks
        } else {
          console.error("Error unfavoriting stock");
        }
      })
      .catch(console.error);
    window.location.reload();
  };

  // Combine favorited stocks with all stocks but make sure non-favorited stocks only appear in the "All Stocks" section
  const nonFavoritedStocks = stocks.filter(
    (stock) => !favoritedStocks.some((fav) => fav.stockId === stock.stockId)
  );

  return (
    <div>
      {admin && (
        <div className="list-group-item nav-item">
          <Link to="/stock/add" className="btn btn-info btn-block text-left">
            Add Stock
          </Link>
        </div>
      )}

      <ul className="list-group nav nav-pills mb-auto"
      >
        {/* Favorited Stocks - Displayed Above All Stocks */}
        {favoritedStocks.length > 0 && (
          <>
            <h5>Favorited Stocks</h5>
            {favoritedStocks.map((stock) => (
              <li
                key={stock.stockId}
                className="list-group-item d-flex justify-content-between align-items-center mb-3"
              >
                <button
                  onClick={() => navigate(`/stock/${stock.stockId}`)}
                  className="btn btn-primary btn-block text-left"
                  style={{ textAlign: "left", width: "80%" }}
                >
                  {stock.stockName}
                </button>
                {/* Unfavorite Button */}
                <button
                  onClick={() => handleUnfavorite(stock.userStockId)}
                  className="btn btn-outline-danger btn-sm ml-2 d-flex align-items-center justify-content-center"
                  style={{ width: "15%" }}
                >
                  <i className="fas fa-times"></i>
                </button>
              </li>
            ))}
          </>
        )}

        {/* All Stocks - Below Favorited Stocks */}
        {nonFavoritedStocks.length > 0 && (
          <>
            <h5>All Stocks</h5>
            {nonFavoritedStocks.map((stock) => (
              <li
                key={stock.stockId}
                className="list-group-item d-flex justify-content-between align-items-center mb-3"
              >
                <button
                  onClick={() => navigate(`/stock/${stock.stockId}`)}
                  className="btn btn-primary btn-block text-left"
                  style={{ textAlign: "left", width: "80%" }}
                >
                  {stock.stockName}
                </button>
                {/* Favorite Button */}
                <button
                  onClick={() => handleFavorite(stock.stockId)}
                  className="btn btn-danger btn-sm ml-2 d-flex align-items-center justify-content-center"
                  style={{ width: "15%" }}
                >
                  <i className="fas fa-heart"></i>
                </button>
              </li>
            ))}
          </>
        )}
      </ul>
    </div>
  );
}

export default StockList;
