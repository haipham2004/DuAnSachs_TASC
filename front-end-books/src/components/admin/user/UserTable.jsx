import React, { useEffect, useState } from 'react';
import { Table, Row, Col, Popconfirm, Button, message, notification } from 'antd';
import { DeleteTwoTone, EditTwoTone, PlusOutlined, ReloadOutlined } from '@ant-design/icons';
import { callFetchListUser, callDeleteUser } from '../../../services/Api';
import UserViewDetail from './UserViewDetail';
import UserModalCreate from './UserModalCreate';
import UserModalUpdate from './UserModalUpdate';
import InputSearch from './InputSearch';

const UserTable = () => {
    const [listUser, setListUser] = useState([]);
    const [current, setCurrent] = useState(1);
    const [pageSize, setPageSize] = useState(5);
    const [total, setTotal] = useState(0);
    const [isLoading, setIsLoading] = useState(false);

    const [openModalCreate, setOpenModalCreate] = useState(false);
    const [openViewDetail, setOpenViewDetail] = useState(false);
    const [dataViewDetail, setDataViewDetail] = useState(null);
    const [openModalUpdate, setOpenModalUpdate] = useState(false);
    const [dataUpdate, setDataUpdate] = useState(null);

    useEffect(() => {
        fetchUser();
    }, [current, pageSize]);

    const fetchUser = async () => {
        setIsLoading(true);
            const res = await callFetchListUser(current, pageSize);
            if (res && res.results) {
                setListUser(res.results.content); 
                setTotal(res.results.totalElements); 
            }
        
        setIsLoading(false);
    };

    const columns = [
        {
            title: 'Id',
            dataIndex: 'id',
            render: (text, record) => (
                <a href="#" onClick={() => {
                    setDataViewDetail(record);
                    setOpenViewDetail(true);
                }}>{record.userId}</a>
            ),
        },
        {
            title: 'User name',
            dataIndex: 'username',
            sorter: true,
        },
        {
            title: 'Email',
            dataIndex: 'email',
            sorter: true,
        },
        {
            title: 'Phone',
            dataIndex: 'phone',
            sorter: true,
        },
        {
            title: 'Action',
            render: (text, record) => (
                <>
                    <Popconfirm
                        placement="leftTop"
                        title={"Xác nhận xóa user"}
                        description={"Bạn có chắc chắn muốn xóa user này?"}
                        onConfirm={() => handleDeleteUser(record.userId)}
                        okText="Xác nhận"
                        cancelText="Hủy"
                    >
                        <span style={{ cursor: "pointer", margin: "0 20px" }}>
                            <DeleteTwoTone twoToneColor="#ff4d4f" />
                        </span>
                    </Popconfirm>
                    <EditTwoTone
                        twoToneColor="#f57800"
                        style={{ cursor: "pointer" }}
                        onClick={() => {
                            setOpenModalUpdate(true);
                            setDataUpdate(record);
                        }}
                    />
                </>
            ),
        }
    ];


    const onChange = (pagination, filters, sorter) => {
        console.log("Pagination changed: ", pagination);
        if (pagination.current !== current) {
            setCurrent(pagination.current);
        }
        if (pagination.pageSize !== pageSize) {
            setPageSize(pagination.pageSize);
            setCurrent(1); 
        }

        if (sorter.field) {
            const q = sorter.order === 'ascend' ? `sort=${sorter.field}` : `sort=-${sorter.field}`;
        }

        fetchUser();
    };

    const handleDeleteUser = async (userId) => {
        const res = await callDeleteUser(userId);
        if (res && res.message) {
            message.success('Xóa user thành công');
            fetchUser();  
        } else {
            notification.error({
                message: 'Có lỗi xảy ra',
                description: res.message,
            });
        }
    };

    const renderHeader = () => (
        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
            <span>Table List Users</span>
            <span style={{ display: 'flex', gap: 15 }}>
                <Button
                    icon={<PlusOutlined />}
                    type="primary"
                    onClick={() => setOpenModalCreate(true)}
                >
                    Thêm mới
                </Button>
                <Button type='ghost' onClick={() => {
       
                    setFilter("");
                }}>
                    <ReloadOutlined />
                </Button>
            </span>
        </div>
    );

    return (
        <>
            <Row gutter={[20, 20]}>
            <Col span={24}>
                    <InputSearch
                        // handleSearch={handleSearch}
                        // setFilter={setFilter}
                    />
                </Col>

                <Col span={24}>
                    <Table
                        title={renderHeader}
                        loading={isLoading}
                        columns={columns}
                        dataSource={listUser}
                        onChange={onChange}
                        rowKey="userId"
                        pagination={{
                            current: current,
                            pageSize: pageSize,
                            showSizeChanger: true,
                            total: total,
                            showTotal: (total, range) => (<div>{range[0]}-{range[1]} trên {total} rows</div>),
                        }}
                    />
                </Col>
            </Row>

            <UserModalCreate
                openModalCreate={openModalCreate}
                setOpenModalCreate={setOpenModalCreate}
                fetchUser={fetchUser}
            />


            <UserViewDetail
                openViewDetail={openViewDetail}
                setOpenViewDetail={setOpenViewDetail}
                dataViewDetail={dataViewDetail}
                setDataViewDetail={setDataViewDetail}
            />

            <UserModalUpdate
                openModalUpdate={openModalUpdate}
                setOpenModalUpdate={setOpenModalUpdate}
                dataUpdate={dataUpdate}
                setDataUpdate={setDataUpdate}
                fetchUser={fetchUser}
            />
        </>
    );
};

export default UserTable;
