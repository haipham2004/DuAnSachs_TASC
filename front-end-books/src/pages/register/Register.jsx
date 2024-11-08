import { Button, Divider, Form, Input, message, notification, Select } from 'antd';
import { useState } from 'react';
import { json, Link, useNavigate } from 'react-router-dom';
import { callRegister } from '../../services/Api';
import './Register.scss';

const RegisterPage = () => {
  const navigate = useNavigate();
  const [isSubmit, setIsSubmit] = useState(false);

  const onFinish = async (values) => {
    const { username, email, phone, password, role } = values;
    setIsSubmit(true);
    const res = await callRegister(username, email, phone, password, [role]);
    setIsSubmit(false);
    if (res && res.message && res.statusCode) {
      message.success('Đăng ký tài khoản thành công!');
      navigate('/login')
    } else {
      notification.error({
        message: "Có lỗi xảy ra",
        description:
          res.message && Array.isArray(res.message) ? res.message[0] : res.message,
        duration: 5
      })
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
                label="Họ tên"
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
