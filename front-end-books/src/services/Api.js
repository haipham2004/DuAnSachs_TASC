import {userInstance, bookInstance, orderInstance} from '../utils/Axios-customize';

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



export const callFetchAuthor = (query) => {
    return bookInstance.get(`/authors/findAll?${query}`);
}


export const callFetchPublisher = (query) => {
    return bookInstance.get(`/publisher/findAll?${query}`);
}

export const callFetchCategory = (query) => {
    return bookInstance.get(`/categories/findAll?${query}`);
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



export const callCreateAuthor  = (name, phone, addRess) => {
    return bookInstance.post('/authors/save', {
        name,phone, addRess
    })
}


export const callUpdateAuthor = (id, name, phone, addRess) => {
    return bookInstance.put(`/authors/update/${id}`, {
        name,phone, addRess
    })
}

//

export const callCreatePublisher  = (name, address,phone, email) => {
    return bookInstance.post('/publisher/save', {
        name, address,phone, email
    })
}


export const callUpdatePublisher = (id, name, address,phone, email) => {
    return bookInstance.put(`/publisher/update/${id}`, {
        name, address,phone, email
    })
}

//

export const callCreateCategories  = (name) => {
    return bookInstance.post('/categories/save', {
        name
    })
}


export const callUpdateCategories = (id, name) => {
    return bookInstance.put(`/categories/update/${id}`, {
        name
    })
}
//order 

export const callFetchListOrder = (query) => {
    return orderInstance.get(`orders/findAll?${query}`)
}

export const getOrderWithItems = (id) => {
    return orderInstance.get(`/orders-items/getOrderWithItems/${id}`)
}


// export const callPlaceOrder = (data) => {
//     return axios.post('/api/v1/order', {
//         ...data
//     })
// }

export const callCreateOrder = (total, shippingAddress, userId, ordersItemsRequests) => {
    return orderInstance.post('/orders/createOrder', {
        total,               
        shippingAddress,    
        userId,             
        ordersItemsRequests  
    });
};



