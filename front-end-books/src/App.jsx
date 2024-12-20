import React, { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Outlet } from "react-router-dom";
import LoginPage from "./pages/login/Login";
import BookPage from "./pages/book/BookPage";
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
import Loading from "./components/loading/Loading";
import NotFound from "./components/notfound/NotFound";
import ProtectedRoute from "./components/protectedroute/RoleBaseRoute";
import { doGetAccountAction } from "./redux/account/AccountSlice";
import LayoutAdmin from "./components/admin/LayoutAdmin";
import AdminPage from "./components/admin/AdminPage";
import ManageBookPage from "./pages/admin/book";
import './styles/Reset.scss';
import './styles/Global.scss';
import AdminOrderPage from "./pages/admin/order/ManageOrderPage";
import OrderPage from "./pages/order/OrderPage";



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
    if (res && res.results) {

      dispatch(doGetAccountAction({
        username: res.results.username,
        roles: res.results.roles,
        email: res.results.email,
        phone: res.results.phone
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
          path: "book/:slug",
          element: <BookPage />,
        },
        {
          path: "order",
          element:  <OrderPage />,
           
        }

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
            <ProtectedRoute>
              <ManageBookPage />
            </ProtectedRoute>

          ),
        },
        {
          path: "book",
          element: (
            <ProtectedRoute>
              <ManageBookPage />
            </ProtectedRoute>
          ),
        },
        {
          path: "order",
          element: (
            <ProtectedRoute>
              <AdminOrderPage />
            </ProtectedRoute>
          
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
        window.location.pathname === "/" ||
        window.location.pathname.startsWith('/book')
        ? (
        <RouterProvider router={router} />
      ) : (
        <Loading />
      )}
    </>
  );
}
