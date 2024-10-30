import React, { useState } from 'react';
import { Outlet } from 'react-router-dom';  
import LoginPage from './pages/login/Login';
import BookPage from './pages/book/Book';
import {
  createBrowserRouter,
  RouterProvider,
  Route,
  Link,
} from "react-router-dom";
import Footer from './components/footer/Footer';
import Header from './components/header/Header';
import HomePage from './components/home/Home';




const Layout = () =>{
  return(
    <>
    <div className='layout-app'>
      <Header/>
      <Outlet/>
      <Footer/>
    </div>
    </>
  )
}

export default function App() {

  const router = createBrowserRouter([
    {
      path: "/",
      element: <div><Layout/></div>,
      errorElement: <div>404 NOT FOUND</div>,
      children:[
        {
          index:true,
          element: <HomePage/>
        },
        {
          path: "login",
          element: <LoginPage/>
        },
        {
          path: "book",
          element: <BookPage/>
        }
      ]
    }
    
  ]);
 
  return(
    <>
    <RouterProvider router={router} />
    </>
  )
}
