import React, { useEffect, useState } from 'react';
import { Col, Divider, Form, Input, InputNumber, message, Modal, notification, Row, Select, Upload } from 'antd';
import { callCreateBook, callFetchAuthor, callFetchCategory, callFetchPublisher, callUploadBookImg } from '../../../services/Api';
import { LoadingOutlined, PlusOutlined } from '@ant-design/icons';

const BookModalCreate = (props) => {
    const { openModalCreate, setOpenModalCreate } = props;
    const [isSubmit, setIsSubmit] = useState(false);

    const [listAuthor, setlistAuthor] = useState([]);
    const [listCategory, setListCategory] = useState([]);
    const [listPublisher, setListPublisher] = useState([]);
    const [form] = Form.useForm();

    const [loading, setLoading] = useState(false);
    const [loadingSlider, setLoadingSlider] = useState(false);

    const [dataThumbnail, setDataThumbnail] = useState([]);
    const [dataSlider, setDataSlider] = useState([]);

    const [previewOpen, setPreviewOpen] = useState(false);
    const [previewImage, setPreviewImage] = useState('');
    const [previewTitle, setPreviewTitle] = useState('');

    useEffect(() => {
        const fetchAuthor = async () => {
            const res = await callFetchAuthor();
            if (res && res.data) {
                const d = res.data.map(item => ({
                    label: item.name,
                    value: item.id
                }));
                setlistAuthor(d);
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

    const onFinish = async (values) => {
        console.log("values",values)
        console.log("dataThumbnail",dataThumbnail)
        console.log("dataSlider",dataSlider)
        
        // if (dataThumbnail.length === 0) {
        //     notification.error({
        //         message: 'Lỗi validate',
        //         description: 'Vui lòng upload ảnh thumbnail'
        //     });
        //     return;
        // }

        // if (dataSlider.length === 0) {
        //     notification.error({
        //         message: 'Lỗi validate',
        //         description: 'Vui lòng upload ảnh slider'
        //     });
        //     return;
        // }

        const { title, authorId, publisherId, categoryId, price, consPrice, description, quantity} = values;
        const thumbnail = dataThumbnail[0].name;
        const imageUrl = dataSlider.map(item => item.name);

        setIsSubmit(true);
        const res = await callCreateBook(title, authorId, publisherId, categoryId, price, consPrice, description, quantity,imageUrl, thumbnail);
        if (res && res.data) {
            message.success('Tạo mới book thành công');
            form.resetFields();
            setDataSlider([]);
            setDataThumbnail([]);
            setOpenModalCreate(false);
            await props.fetchBook();
        } else {
            if (res.messageValidation) {
                const { messageValidation } = res;
                Object.keys(messageValidation).forEach((field) => {
                    notification.error({
                        message: `Lỗi ở trường ${field}`,
                        description: messageValidation[field],
                        duration: 5,
                    });
                });
            }
            if (res.message) {
                notification.error({
                    message: "Có lỗi xảy ra",
                    description: res.message,  // Thông báo lỗi tổng quát từ API
                    duration: 5,
                });
            }
        }
        setIsSubmit(false);
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
        console.log("API slider");
    
        // Gọi API và nhận phản hồi
        const res = await callUploadBookImg(file);
    
        console.log("API slider response", res);  // In ra phản hồi từ API
    
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
    

    const handleRemoveFile = (file, type) => {
        if (type === 'thumbnail') {
            setDataThumbnail([]);
        }
        if (type === 'imageUrl') {
            const newSlider = dataSlider.filter(x => x.uid !== file.uid);
            setDataSlider(newSlider);
        }
    };

    const handlePreview = async (file) => {
        getBase64(file.originFileObj, (url) => {
            setPreviewImage(url);
            setPreviewOpen(true);
            setPreviewTitle(file.name || file.url.substring(file.url.lastIndexOf('/') + 1));
        });
    };

    return (
        <>
            <Modal
                title="Thêm mới book"
                open={openModalCreate}
                onOk={() => { form.submit() }}
                onCancel={() => {
                    form.resetFields();
                    setOpenModalCreate(false);
                }}
                okText={"Tạo mới"}
                cancelText={"Hủy"}
                confirmLoading={isSubmit}
                width={"50vw"}
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
                                name="authorId"
                            >
                                <Select
                                    showSearch
                                    allowClear
                                    options={listAuthor}
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
                                    allowClear
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
                                    allowClear
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

export default BookModalCreate;
