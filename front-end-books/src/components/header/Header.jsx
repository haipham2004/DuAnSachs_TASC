import React, { useState } from 'react';
import { FaReact } from 'react-icons/fa';
import { FiShoppingCart } from 'react-icons/fi';
import { IoBook } from "react-icons/io5";
import { Divider, Badge, Drawer, message, Avatar, Popover, Empty } from 'antd';
import { useDispatch, useSelector } from 'react-redux';
import { DownOutlined } from '@ant-design/icons';
import { Dropdown, Space } from 'antd';
import { useNavigate } from 'react-router';
import { doLogoutAction } from '../../redux/account/AccountSlice';
import { Link } from 'react-router-dom';
import './HeaderCss.scss';
import { callLogout } from '../../services/Api';

const Header = (props) => {
    const [openDrawer, setOpenDrawer] = useState(false);
    const isAuthenticated = useSelector(state => state.account.isAuthenticated);
    const user = useSelector(state => state.account.user);
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const [showManageAccount, setShowManageAccount] = useState(false);
    const carts = useSelector(state => state.order.carts);


    const handleLogout = async () => {
        const res = await callLogout();
        if (res.message) {
            dispatch(doLogoutAction());
            message.success('Đăng xuất thành công');
            navigate('/')
        }
    }

    // Menu items for authenticated user
    const items = [
        {
            label: <label style={{ cursor: 'pointer' }} onClick={() => setShowManageAccount(true)}>Quản lý tài khoản</label>,
            key: 'account',
        },
        {
            label: <Link to="/history">Lịch sử mua hàng</Link>,
            key: 'history',
        },
        {
            label: <label style={{ cursor: 'pointer' }} onClick={() => handleLogout()}>Đăng xuất</label>,
            key: 'logout',
        },
    ];

    if (user?.roles === 'ROLE_ADMIN') {
        items.unshift({
            label: <Link to='/admin'>Trang quản trị</Link>,
            key: 'admin',
        });
    }

    // Content of cart popover
    const contentPopover = () => {
        return (
            <div className='pop-cart-body'>
                <div className='pop-cart-content'>
                    {carts?.map((book, index) => {
                        return (
                            <div className='book' key={`book-${index}`}>
                                <img
                                    // style={{ width: '400px', height: '200px', objectFit: 'cover' }}
                                    src={`${import.meta.env.VITE_BACKEND_BOOKS_URL}/storage/avartar/${book?.detail?.thumbnail}`}
                                />

                                <div>{book?.detail?.title}</div>
                                <div className='price'>
                                    {new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(book?.detail?.price ?? 0)}
                                </div>
                            </div>
                        )
                    })}
                </div>
                {carts.length > 0 ?
                    <div className='pop-cart-footer'>
                        <button onClick={() => navigate('/order')}>Xem giỏ hàng</button>
                    </div>
                    :
                    <Empty
                        description="Không có sản phẩm trong giỏ hàng"
                    />
                }
            </div>
        )
    }


    return (
        <>
            <div className='header-container'>
                <header className="page-header">
                    <div className="page-header__top">
                        <div className="page-header__toggle" onClick={() => setOpenDrawer(true)}>☰</div>
                        <div className='page-header__logo'>
                            <span className='logo'>
                                <span onClick={() => navigate('/')}>
                                    <FaReact className='rotate icon-react' />phamngochai3010
                                </span>
                                <IoBook className='icon-search' />
                            </span>
                            <input
                                className="input-search"
                                type="text"
                                placeholder="Bạn tìm gì hôm nay"
                                value={props.searchTerm}
                                onChange={(e) => props.setSearchTerm(e.target.value)}
                            />
                        </div>
                    </div>

                    <nav className="page-header__bottom">
                        <ul id="navigation" className="navigation">
                            <li className="navigation__item">
                            <Popover
                                    className="popover-carts"
                                    placement="topRight"
                                    rootClassName="popover-carts"
                                    title={"Sản phẩm mới thêm"}
                                    content={contentPopover}
                                    arrow={true}>
                                    <Badge
                                        count={carts?.length ?? 0}
                                        size={"small"}
                                        showZero
                                    >
                                        <FiShoppingCart className='icon-cart' />
                                    </Badge>
                                </Popover>

                            </li>

                            {/* Divider for mobile */}
                            <li className="navigation__item mobile"><Divider type='vertical' /></li>

                            <li className="navigation__item mobile">
                                {!isAuthenticated ?
                                   
                                    <span onClick={() => navigate('/login')}>Tài Khoản</span> :
                                    <Dropdown menu={{ items }} trigger={['click']}>
                                        <Space>
                                            {user?.username} <DownOutlined />
                                        </Space>
                                    </Dropdown>

                                }
                            </li>
                        </ul>
                    </nav>
                </header>
            </div>

            {/* Drawer Menu */}
            <Drawer
                title="Menu chức năng"
                placement="left"
                onClose={() => setOpenDrawer(false)}
                open={openDrawer}
            >
                <p onClick={() => setShowManageAccount(true)}>Quản lý tài khoản</p>
                <Divider />
                <p onClick={() => handleLogout()}>Đăng xuất</p>
                <Divider />
            </Drawer>
        </>
    );
};

export default Header;
