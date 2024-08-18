package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentAfterAddCart extends Fragment {
    private String languageS;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_after_add_product, container, false);
        SharedPreferences shared = getContext().getSharedPreferences("login_prefs", getContext().MODE_PRIVATE);
        languageS = shared.getString("language",null);

        Button buttonClose = view.findViewById(R.id.button_close);
        Button buttonShopping = view.findViewById(R.id.button_to_shopping);
        Button buttonCart = view.findViewById(R.id.button_to_cart);
        TextView textView14 = view.findViewById(R.id.textView14);
        if(languageS!= null){
            if(languageS.contains("en")){
                buttonClose.setText("Close");
                buttonShopping.setText("Continue shopping");
                buttonCart.setText("Go to cart");
                textView14.setText("Added to cart successfully");
            }
        }

        buttonShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(FragmentAfterAddCart.this).commit();
                Intent intent = new Intent(getActivity(), activity_items.class);
                startActivity(intent);
            }
        });

        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(FragmentAfterAddCart.this).commit();
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
            }
        });

        // Handle close button click
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(FragmentAfterAddCart.this).commit();
            }
        });

        return view;

    }

}