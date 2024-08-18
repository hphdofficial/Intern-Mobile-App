package fascon.vovinam.vn.Model.services;

import fascon.vovinam.vn.Model.AttendanceRequest;
import fascon.vovinam.vn.Model.ClassModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CheckinApiService {
    @GET("api/user/classes")
    Call<ClassModel[]> memberViewClass(@Header("Authorization") String token);

    @GET("api/member/view-checkin")
    Call<JsonObject> memberViewCheckin(@Header("Authorization") String token,
                                               @Query("start_date") String start,
                                               @Query("end_date") String end);

    @GET("api/teacher/view-checkin")
    Call<JsonObject> teacherViewCheckin(@Header("Authorization") String token,
                                                @Query("start_date") String start,
                                                @Query("end_date") String end,
                                        @Query("id_class") int id);

    @POST("api/teacher/checkin")
    Call<Void> teacherCheckin(@Header("Authorization") String token,
                              @Body AttendanceRequest request);

    @GET("api/teacher/classes/getall")
    Call<JsonObject> getTeacherClasses(@Header("Authorization") String token);

    @GET("api/teacher/classes/getdetail")
    Call<JsonObject> getClassMembers(@Header("Authorization") String token,
                                     @Query("id_class") int idClass);

    @POST("api/teacher/view-checkin-by-id")
    Call<JsonObject> teacherViewCheckinById(@Header("Authorization") String token,
                                        @Query("start_date") String start,
                                        @Query("end_date") String end,
                                        @Query("id_class") int id,
                                            @Query("member_id") int idMember);

    @POST("api/teacher/view-checkin-by-name")
    Call<JsonObject> teacherViewCheckinByName(@Header("Authorization") String token,
                                        @Query("start_date") String start,
                                        @Query("end_date") String end,
                                        @Query("id_class") int id,
                                              @Query("member_name") String name);

    @POST("api/teacher/view-checkin-by-phone")
    Call<JsonObject> teacherViewCheckinByPhone(@Header("Authorization") String token,
                                        @Query("start_date") String start,
                                        @Query("end_date") String end,
                                        @Query("id_class") int id,
                                               @Query("member_phone") String sdt);
}
