import React, { useState, useEffect } from 'react';
import { Divider, Descriptions, Form, Input, message, Modal, notification, Table, Button } from 'antd';
import { callFetchCategory, callCreateCategories, callUpdateCategories } from '../../../services/Api';
import { PlusOutlined } from '@ant-design/icons';
import BookModalCreate from './BookModalCreate';

const CategoryTable = (props) => {
    const [isSubmit, setIsSubmit] = useState(false);
    const [form] = Form.useForm();
    const { openModalCategory, setOpenModalCategory } = props;
    const [listCategory2, setListCategory2] = useState([]);
    const [openViewDetail, setOpenViewDetail] = useState(false);
    const [dataViewDetail, setDataViewDetail] = useState(null);
    const [isEdit, setIsEdit] = useState(false);  
    const [searchText, setSearchText] = useState('');
    const [current, setCurrent] = useState(1);
    const [pageSize, setPageSize] = useState(5);
    const [total, setTotal] = useState(0);
    const [filter, setFilter] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        fetchCategory();
    }, [current, pageSize, filter]);

    const fetchCategory = async () => {
        setIsLoading(true)
        let query = `pageNumber=${current}&pageSize=${pageSize}`;
        if (filter) {
            query += `&${filter}`;
        }


        const res = await callFetchCategory(query);
        if (res && res.data) {
            setListCategory2(res.data.content);
            setTotal(res.data.totalElements)
        }
        setIsLoading(false)
    }



   
    const onFinish = async (values) => {
        const { name} = values;
        setIsSubmit(true); 

        try {
            let res;
            if (isEdit) {
          
                res = await callUpdateCategories(dataViewDetail.id, name);
                if (res && res.data) {
                    message.success('Cập nhật nhà xuất bản thành công');
                    setOpenModalCategory(true);
                    fetchCategory();  
                    form.resetFields(); 
                    setIsEdit(false);    
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
                            description: res.message,  
                            duration: 5,
                        });
                    }
                }
                setIsSubmit(false);
            } else {
                
                res = await callCreateCategories(name);
                if (res && res.data) {
                    message.success('Tạo mới nhà xuất bản thành công');
                    form.resetFields();  
                    setOpenModalCategory(true);
                    fetchCategory(); 
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
                            description: res.message, 
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


 
    const editCategory = (category) => {
        setIsEdit(true);  
        form.setFieldsValue({
            name: category.name,
        });
        setDataViewDetail(category);
        setOpenModalCategory(true);
    };


    const onClose = () => {
        setOpenViewDetail(false);
        setDataViewDetail(null);
    }

    const columns = [
        {
            title: 'Id',
            dataIndex: 'id',
            render: (text, record, index) => {
                return (
                    <a href='#' onClick={() => {
                        setDataViewDetail(record);
                        setOpenViewDetail(true);
                    }}>{record.id}</a>
                )
            }
        },
        {
            title: 'Tên thể loại',
            dataIndex: 'name'
        },
       
        {
            title: 'Thao tác',
            key: 'action',
            render: (text, record) => (
                <Button
                    type="link"
                    onClick={() => {
                        editCategory(record);
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
        fetchCategory();  // Lấy lại danh sách tác giả ban đầu (không có tìm kiếm)
    };


    return (
        <>

            <Modal
                title={isEdit ? "Chỉnh sửa nhà xuất bản" : "Thêm mới nhà xuất bản"}  // Tiêu đề thay đổi khi sửa hoặc tạo mới
                open={openModalCategory}
                onOk={() => { form.submit(); }}
                onCancel={() => setOpenModalCategory(false)}
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
                        label="Tên thể loại"
                        name="name"
                        rules={[{ required: true, message: 'Vui lòng nhập tên thể loại' }]} >
                        <Input />
                    </Form.Item>
  
                </Form>

                <Divider />

                <h3>Danh sách nhà xuất bản</h3>
                <Button onClick={handleReset} type="default" style={{ marginLeft: 10 }}>
                    Reset
                </Button>

                <Table
                    // title={renderHeader}
                    loading={isLoading}
                    columns={columns}
                    dataSource={listCategory2}
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
                title="Chi tiết thể loại"
                open={openViewDetail}
                onCancel={onClose}
                footer={null}
                width={600}
            >
                <Descriptions column={2} bordered>
                    <Descriptions.Item label="Tên thể loại">{dataViewDetail?.name}</Descriptions.Item>
                </Descriptions>
            </Modal>

            {<BookModalCreate
                listCategory2={listCategory2}       
                setListCategory2={setListCategory2} 
            />
            }
        </>
    );
};

export default CategoryTable;
