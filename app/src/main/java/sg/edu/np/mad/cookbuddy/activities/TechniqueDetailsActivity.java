package sg.edu.np.mad.cookbuddy.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
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
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import org.w3c.dom.Text;

import sg.edu.np.mad.cookbuddy.R;

public class TechniqueDetailsActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvPurpose;
    TextView tvDescription;
    PlayerView videoView;

    ExoPlayer player;
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


        player = new ExoPlayer.Builder(this).build();
        videoView.setPlayer(player);
        // setup videoView
        Uri videoUri = Uri.parse(videoPath);
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        player.setMediaItem(mediaItem);

        player.prepare();
        player.setPlayWhenReady(true);
    }

    //on landscape, videoView will become fullScreen and resets when user is back to portrait.
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ViewGroup.LayoutParams videoViewParams = videoView.getLayoutParams();
        LinearLayout.LayoutParams videoFrameParams = (LinearLayout.LayoutParams) videoFrame.getLayoutParams();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Make video fullscreen
            videoViewParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoView.setLayoutParams(videoViewParams);
            videoFrameParams.setMargins(0,0,0,0); // remove margin in landscape
            videoFrameParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoFrameParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            videoFrameParams.gravity = Gravity.CENTER;
            videoFrame.setLayoutParams(videoFrameParams);

            // Hide other widgets
            tvTitle.setVisibility(View.GONE);
            tvPurpose.setVisibility(View.GONE);
            tvDescription.setVisibility(View.GONE);

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Restore original size
            videoViewParams.height = getResources().getDimensionPixelSize(R.dimen.video_height);
            videoView.setLayoutParams(videoViewParams);
            videoFrameParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            videoFrameParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            videoFrameParams.gravity = Gravity.NO_GRAVITY;
            videoFrame.setLayoutParams(videoFrameParams);

            videoFrameParams.setMargins(0,20,0,0); // add margin for portrait
            // Show other widgets
            tvTitle.setVisibility(View.VISIBLE);
            tvPurpose.setVisibility(View.VISIBLE);
            tvDescription.setVisibility(View.VISIBLE);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }
}