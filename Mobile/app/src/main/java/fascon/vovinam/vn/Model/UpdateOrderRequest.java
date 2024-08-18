package fascon.vovinam.vn.Model;

import java.util.List;

public class UpdateOrderRequest {
    private String txn_ref;
    private List<ProductModel> products;

    public UpdateOrderRequest(String txn_ref, List<ProductModel> products) {
        this.txn_ref = txn_ref;
        this.products = products;
    }

    public String getTxn_ref() {
        return txn_ref;
    }

    public void setTxn_ref(String txn_ref) {
        this.txn_ref = txn_ref;
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }
}