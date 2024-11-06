import axios from "axios";
import { Mutex } from "async-mutex";

const mutex = new Mutex();
const baseUrl = import.meta.env.VITE_BACKEND_USER_URL;

if (!baseUrl) {
    console.error("Base URL is not defined.");
}

const instance = axios.create({
    baseURL: baseUrl,
    withCredentials: true,
});

// Thiết lập header Authorization mặc định
instance.defaults.headers.common['Authorization'] = `Bearer ${localStorage.getItem('access_token') || ''}`;
console.log("Axios, ",localStorage.getItem("access_token"));
const handleRefreshToken = async () => {
    return await mutex.runExclusive(async () => {
        const res = await instance.get('/api/auth/public/refreshToken');
        console.log("refresh: ",res)
        return res && res.data ? res.access_token : null;
    });
};

// Thêm interceptor cho yêu cầu
instance.interceptors.request.use(
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

const NO_RETRY_HEADER = 'x-no-retry';

// Thêm interceptor cho phản hồi
instance.interceptors.response.use(
    function (response) {
        return response && response.data ? response.data : response;
    },
    async function (error) {
        if (error.config && error.response && +error.response.status === 401 && !error.config.headers[NO_RETRY_HEADER]) {
            const access_token = await handleRefreshToken();
            error.config.headers[NO_RETRY_HEADER] = 'true';
            if (access_token) {
                error.config.headers['Authorization'] = `Bearer ${access_token}`;
                localStorage.setItem('access_token', access_token);
                return instance.request(error.config);
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

export default instance;
