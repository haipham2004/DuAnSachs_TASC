import React from 'react';
import { Button, Divider, Checkbox, Form, Input, message, notification } from 'antd';
import { Link, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { callLogin } from '../../services/Api';
import { doLoginAction } from '../../redux/account/AccountSlice';

const onFinishFailed = (errorInfo) => {
  console.log('Failed:', errorInfo);
};

const LoginPage = () => {

  const navigate  =useNavigate();

  const [isSubmit, setIsSubmit] = useState(false);

  const dispatch = useDispatch();

  const onFinish = async (values) => {

  const { username, password } = values;
  setIsSubmit(true)
  const res= await callLogin(username, password);
  setIsSubmit(false)
 console.log("login ",res)
 localStorage.setItem('access_token', res.jwtToken);
 console.log("Data localStorage: ", localStorage.getItem('access_token'));

  if(res && res.username){
 
    dispatch(doLoginAction({
      username: res.username,
      roles: res.roles,
      email: res.email,
      phone: res.phone,
  }));
  
    message.success('Đăng nhập tài khoản thành công!');

     if (res.roles.includes('ROLE_ADMIN')) {
      navigate('/admin'); 
    } else if (res.roles.includes('ROLE_USER')) {
      navigate('/'); 
    } else {
      navigate('/');
    }
  }else {
    notification.error({
        message: "Login fail",
        description:
            res.message && Array.isArray(res.message) ? res.message[0] : res.message,
        duration: 5
    })
}

};

//oke login
  


  return (
    <>
      <p>Login nha</p>
      <Form
        name="basic"
        labelCol={{
          span: 8,
        }}
        wrapperCol={{
          span: 16,
        }}
        style={{
          maxWidth: 600, margin: '0 auto'
        }}
        initialValues={{
          remember: true,
        }}
        onFinish={onFinish}
        onFinishFailed={onFinishFailed}
        autoComplete="off"
      >
        <Form.Item
          label="Username"
          name="username"
          rules={[{ required: true, message: 'Please input your username!' }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label="Password"
          name="password"
          rules={[{ required: true, message: 'Please input your password!' }]}
        >
          <Input.Password />
        </Form.Item>

        <Form.Item
          name="remember"
          valuePropName="checked"
          wrapperCol={{
            offset: 8,
            span: 16,
          }}
        >
          <Checkbox>Remember me</Checkbox>
        </Form.Item>

        <Form.Item
          wrapperCol={{
            offset: 8,
            span: 16,
          }}
        >
          <Button type="primary" htmlType="submit" loading={isSubmit}>
            Login
          </Button>
        </Form.Item>
        <Divider>Or</Divider>
        <p className="text text-normal">Chưa có tài khoản ?
          <span>
            <Link to='/register' > Đăng Ký </Link>
          </span>
        </p>

      </Form>
    </>
  );
};

export default LoginPage;
