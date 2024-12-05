import { Col, Divider, Form, Radio, Row, message, notification } from 'antd';
import { DeleteTwoTone, LoadingOutlined } from '@ant-design/icons';
import { useDispatch, useSelector } from 'react-redux';
import { useEffect, useState } from 'react';
import { doDeleteItemCartAction, doPlaceOrderAction } from '../../redux/order/OrderSlice';
import { Input } from 'antd';
import { callCreateOrder } from '../../services/Api';
const { TextArea } = Input;

const Payment = (props) => {
    const carts = useSelector(state => state.order.carts);
    const [totalPrice, setTotalPrice] = useState(0);
    const dispatch = useDispatch();
    const [isSubmit, setIsSubmit] = useState(false);
    const user = useSelector(state => state.account.user);
    const userId = user.userId;
    console.log("User buy: "+userId)

    const [form] = Form.useForm();

   
    useEffect(() => {
        if (carts && carts.length > 0) {
            let sum = 0;
            carts.map(item => {
                sum += item.quantity * item.detail.price;
            })
            setTotalPrice(sum);
        } else {
            setTotalPrice(0);
        }
    }, [carts]);


    const onFinish = async (values) => {
        setIsSubmit(true);
        
        const total = parseFloat(totalPrice);

        const ordersItemsRequests = carts.map(item => {
            return {
                bookId: item.detail.bookId,
                quantity: item.quantity,
                price: parseFloat(item.detail.price),
            };
        });
    
        console.log("Orders Items Requests:", JSON.stringify(ordersItemsRequests, null, 2));
        
        const data = {
            total: total,
            shippingAddress: values.address,
            userId: user.userId,
            ordersItemsRequests: ordersItemsRequests,
        };

        console.log("Data gửi đi:", JSON.stringify(data, null, 2));
    
        const res = await callCreateOrder(data.total, data.shippingAddress, data.userId, data.ordersItemsRequests);
        console.log("Response API: ", res);
        
        if (res && res.data) {
            message.success('Đặt hàng thành công!');
            dispatch(doPlaceOrderAction());
            
            const paymentUrl = res.data.paymentUrl;
            if (paymentUrl) {
                window.location.href = paymentUrl.replace("redirect:", "");
            }
            
            props.setCurrentStep(2);
        } else {
            notification.error({
                message: "Đã có lỗi xảy ra",
                description: res.message,
            });
        }
        setIsSubmit(false);
    };
    
    
    

    return (
        <Row gutter={[20, 20]}>
            <Col md={16} xs={24}>
                {carts?.map((book, index) => {
                    const currentBookPrice = book?.detail?.price ?? 0;
                    return (
                        <div className='order-book' key={`index-${index}`}>
                            <div className='book-content'>
                                <img src={`${import.meta.env.VITE_BACKEND_URL}/images/book/${book?.detail?.thumbnail}`} />
                                <div className='title'>
                                    {book?.detail?.mainText}
                                </div>
                                <div className='price'>
                                    {new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(currentBookPrice)}
                                </div>
                            </div>
                            <div className='action'>
                                <div className='quantity'>
                                    Số lượng: {book?.quantity}
                                </div>
                                <div className='sum'>
                                    Tổng:  {new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(currentBookPrice * (book?.quantity ?? 0))}
                                </div>
                                <DeleteTwoTone
                                    style={{ cursor: "pointer" }}
                                    onClick={() => dispatch(doDeleteItemCartAction({ _id: book._id }))}
                                    twoToneColor="#eb2f96"
                                />

                            </div>
                        </div>
                    )
                })}
            </Col>
            <Col md={8} xs={24} >
                <div className='order-sum'>
                    <Form
                        onFinish={onFinish}
                        form={form}
                    >
                        <Form.Item
                            style={{ margin: 0 }}
                            labelCol={{ span: 24 }}
                            label="Tên người nhận"
                            name="name"
                            initialValue={user?.fullname}
                            rules={[{ required: true, message: 'Tên người nhận không được để trống!' }]}
                        >
                            <Input />
                        </Form.Item>
                        <Form.Item
                            style={{ margin: 0 }}
                            labelCol={{ span: 24 }}
                            label="Số điện thoại"
                            name="phone"
                            initialValue={user?.phone}
                            rules={[{ required: true, message: 'Số điện thoại không được để trống!' }]}
                        >
                            <Input />
                        </Form.Item>
                        <Form.Item
                            style={{ margin: 0 }}
                            labelCol={{ span: 24 }}
                            label="Địa chỉ"
                            name="address"
                            initialValue={user?.address}
                            rules={[{ required: true, message: 'Địa chỉ không được để trống!' }]}
                        >
                            <TextArea
                                autoFocus
                                rows={4}
                            />
                        </Form.Item>
                    </Form>
                    <div className='info'>
                        <div className='method'>
                            <div>  Hình thức thanh toán</div>
                            <Radio checked>Ví điện tử VnPay</Radio>
                        </div>
                    </div>

                    <Divider style={{ margin: "5px 0" }} />
                    <div className='calculate'>
                        <span> Tổng tiền</span>
                        <span className='sum-final'>
                            {new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(totalPrice || 0)}
                        </span>
                    </div>
                    <Divider style={{ margin: "5px 0" }} />
                    <button
                        onClick={() => form.submit()}
                        disabled={isSubmit}
                    >
                        {isSubmit && <span><LoadingOutlined /> &nbsp;</span>}
                        Đặt Hàng ({carts?.length ?? 0})
                    </button>
                </div>
            </Col>
        </Row>
    )
}

export default Payment;
