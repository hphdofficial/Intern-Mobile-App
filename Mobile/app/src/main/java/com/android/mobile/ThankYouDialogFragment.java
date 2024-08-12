package com.android.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ThankYouDialogFragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_thank_you, container, false);

        Button buttonViewHistory = view.findViewById(R.id.buttonViewHistory);
        Button buttonGoHome = view.findViewById(R.id.buttonGoHome);

        buttonViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OrderActivity.class);
            startActivity(intent);
            dismiss();
        });

        buttonGoHome.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StartActivity.class);
            startActivity(intent);
            dismiss();
        });

        return view;
    }
}