import { useSelector } from "react-redux";
import { Navigate } from "react-router-dom";
import NotPermitted from "./NotPermitted";

const RoleBaseRoute = (props) => {
    const isAdminRoute = window.location.pathname.startsWith('/admin');  // Xác định xem đây có phải là route admin không
    const user = useSelector(state => state.account.user);  
    const userRole = user?.roles || [];  // Lấy roles của user, mặc định là mảng rỗng nếu không có roles

    // Kiểm tra xem người dùng có quyền admin và đang ở route admin
    if (isAdminRoute && userRole.includes('ROLE_ADMIN')) {
        return <>{props.children}</>;  // Hiển thị nội dung nếu người dùng có quyền admin và đang ở route admin
    } else {
        return <NotPermitted />;  // Hiển thị trang "Không có quyền truy cập" nếu không có quyền admin hoặc không phải route admin
    }
};


const ProtectedRoute = (props) => {
    const isAuthenticated = useSelector(state => state.account.isAuthenticated)

    return (
        <>
            {isAuthenticated === true ?
                <>
                    <RoleBaseRoute>
                        {props.children}
                    </RoleBaseRoute>
                </>
                :
                <Navigate to='/login' replace />
            }
        </>
    )
}

export default ProtectedRoute;

