import { BrowserRouter as Router } from "react-router-dom";
import { Routes, Route } from "react-router-dom";
import SignIn from "./SignIn";
import SignUp from "./SignUp";
import Home from "./Home";
import './App.css';
import Navbar from "./components/Navbar";
import Graph from "./components/graph/Graph";

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/login" element={<SignIn />} />
        <Route path="/register" element={<SignUp />} />
        <Route path="/" element={<Home />} />
        <Route path = "/graph" element={<Graph />} />
      </Routes>
    </Router>
  );
}

export default App;