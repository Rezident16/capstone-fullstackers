import { createContext, useState, useContext, useEffect } from "react";

const UserContext = createContext();

export const useUser = () => {
  return useContext(UserContext);
};

export const UserContextProvider = ({ children }) => {
  const [userId, setUserId] = useState(null); 
  const [jwtToken, setJwtToken] = useState(null); 

  useEffect(() => {
    const savedUserId = localStorage.getItem("user_id");
    const savedToken = localStorage.getItem("jwt_token");
    if (savedUserId && savedToken) {
      setUserId(savedUserId);
      setJwtToken(savedToken);
    }
  }, []);

  const login = (userId, token) => {
    setUserId(userId);
    setJwtToken(token);
    localStorage.setItem("user_id", userId);
    localStorage.setItem("jwt_token", token);
  };

  const logout = () => {
    setUserId(null);
    setJwtToken(null);
    localStorage.removeItem("user_id");
    localStorage.removeItem("jwt_token");
  };

  return (
    <UserContext.Provider value={{ userId, jwtToken, login, logout }}>
      {children}
    </UserContext.Provider>
  );
};
