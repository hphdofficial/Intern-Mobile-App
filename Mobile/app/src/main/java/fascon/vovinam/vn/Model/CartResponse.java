package fascon.vovinam.vn.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// CartResponse.java
public class CartResponse {
    @SerializedName("cart")
    private List<CartModel> cart;

    // Getter
    public List<CartModel> getCart() {
        return cart;
    }
}