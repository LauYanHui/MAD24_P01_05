package sg.edu.np.mad.cookbuddy;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TechniqueDetails extends AppCompatActivity {
    private RelativeLayout videoFrameLayout; //get framelayout for video
    private VideoView videoView;
    TextView tvTitle;
    TextView tvPurpose;
    TextView tvDescription;
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
        //find all views by ID
         tvTitle = findViewById(R.id.tvTitle);
         tvPurpose = findViewById(R.id.tvPurpose);
         tvDescription = findViewById(R.id.tvDescription);
        videoView = findViewById(R.id.videoView);
        videoFrameLayout = findViewById(R.id.videoFrameLayout);

        //receive intent from TechniqueRecycler and make changes to text
        Intent receivingTechnique = getIntent();
        tvTitle.setText(receivingTechnique.getStringExtra("title"));
        tvPurpose.setText(receivingTechnique.getStringExtra("purpose"));
        tvDescription.setText(receivingTechnique.getStringExtra("description"));
        String videoPath = receivingTechnique.getStringExtra("videoPath"); //for code below


        //setup videoView
        videoView.setVideoPath(videoPath);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        //play video when everything is prepared
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
            }
        });
    }

    //on landscape, videoView will become fullScreen and resets when user is back to portrait.
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) videoFrameLayout.getLayoutParams();
        ViewGroup.LayoutParams viewParams = videoView.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Set VideoView to fullscreen
            // defines layout height and width when landscape mode
            layoutParams.setMargins(0,0,0,0);

            viewParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoFrameLayout.setGravity(Gravity.CENTER);
            tvTitle.setVisibility(View.GONE);
            tvPurpose.setVisibility(View.GONE);
            tvDescription.setVisibility(View.GONE);


            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Set FrameLayout to original size
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT; // defines a fixed height for portrait mode

            layoutParams.setMargins(0,20,0,0);
            tvTitle.setVisibility(View.VISIBLE);
            tvPurpose.setVisibility(View.VISIBLE);
            tvDescription.setVisibility(View.VISIBLE);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }

        videoFrameLayout.setLayoutParams(layoutParams);
    }
}