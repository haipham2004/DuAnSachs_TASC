import { Badge, Descriptions, Drawer } from "antd";
const UserViewDetail = (props) => {
    const { openViewDetail, setOpenViewDetail, dataViewDetail, setDataViewDetail } = props;

    const onClose = () => {
        setOpenViewDetail(false);
        setDataViewDetail(null);
    }
    return (
        <>
            <Drawer
                title="Chức năng xem chi tiết"
                width={"50vw"}
                onClose={onClose}
                open={openViewDetail}
            >
                <Descriptions
                    title="Thông tin user"
                    bordered
                    column={2}
                >
                    <Descriptions.Item label="Id">{dataViewDetail?.userId}</Descriptions.Item>
                    <Descriptions.Item label="Tên hiển thị">{dataViewDetail?.username}</Descriptions.Item>
                    <Descriptions.Item label="Họ và tên">{dataViewDetail?.fullName}</Descriptions.Item>
                    <Descriptions.Item label="Email">{dataViewDetail?.email}</Descriptions.Item>
                    <Descriptions.Item label="Số điện thoại">{dataViewDetail?.phone}</Descriptions.Item>
                    <Descriptions.Item label="Địa chỉ">{dataViewDetail?.addRess}</Descriptions.Item>
                    <Descriptions.Item label="Role" span={2}>
                        <Badge status="processing" text={dataViewDetail?.enumRolesName} />
                    </Descriptions.Item>
                
                </Descriptions>
            </Drawer>
        </>
    )
}
export default UserViewDetail;
