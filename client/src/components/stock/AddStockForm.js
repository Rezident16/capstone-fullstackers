import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../context/UserContext';
import { useEffect } from 'react';
import { useParams } from 'react-router-dom';

const AddStockForm = () => {
  const [stock, setStock] = useState({
    stockId: 0,
    stockName: '',
    description: '',
    ticker: ''
    
  });

  const baseUrl = process.env.NODE_ENV === "production" ? "https://stockconnect.onrender.com" : "http://localhost:8080";
  const {userId} = useUser();
  const navigate = useNavigate();
  const [errors, setErrors] = useState([]);

  useEffect(() => {
    if (userId) {
      fetch(`${baseUrl}/api/user/${userId}`)
        .then((response) => response.json())
        .then((data) => {
          if (data.roleId != 1) {
            // navigate("/");
          }
        })
        .catch(console.error);
      }
    }, [userId]);
    

    const stockId = useParams().stockId;

    useEffect(() => {
        if (stockId) {
            fetch(`${baseUrl}/api/stocks/${stockId}`)
                .then(response => response.json())
                .then(data => {
                    console.log(data)
                    setStock(data)})
                .catch(console.error);
        }
    }, [stockId])

  const handleChange = (e) => {
    const { name, value } = e.target;
    setStock({
      ...stock,
      [name]: value
    });
  };

  const addStock = async () => {
    const init = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(stock),
    }

    const response = await fetch('${baseUrl}/api/stocks', init);
    if (response.ok) {
        setStock({
            stockName: '',
            description: '',
            ticker: ''
        });
        setErrors([]);
        navigate('/');
    } else {
        const data = await response.json();
        setErrors([data.message || 'An error occurred while adding the stock']);
    }
}

const updateStock = async () => {
    const init = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(stock),
    }

    const response = await fetch(`${baseUrl}/api/stocks/${stockId}`, init);
    if (response.ok) {
        setStock({
            stockName: '',
            description: '',
            ticker: ''
        });
        setErrors([]);
        navigate('/');
    } else {
        const data = await response.json();
        setErrors([data.message || 'An error occurred while updating the stock']);
    }
}

const handleSumbit = (e) => {
    e.preventDefault();
    if (stockId) {
        updateStock();
    } else {
        addStock();
    }
}

  return (
    <div className="container mt-5">
      {stockId ? (
        <h3 className="text-center">Update Stock</h3>
      ): (
        <h3 className="text-center">Add New Stock</h3>
      )}
      
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
      <form onSubmit={handleSumbit} className="border p-4 shadow-sm">
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
          <label htmlFor="description" className="form-label">Stock Description</label>
          <textarea
            className="form-control"
            id="description"
            name="description"
            placeholder="Enter stock description"
            value={stock.description}
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
        <button type="submit" className="btn btn-primary w-100">{stockId ? "Update Stock" : "Add Stock"}</button>
      </form>
    </div>
  );
};

export default AddStockForm;
