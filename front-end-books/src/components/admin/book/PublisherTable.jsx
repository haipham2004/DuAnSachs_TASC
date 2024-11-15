import React, { useState, useEffect } from 'react';
import { Divider, Descriptions, Form, Input, message, Modal, notification, Table, Button } from 'antd';
import { callFetchPublisher, callCreatePublisher, callUpdatePublisher } from '../../../services/Api';
import { PlusOutlined } from '@ant-design/icons';
import BookModalCreate from './BookModalCreate';

const PublisherTable = (props) => {
    const [isSubmit, setIsSubmit] = useState(false);
    const [form] = Form.useForm();
    const { openModalPublisher, setOpenModalPublisher } = props;
    const [listPublisher2, setListPublisher2] = useState([]);
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
        fetchPublisher();
    }, [current, pageSize, filter]);

    const fetchPublisher = async () => {
        setIsLoading(true)
        let query = `pageNumber=${current}&pageSize=${pageSize}`;
        if (filter) {
            query += `&${filter}`;
        }


        const res = await callFetchPublisher(query);
        if (res && res.data) {
            setListPublisher2(res.data.content);
            setTotal(res.data.totalElements)
        }
        setIsLoading(false)
    }




    const onFinish = async (values) => {
        const { name, address,phone, email } = values;
        setIsSubmit(true); 

        try {
            let res;
            if (isEdit) {
                
                res = await callUpdatePublisher(dataViewDetail.id, name, address,phone, email);
                if (res && res.data) {
                    message.success('Cập nhật nhà xuất bản thành công');
                    setOpenModalPublisher(true);
                    fetchPublisher(); 
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
              
                res = await callCreatePublisher(name, address,phone, email);
                if (res && res.data) {
                    message.success('Tạo mới nhà xuất bản thành công');
                    form.resetFields();  
                    setOpenModalPublisher(true);
                    fetchPublisher(); 
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


    const editPublisher = (publisher) => {
        setIsEdit(true);  
        form.setFieldsValue({
            name: publisher.name,
            phone: publisher.phone,
            address: publisher.address,
            email: publisher.email
        });
        setDataViewDetail(publisher);
        setOpenModalPublisher(true);
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
            dataIndex: 'address',
        },
        {
            title: 'Email',
            dataIndex: 'email',
        },
        {
            title: 'Thao tác',
            key: 'action',
            render: (text, record) => (
                <Button
                    type="link"
                    onClick={() => {
                        editPublisher(record);
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


    const handleReset = () => {
        form.resetFields();  
        setSearchText(''); 
        setIsEdit(false);  
        fetchPublisher();  
    };


    return (
        <>

            <Modal
                title={isEdit ? "Chỉnh sửa nhà xuất bản" : "Thêm mới nhà xuất bản"}  // Tiêu đề thay đổi khi sửa hoặc tạo mới
                open={openModalPublisher}
                onOk={() => { form.submit(); }}
                onCancel={() => setOpenModalPublisher(false)}
                okText={isEdit ? "Lưu" : "Tạo mới"}
                cancelText={"Hủy"}
                confirmLoading={isSubmit}
                width={800}  
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
                        rules={[{ required: true, message: 'Vui lòng nhập tên' }]} >
                        <Input />
                    </Form.Item>

                    <Form.Item
                        labelCol={{ span: 24 }}
                        label="Địa chỉ tác giả"
                        name="address"
                        rules={[{ required: true, message: 'Vui lòng nhập Địa chỉ ' }]} >
                        <Input />
                    </Form.Item>

                    <Form.Item
                        labelCol={{ span: 24 }}
                        label="Số điện thoại"
                        name="phone"
                        rules={[{ required: true, message: 'Vui lòng nhập Số điện thoại ' }]} >
                        <Input />
                    </Form.Item>

                    <Form.Item
                        labelCol={{ span: 24 }}
                        label="Email"
                        name="email"
                        rules={[{ required: true, message: 'Vui lòng nhập email ' }]} >
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
                    dataSource={listPublisher2}
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
                title="Chi tiết nhà xuất bản"
                open={openViewDetail}
                onCancel={onClose}
                footer={null}
                width={600}
            >
                <Descriptions column={2} bordered>
                    <Descriptions.Item label="Tên tác giả">{dataViewDetail?.name}</Descriptions.Item>
                    <Descriptions.Item label="Số điện thoại">{dataViewDetail?.phone}</Descriptions.Item>
                    <Descriptions.Item label="Địa chỉ">{dataViewDetail?.address}</Descriptions.Item>
                    <Descriptions.Item label="Email">{dataViewDetail?.email}</Descriptions.Item>
                </Descriptions>
            </Modal>

            {<BookModalCreate
                listPublisher2={listPublisher2}       
                setListPublisher2={setListPublisher2} 
            />
            }
        </>
    );
};

export default PublisherTable;
