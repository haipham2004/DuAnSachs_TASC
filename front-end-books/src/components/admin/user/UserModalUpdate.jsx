import React, { useEffect, useState } from 'react';
import { Button, Select, Divider, Form, Input, message, Modal, notification } from 'antd';
import { callUpdateUser } from '../../../services/Api';

const UserModalUpdate = (props) => {
    const { openModalUpdate, setOpenModalUpdate, dataUpdate, setDataUpdate } = props;
    const [isSubmit, setIsSubmit] = useState(false);

    const [form] = Form.useForm();

    // Hàm xử lý khi form submit
    const onFinish = async (values) => {
        const { userId, username, password, email, phone, fullName, addRess,idRoles } = values;
        setIsSubmit(true);
        const res = await callUpdateUser(userId, username, password, email, phone,fullName, addRess, idRoles);
        console.log("update res ",res)
        if (res && res.results) {
            message.success('Cập nhật user thành công');
            setOpenModalUpdate(false);
            await props.fetchUser();
        } else {
            notification.error({
                message: 'Đã có lỗi xảy ra',
                description: res.message
            });
        }
        setIsSubmit(false);
    };

    // Cập nhật giá trị form mỗi khi dataUpdate thay đổi
    useEffect(() => {
        if (dataUpdate) {
            // Debug để kiểm tra giá trị dataUpdate
            console.log('dataUpdate:', dataUpdate);

            // Set giá trị cho form
            form.setFieldsValue({
                ...dataUpdate,
                idRoles: dataUpdate.idRoles || 1 // Nếu idRoles không có, set mặc định là 1 (Role Admin)
            });
        }
    }, [dataUpdate, form]);

    return (
        <>
            <Modal
                title="Cập nhật người dùng"
                open={openModalUpdate}
                onOk={() => { form.submit(); }}
                onCancel={() => {
                    setOpenModalUpdate(false);
                    setDataUpdate(null);
                }}
                okText={"Cập nhật"}
                cancelText={"Hủy"}
                confirmLoading={isSubmit}
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
                        hidden
                        labelCol={{ span: 24 }}
                        label="Id"
                        name="userId"
                        rules={[{ required: true, message: 'Vui lòng nhập Id!' }]}
                    >
                        <Input />
                    </Form.Item>

                    <Form.Item
                        labelCol={{ span: 24 }}
                        label="Tên hiển thị"
                        name="username"
                        rules={[{ required: true, message: 'Vui lòng nhập tên hiển thị!' }]}
                    >
                        <Input />
                    </Form.Item>

                    <Form.Item
                        labelCol={{ span: 24 }}
                        label="Email"
                        name="email"
                        rules={[{ required: true, message: 'Vui lòng nhập email!' }]}
                    >
                        <Input disabled />
                    </Form.Item>

                    <Form.Item
                        labelCol={{ span: 24 }}
                        label="Số điện thoại"
                        name="phone"
                        rules={[{ required: true, message: 'Vui lòng nhập số điện thoại!' }]}
                    >
                        <Input />
                    </Form.Item>

                <Form.Item
                    labelCol={{ span: 24 }} //whole column
                    label="FullName"
                    name="fullName"
                    rules={[{ required: true, message: 'Họ tên không được để trống!' }]}
                >
                    <Input />
                </Form.Item>


                <Form.Item
                    labelCol={{ span: 24 }} //whole column
                    label="Địa chỉ"
                    name="addRess"
                    rules={[{ required: true, message: 'Địa chỉ không được để trống!' }]}
                >
                    <Input />
                </Form.Item>
                    

                    <Form.Item
                        label="ROLE"
                        name="idRoles"
                        labelCol={{ span: 24 }}
                        rules={[{ required: true, message: 'Vui lòng chọn role!' }]}
                    >
                        <Select>
                            <Select.Option value={1}>Role Admin</Select.Option>
                            <Select.Option value={2}>Role User</Select.Option>
                        </Select>
                    </Form.Item>
                </Form>
            </Modal>
        </>
    );
};

export default UserModalUpdate;
