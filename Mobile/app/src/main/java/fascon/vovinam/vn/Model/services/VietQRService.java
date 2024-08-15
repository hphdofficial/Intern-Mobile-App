package fascon.vovinam.vn.Model.services;

import fascon.vovinam.vn.Model.PaymentRequest;
import fascon.vovinam.vn.Model.PaymentResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface VietQRService {

    @Headers("Content-Type: application/json")
    @POST("v2/paymentRequests")
    Call<PaymentResponse> createPayment(@Body PaymentRequest paymentRequest);
}
