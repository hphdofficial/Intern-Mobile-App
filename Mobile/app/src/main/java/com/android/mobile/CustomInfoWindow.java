package com.android.mobile;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public class CustomInfoWindow extends InfoWindow {
    private final Animation fadeIn;
    private final Animation fadeOut;

    public CustomInfoWindow(MapView mapView) {
        super(R.layout.custom_info_window, mapView);
        fadeIn = AnimationUtils.loadAnimation(mapView.getContext(), R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(mapView.getContext(), R.anim.fade_out);
    }

    @Override
    public void onOpen(Object item) {
        Marker marker = (Marker) item;
        TextView title = mView.findViewById(R.id.title);
        TextView snippet = mView.findViewById(R.id.snippet);

        title.setText(marker.getTitle());
        snippet.setText(marker.getSnippet());

        LinearLayout layout = mView.findViewById(R.id.info_window_layout);
        int screenWidth = mView.getContext().getResources().getDisplayMetrics().widthPixels;
        layout.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 2, LinearLayout.LayoutParams.WRAP_CONTENT));

        title.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 2, LinearLayout.LayoutParams.WRAP_CONTENT));
        snippet.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 2, LinearLayout.LayoutParams.WRAP_CONTENT));

        mView.startAnimation(fadeIn);
        mView.setOnClickListener(v -> marker.closeInfoWindow());
    }

    @Override
    public void onClose() {
        mView.startAnimation(fadeOut);
    }
}