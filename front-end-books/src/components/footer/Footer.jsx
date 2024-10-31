import './FooterCss.css';

const Footer = () => {
    return (
            <div className="container">
                <div className="row">
                    <div className="col-md-4">
                        <h5>Về Chúng Tôi</h5>
                        <p>Cửa hàng sách XYZ cung cấp những cuốn sách hay nhất cho mọi lứa tuổi.</p>
                    </div>
                    <div className="col-md-4">
                        <h5>Liên Hệ</h5>
                        <ul style={{ listStyleType: 'none', padding: 0 }}>
                            <li>Email: phamngochai3010yka@gmail.com</li>
                            <li>Điện thoại: +84 334 294 889</li>
                            <li>Địa chỉ: 10 Trương Công Giai, Cầu Giấy, Hà Nội</li>
                        </ul>
                    </div>
                    <div className="col-md-4">
                        <h5>Kết Nối Với Chúng Tôi</h5>
                        <a href="#" style={{ margin: '0 10px' }}>Facebook</a>
                        <a href="#" style={{ margin: '0 10px' }}>Instagram</a>
                        <a href="#" style={{ margin: '0 10px' }}>Twitter</a>
                    </div>
                </div>
                <hr />
                <p>&copy; 2024 Cửa hàng sách XYZ. Bản quyền thuộc về chúng tôi.</p>
            </div>
    );
}


export default Footer