import React, { useEffect, useState } from 'react';
import { Table, Row, Col, Button, Modal, Descriptions, message } from 'antd';
import { callFetchListOrder, getOrderWithItems } from '../../../services/Api'; // Đảm bảo API này có sẵn
import { ReloadOutlined } from '@ant-design/icons';

const MangeOrder = () => {
    const [listOrder, setListOrder] = useState([]);
    const [orderItems, setOrderItems] = useState([]); // Lưu trữ danh sách sản phẩm của đơn hàng
    const [current, setCurrent] = useState(1);
    const [pageSize, setPageSize] = useState(5);
    const [total, setTotal] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [filter, setFilter] = useState("");
    const [openViewDetail, setOpenViewDetail] = useState(false); // Điều khiển việc mở modal
    const [dataViewDetail, setDataViewDetail] = useState(null); // Lưu dữ liệu đơn hàng

    useEffect(() => {
        fetchOrder();
    }, [current, pageSize, filter]);

    const fetchOrder = async () => {
        setIsLoading(true);
        let query = `pageNumber=${current}&pageSize=${pageSize}`;
        if (filter) {
            query += `&${filter}`;
        }

        const res = await callFetchListOrder(query);
        if (res && res.data) {
            setListOrder(res.data.content);
            setTotal(res.data.totalElements);
        }
        setIsLoading(false);
    };

    const fetchOrderItems = async (orderId) => {
        setIsLoading(true);
        try {
            const res = await getOrderWithItems(orderId);  // Gọi API để lấy các sản phẩm của đơn hàng
            if (res && res.data) {
                setOrderItems(res.data);  // Cập nhật danh sách sản phẩm
                setDataViewDetail(res.data); // Cập nhật chi tiết đơn hàng
                setOpenViewDetail(true);  // Mở modal chi tiết
            }
        } catch (error) {
            message.error('Có lỗi khi tải chi tiết đơn hàng');
        }
        setIsLoading(false);
    };

    const columns = [
        {
            title: 'Id',
            dataIndex: 'orderId',
            render: (text, record, index) => {
                return (
                    <a href='#' onClick={() => {
                        setDataViewDetail(record);          // Lưu lại thông tin đơn hàng
                        fetchOrderItems(record.orderId);    // Gọi API lấy chi tiết sản phẩm của đơn hàng
                        setOpenViewDetail(true);            // Mở modal chi tiết
                    }}>
                        {record.orderId}
                    </a>
                );
            }
        }
        ,
        {
            title: 'Họ tên khách đặt',
            dataIndex: 'fullNameUsers',
            sorter: true,
        },
        {
            title: 'Address',
            dataIndex: 'shippingAddress',
            sorter: true,
        },
        {
            title: 'Số điện thoại',
            dataIndex: 'phoneUsers',
            sorter: true,
        },
        {
            title: 'Tổng tiền',
            dataIndex: 'total',
            render: (text, record) => {
                return (
                    <>{new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(record.total)}</>
                );
            },
            sorter: true,
        },
        {
            title: 'Trạng thái ',
            dataIndex: 'status',
            sorter: true,
        },
        {
            title: 'Thời gian đặt',
            dataIndex: 'createdAt',
            sorter: true,
            render: (text, record) => {
                const date = new Date(record.createdAt);
                const padToTwoDigits = (num) => (num < 10 ? `0${num}` : num);
                const day = padToTwoDigits(date.getDate());
                const month = padToTwoDigits(date.getMonth() + 1);
                const year = date.getFullYear();
                const hour = padToTwoDigits(date.getHours());
                const minute = padToTwoDigits(date.getMinutes());
                return `${day}/${month}/${year} ${hour}:${minute}`;
            },
        },
    ];

    const onChange = (pagination, filters, sorter) => {
        if (pagination && pagination.current !== current) {
            setCurrent(pagination.current);
        }
        if (pagination && pagination.pageSize !== pageSize) {
            setPageSize(pagination.pageSize);
            setCurrent(1);
        }
    };

    const renderHeader = () => {
        return (
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <span>Table List Order</span>
                <span style={{ display: 'flex', gap: 15 }}>
                    <Button type="ghost" onClick={() => setFilter("")}>
                        <ReloadOutlined />
                    </Button>
                </span>
            </div>
        );
    };

    return (
        <>
            <Row gutter={[20, 20]}>
                <Col span={24}>
                    <Table
                        title={renderHeader}
                        loading={isLoading}
                        columns={columns}
                        dataSource={listOrder}
                        onChange={onChange}
                        rowKey="orderId"
                        pagination={{
                            current: current,
                            pageSize: pageSize,
                            showSizeChanger: true,
                            total: total,
                            showTotal: (total, range) => (
                                <div>
                                    {range[0]}-{range[1]} trên {total} rows
                                </div>
                            ),
                        }}
                    />
                </Col>
            </Row>

            {/* Modal chi tiết đơn hàng */}
            <Modal
                // title={`Chi tiết đơn hàng #${dataViewDetail?.orderId}`}
                visible={openViewDetail}
                onCancel={() => setOpenViewDetail(false)}
                footer={null}
                width={800}
            >
                {/* <Descriptions column={2} bordered>
                    <Descriptions.Item label="Họ tên khách hàng">{dataViewDetail?.fullNameUsers}</Descriptions.Item>
                    <Descriptions.Item label="Địa chỉ giao hàng">{dataViewDetail?.shippingAddress}</Descriptions.Item>
                    <Descriptions.Item label="Số điện thoại">{dataViewDetail?.phoneUsers}</Descriptions.Item>
                    <Descriptions.Item label="Trạng thái">{dataViewDetail?.status}</Descriptions.Item>
                    <Descriptions.Item label="Tổng tiền">{new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(dataViewDetail?.total)}</Descriptions.Item>
                </Descriptions> */}

                {/* Hiển thị danh sách sản phẩm trong đơn hàng */}
                <h3>Danh sách sản phẩm trong đơn hàng</h3>
                <Table
                    dataSource={orderItems}
                    rowKey="orderItemId"
                    pagination={false}
                    columns={[
                        { title: 'Tên sản phẩm', dataIndex: 'tileBook' },
                        { title: 'Số lượng', dataIndex: 'quantity' },
                        { title: 'Đơn giá', dataIndex: 'price', render: (text) => new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(text) },
                        { title: 'Tổng giá', dataIndex: 'total', render: (text) => new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(text) },
                    ]}
                />
            </Modal>
        </>
    );
};

export default MangeOrder;
