package sg.edu.np.mad.cookbuddy.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Gravity;
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

import org.w3c.dom.Text;

import sg.edu.np.mad.cookbuddy.R;

public class TechniqueDetailsActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvPurpose;
    TextView tvDescription;
    VideoView videoView;
    RelativeLayout videoFrame;
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
        videoFrame = findViewById(R.id.videoFrame);

        //receive intent from TechniqueRecycler and make changes to text
        Intent receivingTechnique = getIntent();
        tvTitle.setText(receivingTechnique.getStringExtra("title"));
        tvPurpose.setText(receivingTechnique.getStringExtra("purpose"));
        tvDescription.setText(receivingTechnique.getStringExtra("description"));
        String videoPath = receivingTechnique.getStringExtra("videoPath"); //for code below

        // setup videoView
        videoView.setVideoPath(videoPath);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

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
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) videoFrame.getLayoutParams();
        ViewGroup.LayoutParams viewParams = videoView.getLayoutParams();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewParams.height = ViewGroup.LayoutParams.MATCH_PARENT; // fullscreen
            layoutParams.setMargins(0,0,0,0); // remove margin in landscape
            videoFrame.setGravity(Gravity.CENTER); // center video in frame

            // hide other widgets
            tvTitle.setVisibility(View.GONE);
            tvPurpose.setVisibility(View.GONE);
            tvDescription.setVisibility(View.GONE);

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Set FrameLayout to original size
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT; // undo fullscreen
            layoutParams.setMargins(0,20,0,0); // add margin for portrait
            tvTitle.setVisibility(View.VISIBLE);
            tvPurpose.setVisibility(View.VISIBLE);
            tvDescription.setVisibility(View.VISIBLE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
        videoFrame.setLayoutParams(layoutParams);
    }
}