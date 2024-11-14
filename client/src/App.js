import { BrowserRouter as Router } from "react-router-dom";
import { Routes, Route } from "react-router-dom";
import SignIn from "./SignIn";
import SignUp from "./SignUp";
import Home from "./Home";
import './App.css';
import Navbar from "./components/Navbar";
import Graph from "./components/graph/Graph";
import { UserContextProvider } from "./components/context/UserContext";

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
        </Routes>
      </Router>
    </UserContextProvider>
  );
}

export default App;