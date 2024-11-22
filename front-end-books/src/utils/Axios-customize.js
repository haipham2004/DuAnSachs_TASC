import axios from 'axios';
import { Mutex } from 'async-mutex';

const mutex = new Mutex();
const baseUrl = import.meta.env.VITE_BACKEND_USER_URL;
const baseBook = import.meta.env.VITE_BACKEND_BOOKS_URL;
const baseOrder = import.meta.env.VITE_BACKEND_ORDERS_URL; // Order API URL

if (!baseUrl || !baseBook || !baseOrder) {
    console.error("Base URL(s) are not defined.");
}

// Axios instance cho User API
const userInstance = axios.create({
    baseURL: baseUrl,
    withCredentials: true,
});

// Axios instance cho Book API
const bookInstance = axios.create({
    baseURL: baseBook,
    withCredentials: true,
});

// Axios instance cho Order API
const orderInstance = axios.create({
    baseURL: baseOrder,
    withCredentials: true,
});

// Thiết lập header Authorization mặc định cho userInstance
userInstance.defaults.headers.common['Authorization'] = `Bearer ${localStorage.getItem('access_token') || ''}`;
console.log("Axios, ", localStorage.getItem("access_token"));

// Hàm để xử lý Refresh Token
const handleRefreshToken = async () => {
    return await mutex.runExclusive(async () => {
        const res = await userInstance.get('/api/auth/public/refreshToken');
        console.log("refresh: ", res);
        return res && res.results ? res.results.jwtToken : null;
    });
};

// Thêm interceptor cho yêu cầu của userInstance
userInstance.interceptors.request.use(
    async (config) => {
        // Nếu là login, signup hoặc các API công khai, loại bỏ Authorization
        if (config.url && (config.url.startsWith('/api/auth/public/signin') || config.url.startsWith('/api/auth/public/signup'))) {
            delete config.headers.Authorization; // Loại bỏ Authorization header
        } else {
            // Nếu có token, thêm vào Authorization header
            const token = window.localStorage.getItem('access_token');
            if (token) {
                config.headers.Authorization = `Bearer ${token}`; // Thêm token vào header nếu có
            }
        }
        return config;
    },
    function (error) {
        return Promise.reject(error);
    }
);

// Thêm interceptor cho phản hồi của userInstance
userInstance.interceptors.response.use(
    function (response) {
        return response && response.data ? response.data : response;
    },
    async function (error) {
        if (error.config && error.response && +error.response.status === 401 && !error.config.headers['x-no-retry']) {
            const access_token = await handleRefreshToken();
            error.config.headers['x-no-retry'] = 'true';
            if (access_token) {
                error.config.headers['Authorization'] = `Bearer ${access_token}`;
                localStorage.setItem('access_token', access_token);
                return userInstance.request(error.config);
            }
        }

        if (error.config && error.response && +error.response.status === 400 && error.config.url === '/api/auth/api/fetchAccount') {
            if (window.location.pathname !== '/' && !window.location.pathname.startsWith('/book')) {
                window.location.href = '/login';
            }
        }

        return error?.response?.data ?? Promise.reject(error);
    }
);

// Axios interceptor cho Book API (Nếu cần thiết)
bookInstance.interceptors.request.use(
    function (config) {
        const token = window.localStorage.getItem('access_token');
        if (token) {
            config.headers.Authorization = 'Bearer ' + token;
        }
        return config;
    },
    function (error) {
        return Promise.reject(error);
    }
);

bookInstance.interceptors.response.use(
    function (response) {
        return response && response.data ? response.data : response;
    },
    function (error) {
        return error?.response?.data ?? Promise.reject(error);
    }
);

// Axios interceptor cho Order API
orderInstance.interceptors.request.use(
    function (config) {
        const token = window.localStorage.getItem('access_token');
        if (token) {
            config.headers.Authorization = 'Bearer ' + token;
        }
        return config;
    },
    function (error) {
        return Promise.reject(error);
    }
);

orderInstance.interceptors.response.use(
    function (response) {
        return response && response.data ? response.data : response;
    },
    function (error) {
        return error?.response?.data ?? Promise.reject(error);
    }
);

// Export ra các instance để sử dụng ở các nơi khác trong ứng dụng
export { userInstance, bookInstance, orderInstance };
