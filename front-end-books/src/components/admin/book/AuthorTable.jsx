import React, { useState, useEffect } from 'react';
import { Divider, Descriptions, Form, Input, message, Modal, notification, Table, Button } from 'antd';
import { callFetchAuthor, callCreateAuthor, callUpdateAuthor } from '../../../services/Api';
import { PlusOutlined } from '@ant-design/icons';
import BookModalCreate from './BookModalCreate';

const AuthorCreate = (props) => {
    const [isSubmit, setIsSubmit] = useState(false);
    const [form] = Form.useForm();
    const { openModalCreateAuthor, setOpenModalCreateAuthor } = props;
    const [listAuthor2, setListAuthor2] = useState([]); // Truyền fetchAuthor từ component cha
    const [openViewDetail, setOpenViewDetail] = useState(false);
    const [dataViewDetail, setDataViewDetail] = useState(null);
    const [isEdit, setIsEdit] = useState(false);  // Trạng thái để xác định modal có phải là sửa không
    const [searchText, setSearchText] = useState('');  // Lưu trữ giá trị tìm kiếm
    const [current, setCurrent] = useState(1);
    const [pageSize, setPageSize] = useState(5);
    const [total, setTotal] = useState(0);
    const [filter, setFilter] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        fetchAuthor();
    }, [current, pageSize, filter]);

    const fetchAuthor = async () => {
        setIsLoading(true)
        let query = `pageNumber=${current}&pageSize=${pageSize}`;
        if (filter) {
            query += `&${filter}`;
        }


        const res = await callFetchAuthor(query);
        if (res && res.data) {
            setListAuthor2(res.data.content);
            setTotal(res.data.totalElements)
        }
        setIsLoading(false)
    }

   

    // Hàm submit form khi tạo mới hoặc cập nhật tác giả
    const onFinish = async (values) => {
        const { name, phone, addRess } = values;
        setIsSubmit(true); // Đặt trạng thái submit là true để hiển thị loading
    
        try {
            let res;
            if (isEdit) {
                // Nếu đang sửa, gọi API cập nhật
                res = await callUpdateAuthor(dataViewDetail.id, name, phone, addRess);
                if (res && res.data) {
                    message.success('Cập nhật tác giả thành công');
                    setOpenModalCreateAuthor(true);
                    fetchAuthor();  // Cập nhật danh sách sau khi sửa
                    form.resetFields();  // Reset form sau khi cập nhật thành công
                    setIsEdit(false);    // Chuyển về chế độ thêm mới
                }
                else {
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
                setIsSubmit(false);
            } else {
                // Nếu là tạo mới, gọi API tạo mới
                res = await callCreateAuthor(name, phone, addRess);
                if (res && res.data) {
                    message.success('Tạo mới tác giả thành công');
                    form.resetFields();  // Reset form sau khi tạo mới thành công
                    setOpenModalCreateAuthor(true);
                    fetchAuthor();  // Cập nhật danh sách sau khi tạo mới
                }else {
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
                setIsSubmit(false);
            }
        } catch (error) {
            message.error(isEdit ? "Có lỗi khi cập nhật tác giả" : "Có lỗi khi tạo mới tác giả");
        }
    };
    

     // Xử lý sửa thông tin tác giả
    const editAuthor = (author) => {
        setIsEdit(true);  // Đánh dấu là đang chỉnh sửa
        form.setFieldsValue({
            name: author.name,
            phone: author.phone,
            addRess: author.addRess,
        });
        setDataViewDetail(author);
        setOpenModalCreateAuthor(true);
    };


    const onClose = () => {
        setOpenViewDetail(false);
        setDataViewDetail(null);
    }
    // Định nghĩa các cột trong bảng
    const columns = [
        {
            title: 'Id',
            dataIndex: 'bookId',
            render: (text, record, index) => {
                return (
                    <a href='#' onClick={() => {
                        setDataViewDetail(record);
                        console.log("record của tui", record)
                        setOpenViewDetail(true);
                    }}>{record.id}</a>
                )
            }
        },
        {
            title: 'Tên tác giả',
            dataIndex: 'name'
        },
        {
            title: 'Số điện thoại',
            dataIndex: 'phone'
        },
        {
            title: 'Địa chỉ',
            dataIndex: 'addRess',
        },
        {
            title: 'Thao tác',
            key: 'action',
            render: (text, record) => (
                <Button
                    type="link"
                    onClick={() => {
                        editAuthor(record);
                    }}
                >
                    Sửa
                </Button>
            ),
        },
    ];

    const onChange = (pagination, filters, sorter, extra) => {
        if (pagination && pagination.current !== current) {
            setCurrent(pagination.current)
        }
        if (pagination && pagination.pageSize !== pageSize) {
            setPageSize(pagination.pageSize)
            setCurrent(1);
        }
       
    };

    // Hàm Reset - Xóa các trường input và giá trị tìm kiếm
    const handleReset = () => {
        form.resetFields();  // Reset form fields
        setSearchText('');   // Clear search input
        setIsEdit(false);    // Đặt lại chế độ "Thêm mới"
        fetchAuthor();  // Lấy lại danh sách tác giả ban đầu (không có tìm kiếm)
    };
    

    return (
        <>
            {/* <Button
                icon={<PlusOutlined />}
                type="primary"
                onClick={() => {
                    setIsEdit(false);  // Đặt lại chế độ thêm mới
                    form.resetFields();  // Reset form
                    setOpenModalCreateAuthor(true);  // Mở modal tạo mới
                }}
                style={{ marginBottom: 20 }}
            >
                Thêm mới tác giả
            </Button> */}

            <Modal
                title={isEdit ? "Chỉnh sửa tác giả" : "Thêm mới tác giả"}  // Tiêu đề thay đổi khi sửa hoặc tạo mới
                open={openModalCreateAuthor}
                onOk={() => { form.submit(); }}
                onCancel={() => setOpenModalCreateAuthor(false)}
                okText={isEdit ? "Lưu" : "Tạo mới"}
                cancelText={"Hủy"}
                confirmLoading={isSubmit}
                width={800}  // Tăng chiều rộng của modal nếu cần
            >
                <Divider />
                <Form
                    form={form}
                    name="basic"
                    style={{ maxWidth: 600 }}
                    onFinish={onFinish}
                    autoComplete="off"
                >
                    <Form.Item
                        labelCol={{ span: 24 }}
                        label="Tên tác giả"
                        name="name"
                        rules={[{ required: true, message: 'Vui lòng nhập tên tác giả' }]} >
                        <Input />
                    </Form.Item>

                    <Form.Item
                        labelCol={{ span: 24 }}
                        label="Số điện thoại"
                        name="phone"
                        rules={[{ required: true, message: 'Vui lòng nhập Số điện thoại tác giả' }]} >
                        <Input />
                    </Form.Item>

                    <Form.Item
                        labelCol={{ span: 24 }}
                        label="Địa chỉ tác giả"
                        name="addRess"
                        rules={[{ required: true, message: 'Vui lòng nhập Địa chỉ tác giả' }]} >
                        <Input />
                    </Form.Item>
                </Form>

                <Divider />

                <h3>Danh sách tác giả</h3>
                {/* Thêm ô tìm kiếm ở đây */}
                {/* <Input.Search
                    placeholder="Tìm kiếm theo tên hoặc số điện thoại"
                    enterButton="Tìm"
                    onSearch={}
                    value={searchText}
                    onChange={(e) => setSearchText(e.target.value)}  // Đảm bảo giá trị của input tìm kiếm luôn được cập nhật
                    style={{ marginBottom: 20, width: 300 }}
                /> */}
                <Button onClick={handleReset} type="default" style={{ marginLeft: 10 }}>
                    Reset
                </Button>

                <Table
                        // title={renderHeader}
                        loading={isLoading}
                        columns={columns}
                        dataSource={listAuthor2}
                        onChange={onChange}
                        rowKey="id"
                        pagination={
                            {
                                current: current,
                                pageSize: pageSize,
                                showSizeChanger: true,
                                total: total,
                                showTotal: (total, range) => { return (<div> {range[0]}-{range[1]} trên {total} rows</div>) }
                            }
                        }

                    />
            </Modal>

            <Modal
                title="Chi tiết tác giả"
                open={openViewDetail}
                onCancel={onClose}
                footer={null}
                width={600}
            >
                <Descriptions column={2} bordered>
                    <Descriptions.Item label="Tên tác giả">{dataViewDetail?.name}</Descriptions.Item>
                    <Descriptions.Item label="Số điện thoại">{dataViewDetail?.phone}</Descriptions.Item>
                    <Descriptions.Item label="Địa chỉ">{dataViewDetail?.addRess}</Descriptions.Item>
                </Descriptions>
            </Modal>

            <BookModalCreate
                listAuthor2={listAuthor2}       // Truyền listAuthor2 vào
                setListAuthor2={setListAuthor2} // Truyền setListAuthor2 vào
            />
        </>
    );
};

export default AuthorCreate;
