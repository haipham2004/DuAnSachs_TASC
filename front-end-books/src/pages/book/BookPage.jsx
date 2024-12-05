import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { callFetchBookById } from "../../services/Api";
import ViewDetail from "../../components/book/ViewDetail";

const BookPage = () => {
    const [dataBook, setDataBook] = useState()
    let location = useLocation();

    let params = new URLSearchParams(location.search);
    const id = params?.get("id"); // book id
   
    useEffect(() => {
        fetchBook(id);
    }, [id]);

    const fetchBook = async (id) => {
        const res = await callFetchBookById(id);
        if (res && res.data) {
            let raw = res.data;
            //process data
            raw.items = getImages(raw);
            setDataBook(raw);
        }
    }
    const getImages = (raw) => {
        const images = [];
        
        // Kiểm tra và thêm thumbnail vào images nếu có
        if (raw.thumbnail) {
            images.push({
                original: `${import.meta.env.VITE_BACKEND_BOOKS_URL}/storage/avatar/${raw.thumbnail}`,
                thumbnail: `${import.meta.env.VITE_BACKEND_BOOKS_URL}/storage/avatar/${raw.thumbnail}`,
                originalClass: "original-image",
                thumbnailClass: "thumbnail-image"
            });
        }
        
        // Kiểm tra và thêm các imageUrl vào images nếu có
        if (Array.isArray(raw.imageUrl)) {
            raw.imageUrl.forEach(item => {
                images.push({
                    original: `${import.meta.env.VITE_BACKEND_BOOKS_URL}/storage/avatar/${item}`,
                    thumbnail: `${import.meta.env.VITE_BACKEND_BOOKS_URL}/storage/avatar/${item}`,
                    originalClass: "original-image",
                    thumbnailClass: "thumbnail-image"
                });
            });
        } else if (raw.imageUrl) {
            // Trường hợp raw.imageUrl là một chuỗi đơn lẻ (không phải mảng)
            images.push({
                original: `${import.meta.env.VITE_BACKEND_BOOKS_URL}/storage/avatar/${raw.imageUrl}`,
                thumbnail: `${import.meta.env.VITE_BACKEND_BOOKS_URL}/storage/avatar/${raw.imageUrl}`,
                originalClass: "original-image",
                thumbnailClass: "thumbnail-image"
            });
        }
        
        return images;
    };
    
    
    return (
        <>
            <ViewDetail dataBook={dataBook} />
        </>
    )
}

export default BookPage;
