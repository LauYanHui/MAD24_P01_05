package sg.edu.np.mad.myapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TechniqueDetails extends AppCompatActivity {
    private FrameLayout videoFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_technique_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvPurpose = findViewById(R.id.tvPurpose);
        TextView tvDescription = findViewById(R.id.tvDescription);
        ImageView backIconIV = findViewById(R.id.backIconIV);
        VideoView videoView = findViewById(R.id.videoView);
        videoFrameLayout = findViewById(R.id.videoFrameLayout);
        //receive intent from CulinaryRecyclerActivity and make changes to text
        Intent receivingTechnique = getIntent();
        tvTitle.setText(receivingTechnique.getStringExtra("title"));
        tvPurpose.setText(receivingTechnique.getStringExtra("purpose"));
        tvDescription.setText(receivingTechnique.getStringExtra("description"));
        String videoPath = receivingTechnique.getStringExtra("videoPath");
        //make intent to go back to CulinaryRecyclerActivity
        backIconIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent techniqueRecycler = new Intent(TechniqueDetails.this, TechniqueRecycler.class);
                startActivity(techniqueRecycler);
            }
        });
        videoView.setVideoPath(videoPath);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ViewGroup.LayoutParams layoutParams = videoFrameLayout.getLayoutParams();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Set VideoView to fullscreen
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Set VideoView to original size
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = (int) getResources().getDimension(R.dimen.video_height); // Define a fixed height for portrait mode
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }

        videoFrameLayout.setLayoutParams(layoutParams);
    }
}