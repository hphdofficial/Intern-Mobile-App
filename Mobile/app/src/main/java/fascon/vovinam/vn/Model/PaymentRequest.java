package fascon.vovinam.vn.Model;

public class PaymentRequest {
    private String amount;
    private String orderId;
    private String orderInfo;
    private String ipAddr;

    public PaymentRequest(String amount, String orderId, String orderInfo, String ipAddr) {
        this.amount = amount;
        this.orderId = orderId;
        this.orderInfo = orderInfo;
        this.ipAddr = ipAddr;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }
}
