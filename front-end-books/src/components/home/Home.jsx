import { FilterTwoTone, ReloadOutlined, HomeOutlined } from '@ant-design/icons';
import { Row, Col, Form, Checkbox, Divider, InputNumber, Button, Rate, Tabs, Pagination, Spin, Empty, Breadcrumb } from 'antd';
// import { useOutletContext } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { Link, useNavigate, useOutletContext } from 'react-router-dom';
import { callFetchCategory, callFetchListBook } from '../../services/Api';
import './Home.scss';
import MobileFilter from './Filter';
const Home = () => {


    const [listCategory, setListCategory] = useState([]);

    const [listBook, setListBook] = useState([]);
    const [current, setCurrent] = useState(1);
    const [pageSize, setPageSize] = useState(10);
    const [total, setTotal] = useState(0);

    const [isLoading, setIsLoading] = useState(false);
    const [filter, setFilter] = useState("");
    const [sortQuery, setSortQuery] = useState("");

    const [showMobileFilter, setShowMobileFilter] = useState(false);

    const [form] = Form.useForm();
    const navigate = useNavigate();

    console.log("filter1: ", filter)
    useEffect(() => {
        const initCategory = async () => {
            const res = await callFetchCategory();
            if (res && res.data) {
                const d = res.data.content.map(item => {
                    return { label: item.name || item, value: item.id || item };
                });
                setListCategory(d);
                console.log("listCate: ",d)
            }
        };
        initCategory();
    }, []);

    useEffect(() => {

        fetchBook();
    }, [current, pageSize, filter, sortQuery]);
    console.log("filter2: ", filter)

    const fetchBook = async () => {
        setIsLoading(true);
        let query = `pageNumber=${current}&pageSize=${pageSize}`;
        if (filter) {
            query += `&${filter}`;
        }

        if (sortQuery) {
            query += `&${sortQuery}`;
        }

        const res = await callFetchListBook(query);  // Gọi API với tham số phân trang và bộ lọc

        if (res && res.data) {
            setListBook(res.data.content);  // Cập nhật danh sách sách
            setTotal(res.data.totalElements);  // Cập nhật tổng số sách
        } else {
            console.error("Error fetching data:", res);  // Nếu có lỗi, in ra
        }

        setIsLoading(false);  // Tắt loading
    };


    const handleTabChange = (key) => {
        setSortQuery(key); // Cập nhật `sortQuery` khi người dùng chọn tab
        setCurrent(1); // Reset lại trang về 1
    };


    const handleOnchangePage = (pagination) => {
        if (pagination && pagination.current !== current) {
            setCurrent(pagination.current)
        }
        if (pagination && pagination.pageSize !== pageSize) {
            setPageSize(pagination.pageSize)
            setCurrent(1);
        }

    }



    const handleChangeFilter = (changedValues, values) => {

        if (changedValues.nameCategory) {
            let cate = values.nameCategory;

            if (typeof cate === 'string') {
                cate = [cate];  
            }
            if (Array.isArray(cate) && cate.length > 0) {
                const f = cate.join(',');
                setFilter(`nameCategory=${f}`);
            } else {
                // Nếu không có giá trị, reset filter
                setFilter('');
            }
        }
    }



    const onFinish = (values) => {
        console.log('>> check values: ', values)

        if (values?.range?.from >= 0 && values?.range?.to >= 0) {
            let f = `priceMin=${values?.range?.from}&priceMax=${values?.range?.to}`;
            if (values?.nameCategory?.length) {
                const cate = values?.nameCategory;
                f += `&nameCategory=${cate}`
            }
            setFilter(f);
        }
    }


    
    
    const items = [
        {
            key: "ntth",
            label: `Tất cả sách`,
            children: <>
            </>,
        },
        {
            key: "",
            label: `Phổ biến`,
            children: <>
                huhu
            </>,
        },
        {
            key: 'sort=-updatedAt',
            label: `Hàng Mới`,
            children: <></>,
        },
        {
            key: 'sort=price',
            label: `Giá Thấp Đến Cao`,
            children: <></>,
        }
    ];

    const nonAccentVietnamese = (str) => {
        // Kiểm tra nếu str là null hoặc undefined, nếu có trả về chuỗi rỗng
        if (typeof str !== 'string') {
            return '';
        }
    
        str = str.replace(/A|Á|À|Ã|Ạ|Â|Ấ|Ầ|Ẫ|Ậ|Ă|Ắ|Ằ|Ẵ|Ặ/g, "A");
        str = str.replace(/à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ/g, "a");
        str = str.replace(/E|É|È|Ẽ|Ẹ|Ê|Ế|Ề|Ễ|Ệ/, "E");
        str = str.replace(/è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ/g, "e");
        str = str.replace(/I|Í|Ì|Ĩ|Ị/g, "I");
        str = str.replace(/ì|í|ị|ỉ|ĩ/g, "i");
        str = str.replace(/O|Ó|Ò|Õ|Ọ|Ô|Ố|Ồ|Ỗ|Ộ|Ơ|Ớ|Ờ|Ỡ|Ợ/g, "O");
        str = str.replace(/ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ/g, "o");
        str = str.replace(/U|Ú|Ù|Ũ|Ụ|Ư|Ứ|Ừ|Ữ|Ự/g, "U");
        str = str.replace(/ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ/g, "u");
        str = str.replace(/Y|Ý|Ỳ|Ỹ|Ỵ/g, "Y");
        str = str.replace(/ỳ|ý|ỵ|ỷ|ỹ/g, "y");
        str = str.replace(/Đ/g, "D");
        str = str.replace(/đ/g, "d");
    
        // Các ký tự kết hợp (diacritical marks) UTF-8
        str = str.replace(/\u0300|\u0301|\u0303|\u0309|\u0323/g, ""); // Huyền sắc hỏi ngã nặng
        str = str.replace(/\u02C6|\u0306|\u031B/g, ""); // Â, Ê, Ă, Ơ, Ư
    
        return str;
    }
    
    const convertSlug = (str) => {
        str = nonAccentVietnamese(str);
        str = str.replace(/^\s+|\s+$/g, ''); // trim
        str = str.toLowerCase();

        // remove accents, swap ñ for n, etc
        const from = "ÁÄÂÀÃÅČÇĆĎÉĚËÈÊẼĔȆĞÍÌÎÏİŇÑÓÖÒÔÕØŘŔŠŞŤÚŮÜÙÛÝŸŽáäâàãåčçćďéěëèêẽĕȇğíìîïıňñóöòôõøðřŕšşťúůüùûýÿžþÞĐđßÆa·/_,:;";
        const to = "AAAAAACCCDEEEEEEEEGIIIIINNOOOOOORRSSTUUUUUYYZaaaaaacccdeeeeeeeegiiiiinnooooooorrsstuuuuuyyzbBDdBAa------";
        for (let i = 0, l = from.length; i < l; i++) {
            str = str.replace(new RegExp(from.charAt(i), 'g'), to.charAt(i));
        }

        str = str.replace(/[^a-z0-9 -]/g, '') // remove invalid chars
            .replace(/\s+/g, '-') // collapse whitespace and replace by -
            .replace(/-+/g, '-'); // collapse dashes

        return str;
    }

    const handleRedirectBook = (book) => {
        const slug = convertSlug(book.title);
        navigate(`/book/${slug}?id=${book.bookId}`)
    }
    handleChangeFilter
    return (
        <>


            <div style={{ background: '#efefef', padding: "20px 0" }}>
                <div className="homepage-container" style={{ maxWidth: 1440, margin: '0 auto' }}>
                    <Breadcrumb
                        style={{ margin: '5px 0' }}
                        items={[
                            {
                                title: <HomeOutlined />,
                            },
                            {
                                title: (
                                    <Link to={'/'}>
                                        <span>Trang Chủ</span>
                                    </Link>
                                ),
                            }
                        ]}
                    />

                    <Row gutter={[20, 20]}>
                        <Col md={4} sm={0} xs={0}>
                            <div style={{ padding: "20px", background: '#fff', borderRadius: 5 }}>
                                <div style={{ display: 'flex', justifyContent: "space-between" }}>
                                    <span> <FilterTwoTone />
                                        <span style={{ fontWeight: 500 }}> Bộ lọc tìm kiếm</span>
                                    </span>
                                    <ReloadOutlined title="Reset" onClick={() => {
                                        form.resetFields();
                                        setFilter('');
                                        // setSearchTerm('');
                                    }}
                                    />
                                </div>
                                <Divider />
                                <Form
                                    onFinish={onFinish}
                                    form={form}
                                    onValuesChange={(changedValues, values) => handleChangeFilter(changedValues, values)}
                                >
                                    <Form.Item
                                        name="category"
                                        labelCol={{ span: 24 }}
                                    >
                                        <Form.Item name="nameCategory" label="Danh mục sản phẩm" labelCol={{ span: 24 }}>
                                            <Checkbox.Group>
                                                <Row>
                                                    {listCategory?.map((item, index) => {
                                                        return (
                                                            <Col span={24} key={`index-${index}`} style={{ padding: '7px 0' }}>
                                                                <Checkbox value={item.label}>{item.label}</Checkbox> 
                                                                
                                                            </Col>
                                                        )
                                                    })}
                                                </Row>
                                            </Checkbox.Group>
                                        </Form.Item>

                                    </Form.Item>
                                    <Divider />
                                    <Form.Item
                                        label="Khoảng giá"
                                        labelCol={{ span: 24 }}
                                    >
                                        <Row gutter={[10, 10]} style={{ width: "100%" }}>
                                            <Col xl={11} md={24}>
                                                <Form.Item name={["range", 'from']}>
                                                    <InputNumber
                                                        name='from'
                                                        min={0}
                                                        placeholder="đ TỪ"
                                                        formatter={(value) => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
                                                        style={{ width: '100%' }}
                                                    />
                                                </Form.Item>
                                            </Col>
                                            <Col xl={2} md={0}>
                                                <div > - </div>
                                            </Col>
                                            <Col xl={11} md={24}>
                                                <Form.Item name={["range", 'to']}>
                                                    <InputNumber
                                                        name='to'
                                                        min={0}
                                                        placeholder="đ ĐẾN"
                                                        formatter={(value) => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
                                                        style={{ width: '100%' }}
                                                    />
                                                </Form.Item>
                                            </Col>
                                        </Row>
                                        <div>
                                            <Button onClick={() => form.submit()}
                                                style={{ width: "100%" }} type='primary'>Áp dụng</Button>
                                        </div>
                                    </Form.Item>
                                    <Divider />
                                    <Form.Item
                                        label="Đánh giá"
                                        labelCol={{ span: 24 }}
                                    >
                                        <div>
                                            <Rate value={5} disabled style={{ color: '#ffce3d', fontSize: 15 }} />
                                            <span className="ant-rate-text"></span>
                                        </div>
                                        <div>
                                            <Rate value={4} disabled style={{ color: '#ffce3d', fontSize: 15 }} />
                                            <span className="ant-rate-text">trở lên</span>
                                        </div>
                                        <div>
                                            <Rate value={3} disabled style={{ color: '#ffce3d', fontSize: 15 }} />
                                            <span className="ant-rate-text">trở lên</span>
                                        </div>
                                        <div>
                                            <Rate value={2} disabled style={{ color: '#ffce3d', fontSize: 15 }} />
                                            <span className="ant-rate-text">trở lên</span>
                                        </div>
                                        <div>
                                            <Rate value={1} disabled style={{ color: '#ffce3d', fontSize: 15 }} />
                                            <span className="ant-rate-text">trở lên</span>
                                        </div>
                                    </Form.Item>
                                </Form>
                            </div>
                        </Col>

                        <Col md={20} xs={24} >
                            <Spin spinning={isLoading} tip="Loading...">
                                <div style={{ padding: "20px", background: '#fff', borderRadius: 5 }}>
                                    <Row >
                                        <Tabs
                                            defaultActiveKey=""
                                            items={items}
                                            activeKey={sortQuery} //
                                            // onChange={(value) => { setSortQuery(value) }}
                                            onChange={handleTabChange}
                                            style={{ overflowX: "auto" }}
                                        />
                                        <Col xs={24} md={0}>
                                            <div style={{ marginBottom: 20 }} >
                                                <span onClick={() => setShowMobileFilter(true)}>
                                                    <FilterTwoTone />
                                                    <span style={{ fontWeight: 500 }}> Lọc</span>
                                                </span>

                                            </div>
                                        </Col>
                                        <br />
                                    </Row>
                                    <Row className='customize-row'>
                                        {listBook?.map((item, index) => {
                                            return (
                                                <div className="column" key={`book-${index}`} onClick={() => handleRedirectBook(item)}>
                                                    <div className='wrapper'>
                                                        <div className='thumbnail'>
                                                            <img src={`${import.meta.env.VITE_BACKEND_BOOKS_URL}/storage/avartar/${item.thumbnail}`} alt="thumbnail" />
                                                        </div>
                                                        <div className='text' title={item.title}>{item.title}</div>
                                                        <div className='price'>
                                                            {new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(item?.price ?? 0)}
                                                        </div>
                                                        <div className='rating'>
                                                            <Rate value={5} disabled style={{ color: '#ffce3d', fontSize: 10 }} />
                                                            <span>Đã bán {item.sold}</span>
                                                        </div>
                                                    </div>
                                                </div>
                                            )
                                        })}

                                        {/* {listBook.length === 0 &&
                                            <div style={{ width: "100%", margin: "0 auto" }}>
                                                <Empty
                                                    description="Không có dữ liệu"
                                                />
                                            </div>

                                        } */}
                                    </Row>
                                    <div style={{ marginTop: 30 }}></div>
                                    <Row style={{ display: "flex", justifyContent: "center" }}>
                                        <Pagination
                                            current={current}
                                            total={total}
                                            pageSize={pageSize}
                                            responsive
                                            onChange={(p, s) => handleOnchangePage({ current: p, pageSize: s })}
                                        />
                                    </Row>
                                </div>
                            </Spin>
                        </Col>
                    </Row>
                    <MobileFilter
                        isOpen={showMobileFilter}
                        setIsOpen={setShowMobileFilter}
                        handleChangeFilter={handleChangeFilter}
                        listCategory={listCategory}
                        onFinish={onFinish}
                    />
                </div>
            </div>
        </>
    )
}

export default Home;
