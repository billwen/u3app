import React from 'react';
import {BrowserRouter as Router, Routes, Route, Link, Navigate} from "react-router-dom";

import './App.css';
import {FaCalendarAlt, FaDoorOpen, FaUsers} from "react-icons/fa";
import UserPicker from "./Components/UserPicker";
import BookingsPage from "./Pages/BookingsPage";
import BookablesPage from "./Pages/BookablesPage";
import UsersPage from "./Pages/UsersPage";


function App() {
  return (
    <Router>
      <div className="App">
        <header>
          <nav>
            <ul>
              <li>
                <Link to="/bookings" className="btn btn-header">
                  <FaCalendarAlt />
                  <span>Bookings</span>
                </Link>
              </li>
              <li>
                <Link to="/bookables" className="btn btn-header">
                  <FaDoorOpen />
                  <span>Bookables</span>
                </Link>
              </li>
              <li>
                <Link to="/users" className="btn btn-header">
                  <FaUsers />
                  <span>Users</span>
                </Link>
              </li>
            </ul>
          </nav>

          <UserPicker />
        </header>

        <Routes>
          <Route path="/bookings" element={<BookingsPage />} />
          <Route path="/bookables" element={<BookablesPage />} />
          <Route path="/users" element={<UsersPage />} />
          <Route path="*" element={<Navigate to="/bookings" />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
