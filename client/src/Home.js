import React, { useState } from 'react';
import { Link } from 'react-router-dom';

const Home = () => {
  const [stockPrice, setStockPrice] = useState(150);
  const [posts, setPosts] = useState([
    "Stock 1 is doing great today!",
    "Considering buying more Stock 1 soon.",
    "Stock 1 hit a new high this week!"
  ]);

  return (
    <div className="d-flex">
      {/* Sidebar */}
      <div className="sidebar p-3 bg-light" style={{ width: '200px' }}>
        <h4>Stocks</h4>
        <ul className="list-unstyled">
          <li className="mb-3"><Link to="#">Stock 1</Link></li>
          <li className="mb-3"><Link to="#">Stock 2</Link></li>
          <li className="mb-3"><Link to="#">Stock 3</Link></li>
          <li className="mb-3"><Link to="#">Stock 4</Link></li>
          <li className="mb-3"><Link to="#">Stock 5</Link></li>
        </ul>
      </div>

      {/* Main Content */}
      <div className="main-content p-4" style={{ flex: 1 }}>
        {/* Stock Price*/}
        <div className="stock-price mb-4">
          <h2>Stock 1 Price: ${stockPrice}</h2>
        </div>

        {/* Stock Chart Section*/}
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
          {posts.length > 0 ? (
            <ul className="list-group">
              {posts.map((post, index) => (
                <li key={index} className="list-group-item border-bottom py-3">
                  {/* Posts*/}
                  <div className="d-flex">
                    <img
                      src="https://media.tenor.com/Ucg45NFV8XkAAAAM/ducks-funny-ducks.gif"
                      alt="Avatar"
                      className="rounded-circle mr-3"
                      style={{ width: '50px', height: '50px' }}
                    />
                    <div>
                      <p className="mb-1"><strong>User Name</strong></p>
                      <p>{post}</p>
                    </div>
                  </div>
                </li>
              ))}
            </ul>
          ) : (
            <p>No posts available.</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default Home;
