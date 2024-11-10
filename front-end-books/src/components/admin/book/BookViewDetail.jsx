import { Badge, Descriptions, Divider, Drawer, Modal, Upload } from "antd";
// import moment from 'moment';
// import { FORMAT_DATE_DISPLAY } from "../../../utils/constant";
//FORMAT_DATE_DISPLAY = 'DD-MM-YYYY HH:mm:ss'
import { useEffect, useState } from "react";
import { v4 as uuidv4 } from 'uuid';

const BookViewDetail = (props) => {
    const { openViewDetail, setOpenViewDetail, dataViewDetail, setDataViewDetail } = props;
    const onClose = () => {
        setOpenViewDetail(false);
        setDataViewDetail(null);
    }

    const [previewOpen, setPreviewOpen] = useState(false);
    const [previewImage, setPreviewImage] = useState('');
    const [previewTitle, setPreviewTitle] = useState('');

    const [fileList, setFileList] = useState([]);


    useEffect(() => {
        if (dataViewDetail) {
            let imgThumbnail = {}, imgSlider = [];
            if (dataViewDetail.thumbnail) {
                imgThumbnail = {
                    uid: uuidv4(),
                    name: dataViewDetail.thumbnail,
                    status: 'done',
                    url: `${import.meta.env.VITE_BACKEND_BOOKS_URL}/storage/avartar/${dataViewDetail.thumbnail}`,
                }
            }
            if (dataViewDetail.imageUrl && dataViewDetail.imageUrl.length > 0) {
                dataViewDetail.imageUrl.map(item => {
                    imgSlider.push({
                        uid: uuidv4(),
                        name: item,
                        status: 'done',
                        url: `${import.meta.env.VITE_BACKEND_BOOKS_URL}/storage/avartar/${item}`,
                    })
                })
            }

            setFileList([imgThumbnail, ...imgSlider])
        }
    }, [dataViewDetail])


    const handleCancel = () => setPreviewOpen(false);

    const handlePreview = async (file) => {
        setPreviewImage(file.url);
        setPreviewOpen(true);
        setPreviewTitle(file.name || file.url.substring(file.url.lastIndexOf('/') + 1));
    };

    const handleChange = ({ fileList: newFileList }) => {
        setFileList(newFileList);
    }


    return (
        <>
            { <Drawer
                title="Chức năng xem chi tiết"
                width={"50vw"}
                onClose={onClose}
                open={openViewDetail}
            >
                <Descriptions
                    title="Thông tin Book"
                    bordered
                    column={2}
                >
                    <Descriptions.Item label="Id">{dataViewDetail?.bookId}</Descriptions.Item>
                    <Descriptions.Item label="Tên sách">{dataViewDetail?.title}</Descriptions.Item>
                    <Descriptions.Item label="Giá nhập">{new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(dataViewDetail?.consPrice ?? 0)}</Descriptions.Item>
                    <Descriptions.Item label="Giá bán">{new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(dataViewDetail?.price ?? 0)}</Descriptions.Item>
                    <Descriptions.Item label="Số lượng">{dataViewDetail?.quantity ?? 0}</Descriptions.Item>
                    <Descriptions.Item label="Mô tả sách">{dataViewDetail?.description}</Descriptions.Item>
           
                    <Descriptions.Item label="Tác giả" span={2}>
                        <Badge status="processing" text={dataViewDetail?.nameAuthor} />
                    </Descriptions.Item>

                    <Descriptions.Item label="Thể loại" span={2}>
                        <Badge status="processing" text={dataViewDetail?.nameCategory} />
                    </Descriptions.Item>
                  
                    <Descriptions.Item label="Nhà xuất bản" span={2}>
                        <Badge status="processing" text={dataViewDetail?.namePublisher} />
                    </Descriptions.Item>
                </Descriptions>
                <Divider orientation="left" > Ảnh Books </Divider>
                <Upload
                    action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
                    listType="picture-card"
                    fileList={fileList}
                    onPreview={handlePreview}
                    onChange={handleChange}
                    showUploadList={
                        { showRemoveIcon: false }
                    }
                >
                </Upload>
                <Modal open={previewOpen} title={previewTitle} footer={null} onCancel={handleCancel}>
                    <img alt="example" style={{ width: '100%' }} src={previewImage} />
                </Modal>
            </Drawer> }
        </>
    )
}
export default BookViewDetail;
