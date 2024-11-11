import React, { useEffect, useState } from 'react';
import { Table, Row, Col, Button, message, notification, Switch, } from 'antd';
import InputSearch from './InputSearch';
import { callDeleteBook, callFetchListBook } from '../../../services/Api';
import { DeleteTwoTone, EditTwoTone, PlusOutlined, ReloadOutlined, DownloadOutlined, CheckOutlined, CloseOutlined } from '@ant-design/icons';
// import BookModalCreate from './BookModalCreate';
import BookViewDetail from './BookViewDetail';
// import moment from 'moment/moment';
// import { FORMAT_DATE_DISPLAY } from '../../../utils/constant';
import BookModalUpdate from './BookModalUpdate';
import * as XLSX from 'xlsx';
import BookModalCreate from './BookModalCreate';

const BookTable = () => {
    const [listBook, setListBook] = useState([]);
    const [current, setCurrent] = useState(1);
    const [pageSize, setPageSize] = useState(5);
    const [total, setTotal] = useState(0);

    const [isLoading, setIsLoading] = useState(false);
    const [filter, setFilter] = useState("");
    const [sortQuery, setSortQuery] = useState("sort=-updatedAt");

    const [openModalCreate, setOpenModalCreate] = useState(false);
    const [openViewDetail, setOpenViewDetail] = useState(false);
    const [dataViewDetail, setDataViewDetail] = useState(null);

    const [openModalUpdate, setOpenModalUpdate] = useState(false);
    const [dataUpdate, setDataUpdate] = useState(null);

    useEffect(() => {
        fetchBook();
    }, [current, pageSize, filter]);

    const fetchBook = async () => {
        setIsLoading(true)
        let query = `pageNumber=${current}&pageSize=${pageSize}`;
        if (filter) {
            query += `&${filter}`;
        }
        

        const res = await callFetchListBook(query);
        if (res && res.data) {
            setListBook(res.data.content);
            setTotal(res.data.totalElements)
        }
        setIsLoading(false)
    }

    const columns = [
        {
            title: 'Id',
            dataIndex: 'bookId',
            render: (text, record, index) => {
                return (
                    <a href='#' onClick={() => {
                        setDataViewDetail(record);
                        console.log("record của tui",record)
                        setOpenViewDetail(true);
                    }}>{record.bookId}</a>
                )
            }
        },
        {
            title: 'Tên sách',
            dataIndex: 'title',
            sorter: true
        },
        {
            title: 'Tác giả',
            dataIndex: 'nameAuthor',
            sorter: true,
        },
        {
            title: 'Nhà xuất bản',
            dataIndex: 'namePublisher',
            sorter: true,
        },
        {
            title: 'Thể loại',
            dataIndex: 'nameCategory',
            sorter: true
        },
        {
            title: 'Mô tả',
            dataIndex: 'description',
            sorter: true
        },

        {
            title: 'Giá tiền',
            dataIndex: 'price',
            sorter: true,
            // https://stackoverflow.com/questions/37985642/vnd-currency-formatting
            render: (text, record, index) => {
                return (
                    <>{new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(record.price)}</>
                )
            }
        },
        // {
        //     title: 'Ngày cập nhật',
        //     dataIndex: 'updatedAt',
        //     sorter: true,
        //     render: (text, record, index) => {
        //         return (
        //             <>{moment(record.updatedAt).format(FORMAT_DATE_DISPLAY)}</>
        //         )
        //     }

        // },
        {
            title: 'Action',
            width: 100,
            render: (text, record, index) => {
                return (
                    <>

                        {/* <Popconfirm
                            placement="leftTop"
                            title={"Xác nhận xóa book"}
                            description={"Bạn có chắc chắn muốn xóa book này ?"}
                            onConfirm={() => handleDeleteBook(record.bookId)}
                            okText="Xác nhận"
                            cancelText="Hủy"
                        >
                            <span style={{ cursor: "pointer", margin: "0 20px" }}>
                                <DeleteTwoTone twoToneColor="#ff4d4f" />
                            </span>
                        </Popconfirm> */}

                        <Switch
                            checkedChildren={<CheckOutlined />}  // Khi bật, hiển thị dấu check
                            unCheckedChildren={<CloseOutlined />}  // Khi tắt, hiển thị dấu x
                            checked={!record.deleted}  // Nếu deleted = false, bật switch, nếu deleted = true, tắt switch
                            onChange={(checked) => handleSwitchChange(checked, record.bookId)}  // Khi thay đổi trạng thái switch, gọi handleSwitchChange
                        />

                        <EditTwoTone
                            twoToneColor="#f57800" style={{ cursor: "pointer" }}
                            onClick={() => {
                                setOpenModalUpdate(true);
                                // setDataUpdate(record);
                                setDataUpdate({
                                    ...record,
                                    authorId: record.authorId, // Thêm authorId
                                    publisherId: record.publisherId, // Thêm publisherId
                                    categoryId: record.categoryId // Thêm categoryId
                                });
                              console.log("Dữ liệu data: ",dataUpdate)                                
                            }}
                        />
                    </>

                )
            }
        }
    ];

    const onChange = (pagination, filters, sorter, extra) => {
        if (pagination && pagination.current !== current) {
            setCurrent(pagination.current)
        }
        if (pagination && pagination.pageSize !== pageSize) {
            setPageSize(pagination.pageSize)
            setCurrent(1);
        }
        if (sorter && sorter.field) {
            const q = sorter.order === 'ascend' ? `sort=${sorter.field}` : `sort=-${sorter.field}`;
            setSortQuery(q);
        }
    };
    
    const handleSwitchChange = async (checked, id) => {
        const deleteFlag = checked ? false : true;  // Nếu checked = true, thì sách chưa bị xóa (deleted = false), ngược lại (checked = false) thì xóa mềm (deleted = true)
        
        try {
            const res = await callDeleteBook(id, deleteFlag); // Gọi API để cập nhật trạng thái deleted của sách
            if (res && res.statusCode) {
                const action = deleteFlag ? 'xóa mềm' : 'khôi phục'; // Xác định hành động dựa trên trạng thái deleteFlag
                message.success(`${action} sách thành công`);
                fetchBook();  // Tải lại danh sách sách
            } else {
                notification.error({
                    message: 'Có lỗi xảy ra',
                    description: res.message || 'Không thể xử lý yêu cầu'
                });
            }
        } catch (error) {
            notification.error({
                message: 'Có lỗi xảy ra',
                description: error.message || 'Không thể kết nối với máy chủ'
            });
        }
    };
    



    // change button color: https://ant.design/docs/react/customize-theme#customize-design-token
    const renderHeader = () => {
        return (
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <span>Table List Books</span>
                <span style={{ display: 'flex', gap: 15 }}>
                    <Button
                        icon={<DownloadOutlined />}
                        type="primary"
                        onClick={() => handleExportData()}
                    >Export</Button>

                    <Button
                        icon={<PlusOutlined />}
                        type="primary"
                        onClick={() => setOpenModalCreate(true)}
                    >Thêm mới</Button>
                    <Button type='ghost' onClick={() => {
                        setFilter("");
                        setSortQuery("")
                    }}>
                        <ReloadOutlined />
                    </Button>


                </span>
            </div>
        )
    }

    const handleSearch = (query) => {
        setFilter(query);
    }

    const handleExportData = async () => {
        setIsLoading(true);

        // Gọi API để lấy tất cả dữ liệu nếu dữ liệu đang phân trang.
        let allBooks = [];
        let currentPage = 1;
        let totalPages = 1; // Biến này sẽ giúp kiểm soát việc lấy tất cả trang

        // Lặp qua tất cả các trang
        while (currentPage <= totalPages) {
            const query = `pageNumber=${currentPage}&pageSize=${pageSize}`;
            const res = await callFetchListBook(query);

            if (res && res.data) {
                allBooks = [...allBooks, ...res.data.content];  // Thêm dữ liệu của trang hiện tại vào `allBooks`
                totalPages = Math.ceil(res.data.totalElements / pageSize);  // Cập nhật tổng số trang
            }

            currentPage++;
        }

        // Nếu có dữ liệu, tạo và xuất Excel
        if (allBooks.length > 0) {
            const worksheet = XLSX.utils.json_to_sheet(allBooks); // Chuyển toàn bộ dữ liệu thành worksheet
            const workbook = XLSX.utils.book_new();  // Tạo mới workbook
            XLSX.utils.book_append_sheet(workbook, worksheet, "Sheet1");  // Thêm sheet vào workbook
            XLSX.writeFile(workbook, "ExportBook.xlsx");  // Xuất file Excel
        }

        setIsLoading(false);
    };

    return (
        <>
            <Row gutter={[20, 20]}>
                <Col span={24}>
                    <InputSearch
                        handleSearch={handleSearch}
                        setFilter={setFilter}
                    />
                </Col>
                <Col span={24}>
                    <Table
                        title={renderHeader}
                        loading={isLoading}
                        columns={columns}
                        dataSource={listBook}
                        onChange={onChange}
                        rowKey="bookId"
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
                </Col>
            </Row>
            {<BookModalCreate
                openModalCreate={openModalCreate}
                setOpenModalCreate={setOpenModalCreate}
                fetchBook={fetchBook}
            />}

            {<BookViewDetail
                openViewDetail={openViewDetail}
                setOpenViewDetail={setOpenViewDetail}
                dataViewDetail={dataViewDetail}
                setDataViewDetail={setDataViewDetail}
            />
            }
            {<BookModalUpdate
                openModalUpdate={openModalUpdate}
                setOpenModalUpdate={setOpenModalUpdate}
                dataUpdate={dataUpdate}
                setDataUpdate={setDataUpdate}
                fetchBook={fetchBook}
            /> }

        </>
    )
}


export default BookTable;
