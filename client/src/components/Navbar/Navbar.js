import { Link, useNavigate } from "react-router-dom";
import { useUser } from "../context/UserContext";
import Dropdown from 'react-bootstrap/Dropdown';
import 'bootstrap/dist/css/bootstrap.min.css';

function Navbar() {
  const { userId, logout } = useUser(); 
  const navigate = useNavigate();


  const handleSignOut = () => {
    logout(); 
    navigate("/login"); 
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light" style={{borderBottom: 
      "1px solid grey"
    }}>
      <div className="container-fluid d-flex justify-content-between">
        <Link to="/" className="navbar-brand">Home</Link>

        {/* Navbar Dropdown for Sign In/Sign Out */}
        <Dropdown align="end">
          <Dropdown.Toggle
            variant="primary"
            id="dropdown-basic"
            className="dropdown-toggle-custom"
          >
            {userId ? "Profile" : "Sign In / Register"}
          </Dropdown.Toggle>

          <Dropdown.Menu className="dropdown-menu-custom">
            {userId ? (
              <Dropdown.Item
                onClick={handleSignOut}
                className="text-center"
              >
                Sign Out
              </Dropdown.Item>
            ) : (
              <>
                <Dropdown.Item as={Link} to="/login" className="text-center">
                  Sign In
                </Dropdown.Item>
                <Dropdown.Item as={Link} to="/register" className="text-center">
                  Sign Up
                </Dropdown.Item>
              </>
            )}
          </Dropdown.Menu>
        </Dropdown>
      </div>
    </nav>
  );
}

export default Navbar;
