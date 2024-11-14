import {userInstance, bookInstance} from '../utils/Axios-customize';

export const callRegister = (username, email, phone, password, fullName, addRess,role) => {
    return userInstance.post('/api/auth/public/signup', 
        { 
            username, 
            email, 
            phone, 
            password,
            fullName,
            addRess, 
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

// export const callFetchListUser = (query) => {
//     return userInstance.get('/users/findAll?${query}')
// }
// export const callFetchListBook2 = (query) => {
//     return bookInstance.get(`/books/findAllBooksPage3?${query}`)
// }

export const callFetchListUser= (query) =>{
return userInstance.get(`/users/findAll?${query}`)
}
// export const callFetchListUser = (query) => {
//     return axios.get(`/users/findAllUserWithPage?${query}`)
// }


export const callCreateUser = (username, password, email, phone, fullName, addRess, idRoles) => {
    return userInstance.post('/users/save', 
        { 
            username, 
            email, 
            phone, 
            password, 
            fullName,
            addRess,
            idRoles 
        }
    );
}


export const callUpdateUser = (id, username, password, email, phone, fullName, addRess, idRoles) => {
    return userInstance.put(`/users/update/${id}`, { id, username, password, email, phone,fullName, addRess, idRoles });
}




export const callDeleteUser = (id) => {
    return userInstance.delete(`users/deleteUser/${id}`)
}



export const callFetchListBook = (query) => {
    return bookInstance.get(`/books/findAllBooksPage3?${query}`)
}


export const callFetchCategory = () => {
    return bookInstance.get('/categories/findAllCategoriesDto');
}

export const callFetchAuthor = () => {
    return bookInstance.get('/authors/findAllAuthorsDto');
}


export const callFetchPublisher = () => {
    return bookInstance.get('/publisher/findAllPublisherDto');
}


export const callCreateBook = (title, authorId, publisherId, categoryId, price, consPrice, description, quantity,imageUrl, thumbnail) => {
    return bookInstance.post('/books/save', {
        title, authorId, publisherId, categoryId, price, consPrice, description, quantity,imageUrl, thumbnail
    })
}

export const callUpdateBook = (bookId, title, authorId, publisherId, categoryId, price, consPrice, description, quantity,imageUrl, thumbnail) => {
    return bookInstance.put(`books/update/${bookId}`, {
        title, authorId, publisherId, categoryId, price, consPrice, description, quantity,imageUrl, thumbnail
    })
}


export const callUploadBookImg = (file) => {
    const bodyFormData = new FormData();
    bodyFormData.append('file', file);
    console.log("file là: ",file)
    return bookInstance({
        method: 'post',
        url: 'http://localhost:8802/files',
        data: bodyFormData,
        headers: {
            "Content-Type": "multipart/form-data",
            "upload-type": "book"
        },
    });
}


export const callDeleteBook = (id, deleteFlag) => {
    return bookInstance.delete(`/books/delete/${id}`, {
        params: {
            delete: deleteFlag // Truyền tham số delete dưới dạng query parameter
        }
    });
}


export const callFetchBookById = (id) => {
    return bookInstance.get(`/books/findById/${id}`)
}



//order 

export const callFetchListOrder = (query) => {
    return axios.get(`/api/v1/order?${query}`)
}

export const callPlaceOrder = (data) => {
    return axios.post('/api/v1/order', {
        ...data
    })
}



