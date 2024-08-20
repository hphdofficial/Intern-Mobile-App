package fascon.vovinam.vn.View;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fascon.vovinam.vn.Model.Class;
import fascon.vovinam.vn.Model.Club;
import fascon.vovinam.vn.Model.ProductModel;
import fascon.vovinam.vn.Model.ReponseModel;
import fascon.vovinam.vn.Model.UpdateOrderRequest;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.ClassApiService;
import fascon.vovinam.vn.Model.services.OnItemClickListener;
import fascon.vovinam.vn.Model.services.OrderApiService;
import fascon.vovinam.vn.R;
import fascon.vovinam.vn.ViewModel.CartAdapter;
import fascon.vovinam.vn.ViewModel.ClassAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeClassFragment extends DialogFragment implements OnItemClickListener {
    private RecyclerView recyclerView;
    private ClassAdapter adapter;
    private List<Class> classList = new ArrayList<>();
    private int memberId = 0;
    private int classId = 0;
    private int newClassId = 0;
    private Button btnClose;
    private Button btnUpdate;
    private TextView txtNotify;
    private String languageS;

    public ChangeClassFragment(int memberId, int classId) {
        this.memberId = memberId;
        this.classId = classId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_class, container, false);

        setCancelable(false);

        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new ClassAdapter(getContext(), classList, true, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        SharedPreferences shared = getActivity().getSharedPreferences("login_prefs", getContext().MODE_PRIVATE);
        languageS = shared.getString("language", null);

        btnClose = view.findViewById(R.id.button_close);
        btnUpdate = view.findViewById(R.id.button_update);

        if (languageS != null) {
            if (languageS.contains("en")) {
                btnClose.setText("Close");
                btnUpdate.setText("Update");
            }
        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newClassId != 0) {
                    updateClass();
                } else {
                    if (languageS != null) {
                        if (languageS.contains("en")) {
                            Toast.makeText(getContext(), "Please select the class to transfer", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Vui lòng chọn lớp cần chuyển", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        txtNotify = view.findViewById(R.id.txt_notify);

        getAllClass();

        return view;
    }

    public void getAllClass() {
        if (languageS != null) {
            if (languageS.contains("en")) {
                txtNotify.setText("Loading...");
            }
        }
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_prefs", getContext().MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<JsonObject> call = service.getCoachClass("Bearer " + accessToken, languageS.equals("vn") ? "vi" : "en");

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    Gson gson = new Gson();
                    Type classListType = new TypeToken<List<Class>>() {
                    }.getType();
                    classList = gson.fromJson(jsonObject.get("data"), classListType);


                    if (classList != null) {
                        for (Iterator<Class> iterator = classList.iterator(); iterator.hasNext();) {
                            int id = iterator.next().getId();
                            if (id == classId) {
                                iterator.remove();
                                break;
                            }
                        }
                        txtNotify.setText("");
                    }
                    else {
                        if (languageS != null) {
                            if (languageS.contains("en")) {
                                txtNotify.setText("No other classes");
                            } else {
                                txtNotify.setText("Không có lớp học khác");
                            }
                        }
                    }

                    adapter.setData(classList);
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.e("Fail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void updateClass() {
        btnUpdate.setEnabled(false);
        btnClose.setEnabled(false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_prefs", getContext().MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<ReponseModel> call = service.changeJoinClass("Bearer " + accessToken, memberId, classId, newClassId);

        if (languageS != null) {
            if (languageS.contains("en")) {
                Toast.makeText(getContext(), "Updating...", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Đang cập nhật...", Toast.LENGTH_LONG).show();
            }
        }

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                if (response.isSuccessful()) {
                    btnUpdate.setEnabled(true);
                    btnClose.setEnabled(true);

                    if (languageS != null) {
                        if (languageS.contains("en")) {
                            Toast.makeText(getContext(), "Update class transfer successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Cập nhật chuyển lớp thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (getContext() instanceof ApproveActivity) {
                        ((ApproveActivity) getContext()).selectSpinnerItem(0);
                    }
                    dismiss();
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReponseModel> call, @NonNull Throwable t) {
                btnUpdate.setEnabled(true);
                btnClose.setEnabled(true);
                Log.e("Fail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    @Override
    public void onItemClick(int position, int classId) {
        this.newClassId = classId;
//        Toast.makeText(getContext(), "Item clicked at position: " + position + ", idClass: " + classId, Toast.LENGTH_SHORT).show();
    }
}