import React from 'react';
import './App.scss';
import { css } from '@emotion/css';
import { BrowserRouter as Router, Route, Routes, Navigate} from 'react-router-dom';

import Nav from "Common/Nav";
import ProductsPage from "Products/ProductsPage";
import AdminPage from "Admin/AdminPage";

const AppStyles = css`
  margin: 50px auto;
  width: 380px;
  
  .Container {
    background: #1d1e26;
    border: 4px solid #9580ff;
    border-radius: 6px;
    padding: 25px;
  }
`;
const App = () => {
  return (
      <div className={AppStyles}>
        <Router>
            <div className="Container">
                <Nav />
                <Routes>
                    <Route path="/products*" element={ <ProductsPage /> } />
                    <Route path="/admin" element={ <AdminPage /> } />
                    <Route path="*" element={<Navigate to="/products" />} />
                </Routes>
            </div>
        </Router>
      </div>
  );
}

export default App;
