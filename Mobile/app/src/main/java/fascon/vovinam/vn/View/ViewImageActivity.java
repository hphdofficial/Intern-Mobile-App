package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.os.Bundle;
import android.widget.ImageView;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import com.squareup.picasso.Picasso;

public class ViewImageActivity extends BaseActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        imageView = findViewById(R.id.imageView);

        boolean useDefaultImage = getIntent().getBooleanExtra("defaultImage", false);
        if (useDefaultImage) {
            imageView.setImageResource(R.drawable.photo3x4);
        } else {
            String imageUriString = getIntent().getStringExtra("imageUri");
            if (imageUriString != null) {
                Picasso.get().load(imageUriString).into(imageView);
            }
        }
    }
}
