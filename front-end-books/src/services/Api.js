import {userInstance, bookInstance} from '../utils/Axios-customize';

export const callRegister = (username, email, phone, password, role) => {
    return userInstance.post('/api/auth/public/signup', 
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
    return userInstance.post('/api/auth/public/signin', { username, password })
}

export const callFetchAccount = async () => {
    return userInstance.get('/api/auth/public/fetchAccount');
}

export const callLogout = () => {
    return userInstance.post('/api/auth/public/logout')
}

export const callFetchListUser = (page, size) => {
    return userInstance.get('/users/findAllUserWithPage', {
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
    return userInstance.post('/users/save', 
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
    return userInstance.put(`/users/update/${id}`, { id, username, password, email, phone, idRoles });
}




export const callDeleteUser = (id) => {
    return userInstance.delete(`users/deleteUser/${id}`)
}





// export const callFetchListBook = (page, size) => {
//     console.log("Fetching books with page2:", page, "and size2:", size);
//     return bookInstance.get('/books/findAllBooksPage', {
//         params: {
//             pageNumber: page,  // Gửi giá trị current (pageNumber = page) bắt đầu từ 1
//             pageSize: size     // Số lượng bản ghi mỗi trang
//         }
//     });
// };


//book
// export const callFetchListBook = (page, size) => {
//     console.log("list book api ")
//     return bookInstance.get('/books/findAllBooksPage');
// }


export const callFetchListBook = (query) => {
    return bookInstance.get(`/books/findAllBooksPage2?${query}`)
}


export const callFetchCategory = () => {
    return bookInstance.get('/api/v1/database/category');
}

export const callCreateBook = (thumbnail, slider, mainText, author, price, sold, quantity, category) => {
    return bookInstance.post('/api/v1/book', {
        thumbnail, slider, mainText, author, price, sold, quantity, category
    })
}

export const callUpdateBook = (id, thumbnail, slider, mainText, author, price, sold, quantity, category) => {
    return bookInstance.put(`/api/v1/book/${id}`, {
        thumbnail, slider, mainText, author, price, sold, quantity, category
    })
}

// export const callUploadBookImg = (fileImg) => {
//     const bodyFormData = new FormData();
//     bodyFormData.append('fileImg', fileImg);
//     return bookInstance({
//         method: 'post',
//         url: '/api/v1/file/upload',
//         data: bodyFormData,
//         headers: {
//             "Content-Type": "multipart/form-data",
//             "upload-type": "book"
//         },
//     });
// }

export const callUploadBookImg = (fileImg) => {
    const bodyFormData = new FormData();
    bodyFormData.append('fileImg', fileImg);
    return axios({
        method: 'post',
        url: 'http://localhost:8802/files',
        data: bodyFormData,
        headers: {
            "Content-Type": "multipart/form-data",
            "upload-type": "book"
        },
    });
}


export const callDeleteBook = (id) => {
    return bookInstance.delete(`/api/v1/book/${id}`);
}

export const callFetchBookById = (id) => {
    return bookInstance.get(`api/v1/book/${id}`)
}




