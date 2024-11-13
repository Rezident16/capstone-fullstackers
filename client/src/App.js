//import logo from './logo.svg';
import { BrowserRouter as Router } from "react-router-dom";
import { Routes, Route } from "react-router-dom";
import SignIn from "./SignIn";
import SignUp from "./SignUp";
import Home from "./Home";
import './App.css';

function App() {
  return (
    <Router>
    <Routes>
      <Route path="/login" element={<SignIn/>} />
      <Route path="/register" element={<SignUp/>} />
      <Route path="/" element={<Home/>} />
    </Routes>
   </Router>
  );
}

export default App;
