import { BrowserRouter as Router } from "react-router-dom";
import { Routes, Route } from "react-router-dom";
import SignIn from "./SignIn";
import SignUp from "./SignUp";
import Home from "./Home";
import './App.css';
import Navbar from "./components/Navbar/Navbar";
import Graph from "./components/graph/Graph";
import { UserContextProvider } from "./components/context/UserContext";
import "bootstrap/dist/css/bootstrap.min.css";

import Stock from "./components/stock/Stock";
import AddStockForm from "./components/stock/AddStockForm";
import { useEffect, useState } from "react";

function App() {

  return (
    <UserContextProvider>
      <Router>
        <Navbar />
        <Routes>
          <Route path="/login" element={<SignIn />} />
          <Route path="/register" element={<SignUp />} />
          <Route path="/" element={<Home />} />
          <Route path="/graph" element={<Graph />} />
          <Route path="/stock/:stockId" element={<Stock/>} />
          <Route path="/stock/add" element={<AddStockForm />} />
          <Route path="/stock/:stockId/edit" element={<AddStockForm/>} />
        </Routes>
      </Router>
    </UserContextProvider>
  );
}

export default App;
