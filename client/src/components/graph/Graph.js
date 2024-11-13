import React, { useState, useEffect } from "react";
import getStockInfo from "./helper";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";

function Graph({ stock }) {
  const [bars, setBars] = useState([]);
  const [time, setTime] = useState("1D");

  if (!stock) {
    return null;
  }

  useEffect(() => {
    async function fetchStockBars(stock, time) {
      const bars = await getStockInfo(stock, time);
      const barsClean = bars.map((bar) => {
        return {
          time: new Date(bar.t).toLocaleString(),
          price: bar.c,
        };
      });
      setBars(barsClean);
    }
    fetchStockBars(stock, time);
  }, [stock, time]);

  console.log(bars[0]);

  return (
    <div>
      <h2>Stock Data for {stock}</h2>
      <ResponsiveContainer width="100%" height={400}>
        <LineChart
          data={bars}
          margin={{
            top: 5,
            right: 30,
            left: 20,
            bottom: 5,
          }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="time" hide={true} />
          <YAxis domain={["dataMin", "dataMax"]} />
          <Tooltip />
          {/* <Legend /> */}
          <Line type="monotone" dataKey="price" stroke="#8884d8" dot={false}
            activeDot={false}/>
        </LineChart>
      </ResponsiveContainer>
      <div className="text-center mt-4">
        <div className="btn-group" role="group" aria-label="Time Range">
          <button className="btn btn-primary" onClick={() => setTime("1D")}>1D</button>
          <button className="btn btn-primary" onClick={() => setTime("1W")}>1W</button>
          <button className="btn btn-primary" onClick={() => setTime("1M")}>1M</button>
          <button className="btn btn-primary" onClick={() => setTime("3M")}>3M</button>
          <button className="btn btn-primary" onClick={() => setTime("YTD")}>YTD</button>
          <button className="btn btn-primary" onClick={() => setTime("1Y")}>1Y</button>
          <button className="btn btn-primary" onClick={() => setTime("5Y")}>5Y</button>
        </div>
      </div>
    </div>
  );
}

export default Graph;
