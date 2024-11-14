import React, { useState } from 'react';
import { Button, Select, Divider, Form, Input, message, Modal, notification } from 'antd';
import { callCreateUser } from '../../../services/Api';

const UserModalCreate = (props) => {
    const { openModalCreate, setOpenModalCreate } = props;
    const [isSubmit, setIsSubmit] = useState(false);
    const [form] = Form.useForm();

    const onFinish = async (values) => {
        const { username, password, email, phone, fullName, addRess, idRoles } = values;
        setIsSubmit(true);
        const res = await callCreateUser(username, password, email, phone, fullName, addRess, idRoles);
        console.log("API posst",res)
        setIsSubmit(false);
        if (res && res.results) {
            message.success('Tạo mới user thành công');
            form.resetFields();
            setOpenModalCreate(false);
            await props.fetchUser();
        } else {
            // Xử lý thông báo lỗi từ messageValidation (nếu có)
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

            // Xử lý thông báo lỗi chung từ "message" (ví dụ: "Phone number already exists")
            if (res.message) {
                notification.error({
                    message: "Có lỗi xảy ra",
                    description: res.message,  // Thông báo lỗi tổng quát từ API
                    duration: 5,
                });
            }
        }
    };

    return (
        <Modal
            title="Thêm mới người dùng"
            open={openModalCreate}
            onOk={() => { form.submit(); }}
            onCancel={() => setOpenModalCreate(false)}
            okText={"Tạo mới"}
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
                    labelCol={{ span: 24 }}
                    label="Tên hiển thị"
                    name="username"
                    rules={[{ required: true, message: 'Vui lòng nhập tên hiển thị!' }]}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    labelCol={{ span: 24 }}
                    label="Password"
                    name="password"
                    rules={[{ required: true, message: 'Vui lòng nhập mật khẩu!' }]}
                >
                    <Input.Password />
                </Form.Item>
                <Form.Item
                    labelCol={{ span: 24 }}
                    label="Email"
                    name="email"
                    rules={[{ required: true, message: 'Vui lòng nhập email!' }]}
                >
                    <Input />
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

                <Form.Item label="ROLE" name="idRoles" labelCol={{ span: 24 }} rules={[{ required: true, message: 'Vui lòng chọn role!' }]}>
                    <Select>
                        <Select.Option value={1}>Role Admin</Select.Option>
                        <Select.Option value={2}>Role User</Select.Option>
                    </Select>
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default UserModalCreate;
