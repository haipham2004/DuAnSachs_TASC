import {
    HomeOutlined,
    UserOutlined,
    ShopOutlined,
    HeartOutlined,
    ShoppingCartOutlined,
    SearchOutlined,
} from '@ant-design/icons';
import './HeaderCss.css';


const Header = () => {
    return (
        <>
            <div className="banner">
                <div className="logo">
                    <div className="icon">
                        <HeartOutlined style={{ fontSize: '32px', color: '#ff4d4d' }} />
                    </div>
                    <div className="text">phamngochai3010yka</div>
                </div>
                <div className="nav">
                    <ul>
                        <li>
                            <a href="/">
                                <HomeOutlined />
                                <span>Home</span>
                            </a>
                        </li>
                        <li>
                            <a href="/about">
                                <span>About</span>
                            </a>
                        </li>
                        <li>
                            <a href="/shop">
                                <ShopOutlined />
                                <span>Shop</span>
                            </a>
                        </li>
                        <li>
                            <a href="/blogs">
                                <span>Blogs</span>
                            </a>
                        </li>
                        <li>
                            <a href="/pages">
                                <span>Pages</span>
                            </a>
                        </li>
                        <li>
                            <a href="/contact">
                                <span>Contact</span>
                            </a>
                        </li>
                        <li>
                            <a href="login">
                                <span>Contact</span>
                            </a>
                        </li>
                    </ul>
                </div>
                <div className="actions">
                    <SearchOutlined />
                    <UserOutlined />
                    <HeartOutlined />
                    <ShoppingCartOutlined />
                    <span>(02)</span>
                </div>
            </div>
        </>
    )
}

export default Header