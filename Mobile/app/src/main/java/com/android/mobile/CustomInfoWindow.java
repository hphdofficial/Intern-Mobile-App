package com.android.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public class CustomInfoWindow extends InfoWindow {
    private final Animation fadeIn;
    private final Animation fadeOut;
    private String idClub;

    public CustomInfoWindow(MapView mapView) {
        super(R.layout.custom_info_window, mapView);
        fadeIn = AnimationUtils.loadAnimation(mapView.getContext(), R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(mapView.getContext(), R.anim.fade_out);
    }

    public CustomInfoWindow(MapView mapView, String idClub) {
        super(R.layout.custom_info_window, mapView);
        fadeIn = AnimationUtils.loadAnimation(mapView.getContext(), R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(mapView.getContext(), R.anim.fade_out);
        this.idClub = idClub;
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

        Button btnDetailClubMaps = mView.findViewById(R.id.btnDetailClubMaps);

        title.setText(marker.getTitle());
        snippet.setText(marker.getSnippet());

        btnDetailClubMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mView.getContext(), DetailClubActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id_club", idClub);
                intent.putExtras(bundle);
                mView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onClose() {
        mView.startAnimation(fadeOut);
    }
}