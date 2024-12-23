CREATE DATABASE DB_DuAnSach;

USE DB_DuAnSach;

CREATE TABLE note (
    id int AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(255), -- Lưu trữ nội dung lớn
    owner_username VARCHAR(255)
);


CREATE TABLE roles (
    role_id int AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
	created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at BIT DEFAULT 0
);


CREATE TABLE users (
    user_id int AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(120) NOT NULL,
    account_non_locked BOOLEAN DEFAULT TRUE,
    account_non_expired BOOLEAN DEFAULT TRUE,
    credentials_non_expired BOOLEAN DEFAULT TRUE,
    enabled BOOLEAN DEFAULT TRUE,
    credentials_expiry_date DATE,
    account_expiry_date DATE,
    two_factor_secret VARCHAR(255),
    is_two_factor_enabled BOOLEAN DEFAULT FALSE,
    sign_up_method VARCHAR(50),
    role_id int,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE SET NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at BIT DEFAULT 0
);


CREATE TABLE authors (
    author_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE,
    bio TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at BIT DEFAULT 0
);

CREATE TABLE publishers (
    publisher_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE,
    address VARCHAR(255),
    phone VARCHAR(20),
    email VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at BIT DEFAULT 0
);

CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at BIT DEFAULT 0
);

CREATE TABLE books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    author_id INT,
    publisher_id INT,
    category_id INT,
    price DECIMAL(10, 2),
    description TEXT,
    stock INT DEFAULT 0,  -- Giữ lại trường stock để quản lý số lượng
    quantity INT DEFAULT 0,  -- Thêm trường quantity
    status VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at BIT DEFAULT 0,
    FOREIGN KEY (author_id) REFERENCES authors(author_id),
    FOREIGN KEY (publisher_id) REFERENCES publishers(publisher_id),
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

CREATE TABLE books_images (
    id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT,
    constraint fk_books_images_books foreign key(book_id) references books(book_id) ON DELETE cascade,
    image_url VARCHAR(255),
    created_at DATETIME, 
    updated_at DATETIME,
    deleted_at BIT DEFAULT 0
);

CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10, 2),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at BIT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    book_id INT,
    quantity INT,
    price DECIMAL(10, 2),
    status VARCHAR(10) DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at BIT DEFAULT 0,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id)
);

CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(10, 2),
    payment_method VARCHAR(50),
    status VARCHAR(10) DEFAULT 'SUCCESS',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at BIT DEFAULT 0,
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

CREATE TABLE carts (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at BIT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE cart_items (
    cart_item_id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT,
    book_id INT,
    quantity INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at BIT DEFAULT 0,
    FOREIGN KEY (cart_id) REFERENCES carts(cart_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id)
);

CREATE TABLE transaction_history (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    user_id INT,
    transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(10, 2),
    payment_method VARCHAR(50),
    status VARCHAR(10) DEFAULT 'SUCCESS',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at BIT DEFAULT 0,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);


-- Chèn dữ liệu vào bảng roles
INSERT INTO roles (name, description) VALUES 
('ROLE_ADMIN', 'Quản trị viên'),
('ROLE_USER', 'Người dùng bình thường');


-- Chèn dữ liệu vào bảng users
INSERT INTO users (username, email, password, role_id) VALUES 
('phamngochai', 'phamngochai3010yka@gmail.com', 'password123', 1),
('nguyenhang', 'nguyenhang2502@gmail.com', 'password123', 1),
('nguyenkhanh', 'nguyenkhanh35@gmail.com', 'password123', 1),
('kimanh', 'anddk@gmail.com', 'password123', 2),
('user5', 'user5@example.com', 'password123', 2),
('admin1', 'admin1@example.com', 'password123', 2),
('admin2', 'admin2@example.com', 'password123', 2),
('admin3', 'admin3@example.com', 'password123', 2),
('admin4', 'admin4@example.com', 'password123', 2),
('admin5', 'admin5@example.com', 'password123', 2);

-- Chèn dữ liệu vào bảng authors
INSERT INTO authors (name, bio) VALUES 
('Author One', 'Bio for author one.'),
('Author Two', 'Bio for author two.'),
('Author Three', 'Bio for author three.'),
('Author Four', 'Bio for author four.'),
('Author Five', 'Bio for author five.'),
('Author Six', 'Bio for author six.'),
('Author Seven', 'Bio for author seven.'),
('Author Eight', 'Bio for author eight.'),
('Author Nine', 'Bio for author nine.'),
('Author Ten', 'Bio for author ten.');

-- Chèn dữ liệu vào bảng publishers
INSERT INTO publishers (name, address, phone, email) VALUES 
('Publisher One', 'Address 1', '123456789', 'publisher1@example.com'),
('Publisher Two', 'Address 2', '123456789', 'publisher2@example.com'),
('Publisher Three', 'Address 3', '123456789', 'publisher3@example.com'),
('Publisher Four', 'Address 4', '123456789', 'publisher4@example.com'),
('Publisher Five', 'Address 5', '123456789', 'publisher5@example.com'),
('Publisher Six', 'Address 6', '123456789', 'publisher6@example.com'),
('Publisher Seven', 'Address 7', '123456789', 'publisher7@example.com'),
('Publisher Eight', 'Address 8', '123456789', 'publisher8@example.com'),
('Publisher Nine', 'Address 9', '123456789', 'publisher9@example.com'),
('Publisher Ten', 'Address 10', '123456789', 'publisher10@example.com');

-- Chèn dữ liệu vào bảng categories
INSERT INTO categories (name) VALUES 
('Fiction'),
('Non-Fiction'),
('Science Fiction'),
('Biography'),
('Mystery'),
('Romance'),
('Horror'),
('Fantasy'),
('History'),
('Self-Help');

-- Chèn dữ liệu vào bảng books
INSERT INTO books (title, author_id, publisher_id, category_id, price, description, stock, quantity, status) VALUES 
('Book One', 1, 1, 1, 19.99, 'Description for book one.', 10, 5, 'Available'),
('Book Two', 2, 2, 2, 29.99, 'Description for book two.', 5, 3, 'Available'),
('Book Three', 3, 3, 3, 39.99, 'Description for book three.', 2, 1, 'Available'),
('Book Four', 4, 4, 4, 49.99, 'Description for book four.', 0, 0, 'Out of Stock'),
('Book Five', 5, 5, 5, 15.99, 'Description for book five.', 20, 10, 'Available'),
('Book Six', 6, 6, 6, 25.99, 'Description for book six.', 15, 7, 'Available'),
('Book Seven', 7, 7, 7, 35.99, 'Description for book seven.', 12, 6, 'Available'),
('Book Eight', 8, 8, 8, 45.99, 'Description for book eight.', 8, 4, 'Available'),
('Book Nine', 9, 9, 9, 55.99, 'Description for book nine.', 3, 2, 'Available'),
('Book Ten', 10, 10, 10, 65.99, 'Description for book ten.', 0, 0, 'Out of Stock');

-- Chèn dữ liệu vào bảng books_images
INSERT INTO books_images (book_id, image_url) VALUES 
(1, 'http://example.com/image1.jpg'),
(1, 'http://example.com/image1_2.jpg'),
(2, 'http://example.com/image2.jpg'),
(2, 'http://example.com/image2_2.jpg'),
(3, 'http://example.com/image3.jpg'),
(4, 'http://example.com/image4.jpg'),
(5, 'http://example.com/image5.jpg'),
(6, 'http://example.com/image6.jpg'),
(7, 'http://example.com/image7.jpg'),
(8, 'http://example.com/image8.jpg');

-- Chèn dữ liệu vào bảng orders
INSERT INTO orders (user_id, total) VALUES 
(1, 100.00),
(2, 200.00),
(3, 300.00),
(4, 400.00),
(5, 500.00),
(6, 600.00),
(7, 700.00),
(8, 800.00),
(9, 900.00),
(10, 1000.00);

-- Chèn dữ liệu vào bảng order_items
INSERT INTO order_items (order_id, book_id, quantity, price) VALUES 
(1, 1, 2, 19.99),
(1, 2, 1, 29.99),
(2, 3, 3, 39.99),
(2, 4, 1, 49.99),
(3, 5, 5, 15.99),
(3, 6, 2, 25.99),
(4, 7, 1, 35.99),
(5, 8, 1, 45.99),
(6, 9, 1, 55.99),
(7, 10, 2, 65.99);

-- Chèn dữ liệu vào bảng payments
INSERT INTO payments (order_id, amount, payment_method) VALUES 
(1, 100.00, 'Credit Card'),
(2, 200.00, 'PayPal'),
(3, 300.00, 'Bank Transfer'),
(4, 400.00, 'Credit Card'),
(5, 500.00, 'PayPal'),
(6, 600.00, 'Bank Transfer'),
(7, 700.00, 'Credit Card'),
(8, 800.00, 'PayPal'),
(9, 900.00, 'Bank Transfer'),
(10, 1000.00, 'Credit Card');

-- Chèn dữ liệu vào bảng carts
INSERT INTO carts (user_id) VALUES 
(1),
(2),
(3),
(4),
(5),
(6),
(7),
(8),
(9),
(10);

-- Chèn dữ liệu vào bảng cart_items
INSERT INTO cart_items (cart_id, book_id, quantity) VALUES 
(1, 1, 1),
(1, 2, 1),
(2, 3, 1),
(2, 4, 1),
(3, 5, 1),
(4, 6, 1),
(5, 7, 1),
(6, 8, 1),
(7, 9, 1),
(8, 10, 1);

-- Chèn dữ liệu vào bảng transaction_history
INSERT INTO transaction_history (order_id, user_id, amount, payment_method) VALUES 
(1, 1, 100.00, 'Credit Card'),
(2, 2, 200.00, 'PayPal'),
(3, 3, 300.00, 'Bank Transfer'),
(4, 4, 400.00, 'Credit Card'),
(5, 5, 500.00, 'PayPal'),
(6, 6, 600.00, 'Bank Transfer'),
(7, 7, 700.00, 'Credit Card'),
(8, 8, 800.00, 'PayPal'),
(9, 9, 900.00, 'Bank Transfer'),
(10, 10, 1000.00, 'Credit Card');




