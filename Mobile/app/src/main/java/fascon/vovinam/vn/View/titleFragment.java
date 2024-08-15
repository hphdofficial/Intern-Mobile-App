package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class titleFragment extends Fragment {

    private ImageView btn_back;
    LinearLayout sub_menu;
    private TextView title;
    ConstraintLayout main;
    private ImageView img_menu;
    private ImageView btnHome;

    public static titleFragment newInstance() {
        return new titleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_title, container, false);
        rootView.bringToFront();
        title = rootView.findViewById(R.id.txt_title);
        main = rootView.findViewById(R.id.main);
        sub_menu = rootView.findViewById(R.id.sub_menu);
        img_menu = rootView.findViewById(R.id.img_menu);
        btn_back = rootView.findViewById(R.id.btn_back);
        btnHome = rootView.findViewById(R.id.button_home);

        SharedPreferences myContent = getActivity().getSharedPreferences("myContent", Context.MODE_PRIVATE);
        String value = myContent.getString("title", "Vovinam");

        title.setText(value);
        //  CreateFracmentSubMenu();

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StartActivity.class);
                startActivity(intent);
            }
        });

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartView();
            }
        });
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                sub_menu fragmentB = (sub_menu) fragmentManager.findFragmentByTag("FRAGMENT_B_TAG");
                if (fragmentB != null) {
                    fragmentTransaction.remove(fragmentB);
                    fragmentB = new sub_menu();
                    fragmentTransaction.add(R.id.fragment_container, fragmentB, "FRAGMENT_B_TAG");
                } else {
                    fragmentB = new sub_menu();
                    fragmentTransaction.add(R.id.fragment_container, fragmentB, "FRAGMENT_B_TAG");
                }

                fragmentTransaction.commit();
            }
        });
        String menu = getActivity().toString();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!menu.contains("MenuActivity")) {
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "Không thể quay về", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return rootView;

    }

    public void restartView() {
        sub_menu.setVisibility(View.GONE);
    }

    public void CreateFracmentSubMenu() {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new sub_menu())
                .commit();
        sub_menu.setVisibility(View.GONE);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

    @Override
    public void onResume() {
        super.onResume();
        restartView();
    }
}