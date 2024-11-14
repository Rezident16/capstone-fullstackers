import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useNavigate } from 'react-router-dom';

const AddStockForm = () => {
  const [stock, setStock] = useState({
    stockName: '',
    stockDescription: '',
    ticker: ''
  });
  const [errors, setErrors] = useState([]);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setStock({
      ...stock,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const response = await fetch('http://localhost:8080/api/stocks', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(stock)
    });

    if (response.ok) {
      setStock({
        stockName: '',
        stockDescription: '',
        ticker: ''
      });
      setErrors([]);
      navigate('/');
    } else {
      const data = await response.json();
      setErrors([data.message || 'An error occurred while adding the stock']);
    }
  };

  return (
    <div className="container mt-5">
      <h3 className="text-center">Add New Stock</h3>
      {errors.length > 0 && (
        <div className="alert alert-danger">
          <p>The following errors were found:</p>
          <ul>
            {errors.map((error, index) => (
              <li key={index}>{error}</li>
            ))}
          </ul>
        </div>
      )}
      <form onSubmit={handleSubmit} className="border p-4 shadow-sm">
        <div className="mb-3">
          <label htmlFor="stockName" className="form-label">Stock Name</label>
          <input
            type="text"
            className="form-control"
            id="stockName"
            name="stockName"
            placeholder="Enter stock name"
            value={stock.stockName}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="stockDescription" className="form-label">Stock Description</label>
          <textarea
            className="form-control"
            id="stockDescription"
            name="stockDescription"
            placeholder="Enter stock description"
            value={stock.stockDescription}
            onChange={handleChange}
            rows="3"
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="ticker" className="form-label">Ticker Symbol</label>
          <input
            type="text"
            className="form-control"
            id="ticker"
            name="ticker"
            placeholder="Enter ticker symbol"
            value={stock.ticker}
            onChange={handleChange}
            required
          />
        </div>
        <button type="submit" className="btn btn-primary w-100">Add Stock</button>
      </form>
    </div>
  );
};

export default AddStockForm;