import React, { useEffect, useState } from 'react';
import { Col, Divider, Form, Input, InputNumber, message, Modal, notification, Row, Select, Upload } from 'antd';
import { callFetchAuthor, callFetchPublisher, callFetchCategory, callUpdateBook, callUploadBookImg } from '../../../services/Api';
import { LoadingOutlined, PlusOutlined } from '@ant-design/icons'
import { v4 as uuidv4 } from 'uuid';

const BookModalUpdate = (props) => {
    const { openModalUpdate, setOpenModalUpdate, dataUpdate, setDataUpdate } = props;
    const [isSubmit, setIsSubmit] = useState(false);


    const [listAuthor, setListAuthor] = useState([]);
    const [listCategory, setListCategory] = useState([]);
    const [listPublisher, setListPublisher] = useState([]);

    const [form] = Form.useForm();


    const [loading, setLoading] = useState(false);
    const [loadingSlider, setLoadingSlider] = useState(false);

    const [imageUrl, setImageUrl] = useState("");

    const [dataThumbnail, setDataThumbnail] = useState([])
    const [dataSlider, setDataSlider] = useState([])

    const [previewOpen, setPreviewOpen] = useState(false);
    const [previewImage, setPreviewImage] = useState('');
    const [previewTitle, setPreviewTitle] = useState('');

    const [initForm, setInitForm] = useState(null);

    useEffect(() => {
        const fetchAuthor = async () => {
            const res = await callFetchAuthor();
            if (res && res.data) {
                const d = res.data.map(item => ({
                    label: item.name,
                    value: item.id
                }));
                setListAuthor(d);
                console.log("ListAuthor: ", d)
            }
        };
        fetchAuthor();
    }, []);

    useEffect(() => {
        const fetchPublisher = async () => {
            const res = await callFetchPublisher();
            if (res && res.data) {
                const d = res.data.map(item => ({
                    label: item.name,
                    value: item.id
                }));
                setListPublisher(d);
            }
        };
        fetchPublisher();
    }, []);

    useEffect(() => {
        const fetchCategory = async () => {
            const res = await callFetchCategory();
            if (res && res.data) {
                const d = res.data.map(item => ({
                    label: item.name,
                    value: item.id
                }));
                setListCategory(d);
            }
        };
        fetchCategory();
    }, []);

    useEffect(() => {
        if (dataUpdate?.bookId) {
            const arrThumbnail = [
                {
                    uid: uuidv4(),
                    name: dataUpdate.thumbnail,
                    status: 'done',
                    url: `${import.meta.env.VITE_BACKEND_BOOKS_URL}/storage/avartar/${dataUpdate.thumbnail}`,
                }
            ]

            const arrSlider = dataUpdate?.imageUrl?.map(item => {
                return {
                    uid: uuidv4(),
                    name: item,
                    status: 'done',
                    url: `${import.meta.env.VITE_BACKEND_BOOKS_URL}/storage/avartar/${item}`,
                }
            })

            const init = {
                bookId: dataUpdate.bookId,
                title: dataUpdate.title,
                authorId :dataUpdate.authorId,
                namePublisher: dataUpdate.namePublisher,
                nameCategory: dataUpdate.nameCategory,
                consPrice: dataUpdate.consPrice,
                price: dataUpdate.price,
                description: dataUpdate.description,
                quantity: dataUpdate.quantity,
                thumbnail: { fileList: arrThumbnail },
                imageUrl: { fileList: arrSlider }
            }
            setInitForm(init);
            setDataThumbnail(arrThumbnail);
            setDataSlider(arrSlider);
            form.setFieldsValue(init);
        }
        return () => {
            form.resetFields();
        }
    }, [dataUpdate])


    const onFinish = async (values) => {
        if (dataThumbnail.length === 0) {
            notification.error({
                message: 'Lỗi validate',
                description: 'Vui lòng upload ảnh thumbnail'
            })
            return;
        }

        if (dataSlider.length === 0) {
            notification.error({
                message: 'Lỗi validate',
                description: 'Vui lòng upload ảnh slider'
            })
            return;
        }


        const { bookId, title, authorId, publisherId, categoryId, price, consPrice, description, quantity } = values;
        const thumbnail = dataThumbnail[0].name;
        const imageUrl = dataSlider.map(item => item.name);

        setIsSubmit(true)
        const res = await callUpdateBook(bookId, title, authorId, publisherId, categoryId, price, consPrice, description, quantity, imageUrl, thumbnail);
        if (res && res.data) {
            message.success('Cập nhật book thành công');
            form.resetFields();
            setDataSlider([]);
            setDataThumbnail([]);
            setInitForm(null);
            setOpenModalUpdate(false);
            await props.fetchBook()
        } else {
            if (res.messageValidation) {
                const { messageValidation } = res;
                Object.keys(messageValidation).forEach((field) => {
                    notification.error({
                        message: res.status + ' Please check again' + field,
                        description: messageValidation[field],
                        duration: 5,
                    });
                });
            }
            if (res.message) {
                notification.error({
                    message: "An error occurred",
                    description:  res.message,  // Thông báo lỗi tổng quát từ API
                    duration: 5,
                });
            }
        }
        setIsSubmit(false)
    };


    const getBase64 = (img, callback) => {
        const reader = new FileReader();
        reader.addEventListener('load', () => callback(reader.result));
        reader.readAsDataURL(img);
    };

    // const beforeUpload = (file) => {
    //     const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
    //     if (!isJpgOrPng) {
    //         message.error('You can only upload JPG/PNG file!');
    //     }
    //     const isLt2M = file.size / 1024 / 1024 < 2;
    //     if (!isLt2M) {
    //         message.error('Image must smaller than 2MB!');
    //     }
    //     return isJpgOrPng && isLt2M;
    // };

    // const handleChange = (info, type) => {
    //     if (info.file.status === 'uploading') {
    //         type ? setLoadingSlider(true) : setLoading(true);
    //         return;
    //     }
    //     if (info.file.status === 'done') {
    //         // Get this url from response in real world.
    //         getBase64(info.file.originFileObj, (url) => {
    //             type ? setLoadingSlider(false) : setLoading(false);
    //             setImageUrl(url);
    //         });
    //     }
    // };



    const handleUploadFileThumbnail = async ({ file, onSuccess, onError }) => {
        console.log("File Uploading: ", file);

        try {
            const res = await callUploadBookImg(file);  // Gọi API upload
            console.log("API Response: ", res);  // Kiểm tra phản hồi API

            if (res && res.data && res.data[0]) {  // Kiểm tra nếu có data và phần tử đầu tiên trong data
                const fileName = res.data[0].fileName;  // Lấy fileName từ phần tử đầu tiên của mảng data
                console.log("API fileName: ", fileName);
                setDataThumbnail([{
                    name: fileName,  // Lưu tên file vào state
                    uid: file.uid  // Lưu uid của file
                }]);

                onSuccess('ok');  // Gọi onSuccess sau khi upload thành công
            } else {
                onError('Đã có lỗi khi upload file');  // Nếu không có phản hồi hợp lệ
            }
        } catch (error) {
            console.error("Upload error: ", error);  // Ghi lại lỗi nếu có
            onError('Đã có lỗi khi upload file');  // Gọi onError nếu có lỗi
        }
    };




    const handleUploadFileSlider = async ({ file, onSuccess, onError }) => {
        // Gọi API và nhận phản hồi
        const res = await callUploadBookImg(file);
        // Kiểm tra xem có phản hồi và fileName không
        if (res && res.data && res.data.length > 0) {
            // Lấy fileName từ phản hồi và cập nhật state
            res.data.forEach((fileData) => {
                setDataSlider((dataSlider) => [
                    ...dataSlider,
                    {
                        name: fileData.fileName,
                        uid: file.uid, // Giữ lại uid của file
                    },
                ]);
            });
            onSuccess('ok');
        } else {
            onError('Đã có lỗi khi upload file');
        }
    };

    const handlePreview = async (file) => {
        if (file.url && !file.originFileObj) {
            setPreviewImage(file.url);
            setPreviewOpen(true);
            setPreviewTitle(file.name || file.url.substring(file.url.lastIndexOf('/') + 1));
            return;
        }
        getBase64(file.originFileObj, (url) => {
            setPreviewImage(url);
            setPreviewOpen(true);
            setPreviewTitle(file.name || file.url.substring(file.url.lastIndexOf('/') + 1));
        });
    };

    const handleRemoveFile = (file, type) => {
        if (type === 'thumbnail') {
            setDataThumbnail([]);
        }
        if (type === 'imageUrl') {
            const newSlider = dataSlider.filter(x => x.uid !== file.uid);
            setDataSlider(newSlider);
        }
    };



    return (
        <>
            <Modal
                title="Cập nhật book"
                open={openModalUpdate}
                onOk={() => { form.submit() }}
                onCancel={() => {
                    form.resetFields();
                    setInitForm(null)
                    setDataUpdate(null);
                    setOpenModalUpdate(false)
                }}
                okText={"Cập nhật"}
                cancelText={"Hủy"}
                confirmLoading={isSubmit}
                width={"50vw"}
                //do not close when click outside
                maskClosable={false}
            >

                <Divider />

                <Form
                    form={form}
                    name="basic"
                    onFinish={onFinish}
                    autoComplete="off"
                    initialValues={{
                        authorId: listAuthor.length > 0 ? listAuthor[0].value : undefined,
                        categoryId: listCategory.length > 0 ? listCategory[0].value : undefined,
                        publisherId: listPublisher.length > 0 ? listPublisher[0].value : undefined,
                        sold: 0,
                        quantity: 1,
                    }}
                >
                    <Row gutter={15}>

                        <Col span={12}>

                            <Col hidden>
                                <Form.Item
                                    hidden
                                    labelCol={{ span: 24 }}
                                    label="ID sách"
                                    name="bookId"
                                >
                                    <Input />
                                </Form.Item>
                            </Col>

                            <Form.Item
                                label="Tên sách"
                                name="title"
                                rules={[{ required: true, message: 'Vui lòng nhập tên hiển thị!' }]}
                            >
                                <Input />
                            </Form.Item>
                        </Col>
                        <Col span={12}>
                            <Form.Item
                                label="Tác giả"
                                name="authorId"  // Đảm bảo rằng tên trường là "authorId"
                            >
                                <Select
                                    showSearch
                                    placeholder="Chọn tác giả"
                                    filterOption={(input, option) =>
                                        (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
                                    
                                    }
                                    options={listAuthor}  // listAuthor là mảng chứa các tác giả
                                />
                            </Form.Item>
                        </Col>


                        <Col span={12}>
                            <Form.Item
                                label="Giá nhập"
                                name="consPrice"
                            >
                                <InputNumber
                                    min={0}
                                    style={{ width: '100%' }}
                                    formatter={(value) => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
                                    addonAfter="VND"
                                />
                            </Form.Item>
                        </Col>
                        <Col span={12}>
                            <Form.Item
                                label="Giá bán"
                                name="price"
                            >
                                <InputNumber
                                    min={0}
                                    style={{ width: '100%' }}
                                    formatter={(value) => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
                                    addonAfter="VND"
                                />
                            </Form.Item>
                        </Col>

                        <Col span={12}>
                            <Form.Item
                                label="Nhà xuất bản"
                                name="publisherId"
                            >
                                <Select
                                    showSearch
                                    placeholder="Chọn nhà xuất bản"
                                    filterOption={(input, option) =>
                                        (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
                                    }
                                    options={listPublisher}
                                />

                            </Form.Item>
                        </Col>

                        <Col span={12}>
                            <Form.Item
                                label="Thể loại"
                                name="categoryId"
                            >
                                <Select
                                    showSearch
                                    placeholder="Chọn thể loại"
                                    filterOption={(input, option) =>
                                        (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
                                    }
                                    options={listCategory}
                                />

                            </Form.Item>
                        </Col>

                        <Col span={12}>
                            <Form.Item
                                label="Số lượng"
                                name="quantity"
                                rules={[{ required: true, message: 'Vui lòng nhập số lượng!' }]}
                            >
                                <InputNumber min={1} style={{ width: '100%' }} />
                            </Form.Item>
                        </Col>

                        <Col span={12}>
                            <Form.Item
                                name="description"
                                label="Mô tả"
                            >
                                <Input.TextArea />
                            </Form.Item>
                        </Col>

                        <Col span={6}>
                            <Form.Item
                                labelCol={{ span: 24 }}
                                label="Ảnh Thumbnail"
                                name="thumbnail"
                            >
                                <Upload
                                    name="thumbnail"
                                    listType="picture-card"
                                    className="avatar-uploader"
                                    maxCount={1}
                                    multiple={false}
                                    customRequest={handleUploadFileThumbnail}
                                    // beforeUpload={() => false}
                                    // onChange={handleChange}
                                    onRemove={(file) => handleRemoveFile(file, "thumbnail")}
                                    onPreview={handlePreview}
                                    defaultFileList={initForm?.thumbnail?.fileList ?? []}
                                >
                                    <div>
                                        {loading ? <LoadingOutlined /> : <PlusOutlined />}
                                        <div style={{ marginTop: 8 }}>Upload</div>
                                    </div>
                                </Upload>
                            </Form.Item>

                        </Col>


                        <Col span={6}>
                            <Form.Item
                                labelCol={{ span: 24 }}
                                label="Ảnh Slider"
                                name="imageUrl">
                                <Upload
                                    multiple
                                    name="imageUrl"
                                    listType="picture-card"
                                    className="avatar-uploader"
                                    customRequest={handleUploadFileSlider}
                                    // beforeUpload={() => false}
                                    // onChange={(info) => handleChange(info, 'slider')}
                                    onRemove={(file) => handleRemoveFile(file, "slider")}
                                    onPreview={handlePreview}
                                    defaultFileList={initForm?.imageUrl?.fileList ?? []}
                                >
                                    <div>
                                        {loadingSlider ? <LoadingOutlined /> : <PlusOutlined />}
                                        <div style={{ marginTop: 8 }}>Upload</div>
                                    </div>
                                </Upload>
                            </Form.Item>
                        </Col>

                    </Row>
                </Form>

            </Modal>
            <Modal open={previewOpen} title={previewTitle} footer={null} onCancel={() => setPreviewOpen(false)}>
                <img alt="example" style={{ width: '100%' }} src={previewImage} />
            </Modal>
        </>
    );
};

export default BookModalUpdate;
