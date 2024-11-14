import React, { useState } from 'react';
import MessageList from './components/messages/MessageList';
import StockList from './components/stock/StockList';
import { Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useUser } from './components/context/UserContext';

const Home = () => {
  const [selectedStockId, setSelectedStockId] = useState(null);

  // Method to handle stock selection
  const onSelectStock = (stockId) => {
    setSelectedStockId(stockId);
  };

  const { userId } = useUser();

  return (
    <div className="d-flex" style={{ backgroundColor: "rgb(248, 249, 250)" }}>
      <div className="d-flex flex-column p-3 text-black bg-light" style={{ width: '200px', height: '100vh', position: 'fixed', top: '60px', left: 0, overflowY: 'auto' }}>
        <StockList onSelectStock={onSelectStock} />
      </div>
      <div className="content flex-grow-1 p-3" style={{ marginLeft: '200px' }}>
        <header className="text-center">
          <div style={{ backgroundColor: '#ffffff', padding: '50px', borderRadius: '10px', boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)', border: '1px solid #dee2e6' }}>
            <h1 style={{ color: '#000000' }}>Welcome to Stock Connect</h1>
            <p className="lead" style={{ color: '#000000' }}>
              Select a stock from the list to start exploring.
            </p>
            {userId == 0 ? (
              <>
                <p className="lead" style={{ color: '#000000' }}>Like what you see?</p>
                <Link to="/register" className="btn btn-primary">
                  Sign Up
                </Link>
              </>
            ) : (
              null
            )}
          </div>
        </header>
        {selectedStockId && <MessageList stockId={selectedStockId} />}
      </div>
    </div>
  );
};

export default Home;
