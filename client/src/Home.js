import React, { useState } from 'react';
import MessageList from './components/messages/MessageList';
import StockList from './components/stock/StockList';
import AddStocks from './components/AddStock';

const Home = () => {
  const [stockPrice, setStockPrice] = useState(150);
  const [selectedStockId, setSelectedStockId] = useState(null);
  const [posts, setPosts] = useState([
    "Stock 1 is doing great today!",
    "Considering buying more Stock 1 soon.",
    "Stock 1 hit a new high this week!"
  ]);

  // Method to handle stock selection
  const onSelectStock = (stockId) => {
    setSelectedStockId(stockId);
  };

  return (
    <div className="d-flex">
      {/* Sidebar */}
      <div className="sidebar p-3 bg-light" style={{ width: '200px' }}>
        <AddStocks />
        <h4>Stocks</h4>
        {/* Pass onSelectStock method to StockList */}
        <StockList onSelectStock={onSelectStock} />
      </div>

      {/* Main Content */}
      <div className="main-content p-4" style={{ flex: 1 }}>
        {/* Stock Price */}
        <div className="stock-price mb-4">
          <h2>Stock 1 Price: ${stockPrice}</h2>
        </div>

        {/* Stock Chart Section */}
        <div className="stock-chart mb-4">
          <img
            src="https://www.investopedia.com/thmb/5DuVJbOwz12uf88m9DdyTJ-My-k=/750x0/filters:no_upscale():max_bytes(150000):strip_icc()/ScreenShot2019-08-28at1.59.03PM-2e8cb1195471423392644ee65bf2ca31.png"
            alt="Stock 1 Chart Placeholder"
            className="img-fluid"
          />
        </div>

        {/* Post Section */}
        <div className="posts">
          <h4>Posts</h4>
          {/* Pass selectedStockId to MessageList */}
          <MessageList stockId={selectedStockId} />
        </div>
      </div>
    </div>
  );
};

export default Home;
