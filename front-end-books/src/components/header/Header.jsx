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

const Header = (props) => {
    const [openDrawer, setOpenDrawer] = useState(false);
    const isAuthenticated = useSelector(state => state.account.isAuthenticated);
    const user = useSelector(state => state.account.user);
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const [showManageAccount, setShowManageAccount] = useState(false);

    const handleLogout = async () => {
        // Nếu cần gọi API logout, thì bỏ dòng comment dưới
        const res = await callLogout();
        if (res && res.statusCode && res.message) {
            dispatch(doLogoutAction());
            message.success('Đăng xuất thành công');
            navigate('/');
        }
    };

    let items = [
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

    if (user?.role === 'ADMIN') {
        items.unshift({
            label: <Link to='/admin'>Trang quản trị</Link>,
            key: 'admin',
        });
    }

    // const urlAvatar = `${import.meta.env.VITE_BACKEND_URL}/images/avatar/${user?.avatar}`;

    const contentPopover = () => {
        return (
            <div className='pop-cart-body'>
                <div className='pop-cart-content'>
                    {/* Giỏ hàng trống */}
                </div>
                <Empty description="Không có sản phẩm trong giỏ hàng" />
            </div>
        );
    };

    return (
        <>
            <div className='header-container'>
                <header className="page-header">
                    <div className="page-header__top">
                        <div className="page-header__toggle" onClick={() => setOpenDrawer(true)}>☰</div>
                        <div className='page-header__logo'>
                            <span className='logo'>
                                <span onClick={() => navigate('/')}> <FaReact className='rotate icon-react' />phamngochai3010</span>
                                <IoBook className='icon-search' />
                            </span>
                            
                            <input
                                className="input-search" type={'text'}
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
                                    <Badge size="small" showZero>
                                        <FiShoppingCart className='icon-cart' />
                                    </Badge>
                                </Popover>
                            </li>
                            <li className="navigation__item mobile"><Divider type='vertical' /></li>
                            <li className="navigation__item mobile">
                                {!isAuthenticated ? 
                                    <span onClick={() => navigate('/login')}>Tài Khoản</span> 
                                    : 
                                    <Dropdown menu={{ items }} trigger={['click']}>
                                        <Space>
                                            {/* <Avatar src={urlAvatar} /> */}
                                            {user?.fullName}
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
                <p>Quản lý tài khoản</p>
                <Divider />
                <p>Đăng xuất</p>
                <Divider />
            </Drawer>
        </>
    );
};

export default Header;
