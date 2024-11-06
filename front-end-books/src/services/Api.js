import axios from '../utils/Axios-customize';

export const callRegister = (username, email, phone, password, role) => {
    console.log("Đăng kí")
    return axios.post('/api/auth/public/signup', 
        { 
            username, 
            email, 
            phone, 
            password, 
            role 
        }
    );
};


export const callLogin = (username, password) => {
    return axios.post('/api/auth/public/signin', { username, password })
}

export const callFetchAccount = async () => {
    return axios.get('/api/auth/public/fetchAccount');
}

export const callLogout = () => {
    return axios.post('/api/auth/public/logout')
}

export const callFetchListUser = (page, size) => {
    return axios.get('/users/findAllUserWithPage', {
        params: {
            page: page - 1, // Trang API bắt đầu từ 0
            size: size
        }
    });
}

// export const callFetchListUser = (query) => {
//     return axios.get(`/users/findAllUserWithPage?${query}`)
// }


export const callCreateAUser = (username, password, email, phone, idRoles) => {
    return axios.post('/users/save', 
        { 
            username, 
            email, 
            phone, 
            password, 
            idRoles 
        }
    );
}


// export const callBulkCreateUser = (data) => {
//     return axios.post('/api/v1/user/bulk-create', data)
// }
export const callUpdateUser = (id, username, password, email, phone, idRoles) => {
    return axios.put(`/users/update/${id}`, { id, username, password, email, phone, idRoles });
}




export const callDeleteUser = (id) => {
    return axios.delete(`users/deleteUser/${id}`)
}




