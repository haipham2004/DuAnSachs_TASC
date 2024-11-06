import React, { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Outlet } from "react-router-dom";
import LoginPage from "./pages/login/Login";
import BookPage from "./pages/book/Book";
import {
  createBrowserRouter,
  RouterProvider,
  Route,
  Link,
} from "react-router-dom";
import Footer from "./components/footer/Footer";
import Header from "./components/header/Header";
import HomePage from "./components/home/Home";
import RegisterPage from "./pages/register/Register";
import { callFetchAccount } from "./services/Api";
import Loading from "./components/Loading/Loading";
import NotFound from "./components/notfound/NotFound";
import AdminPage from "./pages/admin/AdminPage";
import ProtectedRoute from "./components/protectedroute/RoleBaseRoute";
import { doGetAccountAction } from "./redux/account/AccountSlice";
import LayoutAdmin from "./pages/admin/LayoutAdmin";


const Layout = () => {
  return (
    <>
      <div className="layout-app">
        <Header />
        <Outlet />
        <Footer />
      </div>
    </>
  );
};

export default function App() {
  const isAdminRoute = window.location.pathname.startsWith("/admin");
  const user = useSelector((state) => state.account.user);
  const userRole = user.roles || []; 
  const dispatch = useDispatch();
  const isLoading = useSelector((state) => state.account.isLoading);

  const getAccount = async () => {
    if (
      window.location.pathname === "/login" ||
      window.location.pathname === "/register" 
    )
      return;

    const res = await callFetchAccount();
    console.log("Data ",res)
    if (res && res.username ) {
  
      dispatch(doGetAccountAction({
        username: res.username, 
        roles: res.roles,       
        email: res.email,        
        phone: res.phone         
      }));
    }
  };

  useEffect(() => {
    getAccount();
  }, []);

  const router = createBrowserRouter([
    {
      path: "/",
      element: (
        <div>
          <Layout />
        </div>
      ),
      errorElement: <NotFound />,
      children: [
        {
          index: true,
          element: <HomePage />,
        },
        {
          path: "login",
          element: <LoginPage />,
        },
        {
          path: "register",
          element: <RegisterPage />,
        },
        {
          path: "book",
          element: <BookPage />,
        },
      ],
    },

    {
      path: "/admin",
      element: <LayoutAdmin />,
      errorElement: <NotFound />,
      children: [
        {
          index: true,
          element: (
            <ProtectedRoute>
              <AdminPage />
            </ProtectedRoute>
          ),
        },
        {
          path: "user",
          element: (
            // <ProtectedRoute>
            //   {/* <ManageUserPage /> */}
            // </ProtectedRoute>
            <p>user</p>
          ),
        },
        {
          path: "book",
          element: (
            // <ProtectedRout>
            //   {/* <ManageBookPage /> */}
            // </ProtectedRout>
            <p>book</p>
          ),
        },
        {
          path: "order",
          element: (
            // <ProtectedRoute>
            //   <AdminOrderPage />
            // </ProtectedRoute>
            <p>order</p>
          ),
        },
      ],
    },
  ]);

  return (
    <>
      {isLoading === false ||
        window.location.pathname === "/login" ||
        window.location.pathname === "/register" ||
        window.location.pathname === "/" ? (
        <RouterProvider router={router} />
      ) : (
        <Loading />
      )}
    </>
  );
}
