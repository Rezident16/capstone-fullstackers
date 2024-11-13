import { Link } from "react-router-dom";

function Navbar() {
    return (<>
        <nav>
            <Link to={"/"}>Home</Link>
            <Link to={"/login"}>Sign in</Link>
            <Link to={"/register"}>Sign Up</Link>
        </nav>
    </>);
}

export default Navbar;