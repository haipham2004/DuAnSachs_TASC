import React, { useState } from 'react';
import { Button, Col, Form, Input, Row, theme } from 'antd';

const InputSearch = (props) => {
    const { token } = theme.useToken();
    const [form] = Form.useForm();

    const formStyle = {
        maxWidth: 'none',
        background: token.colorFillAlter,
        borderRadius: token.borderRadiusLG,
        padding: 24,
    };

    const onFinish = (values) => {
        console.log("Values check: ",values)
        let query = "";
        //build query
        if (values.title) {
            query += `&title=${values.title}`
        }
        if (values.nameAuthor) {
            query += `&nameAuthor=${values.nameAuthor}`
        }

        if (values.namePublisher) {
            query += `&namePublisher=${values.namePublisher}`
        }

        if (values.nameCategory) {
            query += `&nameCategory=${values.nameCategory}`
        }

        if (query) {
            props.handleSearch(query);
        }

    };

    return (
        <Form form={form} name="advanced_search" style={formStyle} onFinish={onFinish}>
            <Row gutter={24}>
                <Col span={6}>
                    <Form.Item
                        labelCol={{ span: 24 }}
                        name={`title`}
                        label={`Tên sách`}
                    >
                        <Input />
                    </Form.Item>
                </Col>
                <Col span={6}>
                    <Form.Item
                        labelCol={{ span: 24 }}
                        name={`nameAuthor`}
                        label={`Tác giả`}
                    >
                        <Input />
                    </Form.Item>
                </Col>

                <Col span={6}>
                    <Form.Item
                        labelCol={{ span: 24 }}
                        name={`namePublisher`}
                        label={`Nhà xuất bản`}
                    >
                        <Input />
                    </Form.Item>
                </Col>

                <Col span={6}>
                    <Form.Item
                        labelCol={{ span: 24 }}
                        name={`nameCategory`}
                        label={`Thể loại`}
                    >
                        <Input />
                    </Form.Item>
                </Col>
            </Row>
            <Row>
                <Col span={24} style={{ textAlign: 'right' }}>
                    <Button type="primary" htmlType="submit">
                        Search
                    </Button>
                    <Button
                        style={{ margin: '0 8px' }}
                        onClick={() => {
                            form.resetFields();
                            props.setFilter("");
                        }}
                    >
                        Clear
                    </Button>
    
                </Col>
            </Row>
        </Form>
    );
};


export default InputSearch;
