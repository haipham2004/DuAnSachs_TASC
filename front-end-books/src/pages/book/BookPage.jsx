import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { callFetchBookById } from "../../services/Api";
import ViewDetail from "../../components/book/ViewDetail";

const BookPage = () => {
    const [dataBook, setDataBook] = useState()
    let location = useLocation();

    let params = new URLSearchParams(location.search);
    const id = params?.get("id"); // book id
   

    console.log("id ",id)
    useEffect(() => {
        fetchBook(id);
    }, [id]);

    const fetchBook = async (id) => {
        const res = await callFetchBookById(id);
        console.log("id book raw ",res)
        if (res && res.data) {
            let raw = res.data;
            //process data
            raw.items = getImages(raw);
            setDataBook(raw);
        }
    }

    const getImages = (raw) => {
        const images = [];
        if (raw.thumbnail) {
            images.push(
                {
                    original: `${import.meta.env.VITE_BACKEND_BOOKS_URL}/storage/avartar/${raw.thumbnail}`,
                    thumbnail: `${import.meta.env.VITE_BACKEND_BOOKS_URL}/storage/avartar/${raw.thumbnail}`,
                    originalClass: "original-image",
                    thumbnailClass: "thumbnail-image"
                },
            )
        }
        if (raw.imageUrl) {
            raw.imageUrl?.map(item => {
                images.push(
                    {
                        original: `${import.meta.env.VITE_BACKEND_URL}/images/book/${item}`,
                        thumbnail: `${import.meta.env.VITE_BACKEND_URL}/images/book/${item}`,
                        originalClass: "original-image",
                        thumbnailClass: "thumbnail-image"
                    },
                )
            })
        }
        return images;
    }
    return (
        <>
            <ViewDetail dataBook={dataBook} />
        </>
    )
}

export default BookPage;
