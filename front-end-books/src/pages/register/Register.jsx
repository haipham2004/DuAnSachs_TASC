import { Button, Divider, Form, Input, message, notification, Select } from 'antd';
import { useState } from 'react';
import { json, Link, useNavigate } from 'react-router-dom';
import { callRegister } from '../../services/Api';
import './Register.scss';

const RegisterPage = () => {
  const navigate = useNavigate();
  const [isSubmit, setIsSubmit] = useState(false);

  const onFinish = async (values) => {
    
    const { username, email, phone, password, fullName, addRess,role } = values;
    setIsSubmit(true);
    const res = await callRegister(username, email, phone, password, fullName, addRess, [role]);
    setIsSubmit(false);
    if (res && res.message && res.statusCode) {
      message.success('Đăng ký tài khoản thành công!');
      navigate('/login')
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
  };


  return (
    <div className="register-page">
      <main className="main">
        <div className="container">
          <section className="wrapper">
            <div className="heading">
              <h2 className="text text-large">Đăng Ký Tài Khoản</h2>
              <Divider />
            </div>
            <Form
              name="basic"
              // style={{ maxWidth: 600, margin: '0 auto' }}
              onFinish={onFinish}
              autoComplete="off"
            >
              <Form.Item
                labelCol={{ span: 24 }} //whole column
                label="UserName"
                name="username"
                rules={[{ required: true, message: 'Họ tên không được để trống!' }]}
              >
                <Input />
              </Form.Item>


              <Form.Item
                labelCol={{ span: 24 }} //whole column
                label="Email"
                name="email"
                rules={[{ required: true, message: 'Email không được để trống!' }]}
              >
                <Input />
              </Form.Item>

              <Form.Item
                labelCol={{ span: 24 }} //whole column
                label="Mật khẩu"
                name="password"
                rules={[{ required: true, message: 'Mật khẩu không được để trống!' }]}
              >
                <Input.Password />
              </Form.Item>
              <Form.Item
                labelCol={{ span: 24 }} //whole column
                label="Số điện thoại"
                name="phone"
                rules={[{ required: true, message: 'Số điện thoại không được để trống!' }]}
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

              <Form.Item label="ROLE" name="role" labelCol={{ span: 24 }}>
                <Select>
                  <Select.Option value="ADMIN">ROLE_ADMIN</Select.Option>
                  <Select.Option value="USER">ROLE_USER</Select.Option>
                </Select>
              </Form.Item>

              <Form.Item
              // wrapperCol={{ offset: 6, span: 16 }}
              >
                <Button type="primary" htmlType="submit" loading={isSubmit}>
                  Đăng ký
                </Button>
              </Form.Item>
              <Divider>Or</Divider>
              <p className="text text-normal">Đã có tài khoản ?
                <span>
                  <Link to='/login' > Đăng Nhập </Link>
                </span>
              </p>
            </Form>
          </section>
        </div>
      </main>
    </div>
  )
}

export default RegisterPage;
