package sg.edu.np.mad.cookbuddy.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.DefaultTimeBar;
import androidx.media3.ui.PlayerView;
import androidx.media3.ui.TimeBar;


import java.util.Formatter;

import sg.edu.np.mad.cookbuddy.R;

public class TechniqueDetailsActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvPurpose;
    TextView tvDescription;
    PlayerView videoView;

    @UnstableApi
    DefaultTimeBar playerTimeBar;
    Handler handler;
    ExoPlayer player;
    RelativeLayout videoFrame;

    //custom_player_controls view groups
    ImageView ivPlayPause, ivFastRewind, ivFastForward, ivFullScreen, ivSpeedControl;
    TextView tvVideoPosition, tvVideoDuration;

    String[] speed_options = {"0.5x", "0.75x", "Normal", "1.25x", "1.5x"};

    boolean isFullscreen = false;
    @UnstableApi
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

        //Initialize ExoPlayer
        player = new ExoPlayer.Builder(this).build();
        videoView.setPlayer(player);

        // setup videoView
        Uri videoUri = Uri.parse(videoPath);
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        player.setMediaItem(mediaItem);

        player.prepare();
        player.setPlayWhenReady(true);

        //find custom controls in videoView by id
        ivPlayPause = videoView.findViewById(R.id.ivPlayPause);
        ivFastRewind = videoView.findViewById(R.id.ivFastRewind);
        ivFastForward = videoView.findViewById(R.id.ivFastForward);
        ivFullScreen = videoView.findViewById(R.id.ivFullScreen);
        ivSpeedControl = videoView.findViewById(R.id.ivSpeedControl);
        tvVideoPosition = videoView.findViewById(R.id.tvVideoPosition);
        tvVideoDuration = videoView.findViewById(R.id.tvVideoDuration);
        playerTimeBar = videoView.findViewById(R.id.playerTimeBar);

        //handle when user drags the player Time bar
        playerTimeBar.addListener(new DefaultTimeBar.OnScrubListener() {
            @Override
            public void onScrubStart(TimeBar timeBar, long position) {
                //scrub start methods
            }

            @Override
            public void onScrubMove(TimeBar timeBar, long position) {
                player.seekTo(position);
                updateUI();
            }

            @Override
            public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
                player.seekTo(position);
                updateUI();
            }
        });
        player.addListener(new Player.Listener(){
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_READY){
                    ivPlayPause.setVisibility(View.VISIBLE);
                }
                else if (playbackState == Player.STATE_BUFFERING){
                    ivPlayPause.setVisibility(View.INVISIBLE);
                }
                else if (playbackState == Player.STATE_ENDED){
                    ivPlayPause.setImageResource(R.drawable.ic_play_48);
                    ivPlayPause.setVisibility(View.VISIBLE);
                }
            }
        });
        ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.getPlaybackState() == Player.STATE_ENDED){
                    player.seekTo(0);
                }
                player.setPlayWhenReady(!player.getPlayWhenReady());
                ivPlayPause.setImageResource(Boolean.TRUE.equals(player.getPlayWhenReady()) ? R.drawable.ic_pause_48 : R.drawable.ic_play_48);
            }
        });

        // rewind video by 5 seconds
        ivFastRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long num = player.getCurrentPosition() - 5000;
                // for when video hasn't reached a position greater than 00:05
                if (num < 0){
                    player.seekTo(0);
                }
                else{
                    player.seekTo(num);
                }
            }
        });
        ivFastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.seekTo(player.getCurrentPosition() + 5000);
            }
        });
        ivSpeedControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TechniqueDetailsActivity.this);
                builder.setTitle("Speed Control");
                builder.setItems(speed_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            PlaybackParameters speedParams = new PlaybackParameters(0.5f);
                            player.setPlaybackParameters(speedParams);
                        }
                        if (which == 1){
                            PlaybackParameters speedParams = new PlaybackParameters(0.75f);
                            player.setPlaybackParameters(speedParams);
                        }
                        if (which == 2){
                            PlaybackParameters speedParams = new PlaybackParameters(1f);
                            player.setPlaybackParameters(speedParams);
                        }
                        if (which == 3){
                            PlaybackParameters speedParams = new PlaybackParameters(1.25f);
                            player.setPlaybackParameters(speedParams);
                        }
                        if (which == 4){
                            PlaybackParameters speedParams = new PlaybackParameters(1.5f);
                            player.setPlaybackParameters(speedParams);
                        }
                    }
                });
                builder.show();
            }
        });
        videoView.setControllerVisibilityListener(new PlayerView.ControllerVisibilityListener() {
            @Override
            public void onVisibilityChanged(int visibility) {
                if (isFullscreen){
                    getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                    isFullscreen = true;
                }
            }
        });
        ivFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFullscreen();
            }
        });
        handler = new Handler(Looper.getMainLooper());
        updateUI();
    }

    @OptIn(markerClass = UnstableApi.class)
    private void updateUI() {
        if (player != null && player.isPlaying()){
            long videoPosition = player.getCurrentPosition();
            long videoDuration = player.getDuration();

            tvVideoPosition.setText(Util.getStringForTime(new StringBuilder(), new Formatter(), videoPosition));
            tvVideoDuration.setText(Util.getStringForTime(new StringBuilder(), new Formatter(), videoDuration));
            playerTimeBar.setDuration(videoDuration);
            playerTimeBar.setPosition(videoPosition);
            // update realtime the videoPosition and playerTimeBar
            handler.postDelayed(this::updateUI, 1000);
        }
        else{
            handler.postDelayed(this::updateUI, 1000);
        }
    }

    //ivFullScreen activates landscape or portrait, onConfigChanged will change the playerView to enter 'fullscreen mode'
    private void toggleFullscreen() {
        if (!isFullscreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }



    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ViewGroup.LayoutParams videoViewParams = videoView.getLayoutParams();
        LinearLayout.LayoutParams videoFrameParams = (LinearLayout.LayoutParams) videoFrame.getLayoutParams();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Enter fullscreen mode
            ivFullScreen.setImageResource(R.drawable.ic_fullscreen_exit_48);
            videoViewParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoView.setLayoutParams(videoViewParams);

            videoFrameParams.setMargins(0, 0, 0, 0); // Remove margins
            videoFrameParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoFrameParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            videoFrameParams.gravity = Gravity.CENTER;
            videoFrame.setLayoutParams(videoFrameParams);

            tvTitle.setVisibility(View.GONE);
            tvPurpose.setVisibility(View.GONE);
            tvDescription.setVisibility(View.GONE);

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

            isFullscreen = true;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Exit fullscreen mode
            ivFullScreen.setImageResource(R.drawable.ic_fullscreen_48);
            videoViewParams.height = getResources().getDimensionPixelSize(R.dimen.video_height);
            videoView.setLayoutParams(videoViewParams);

            videoFrameParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            videoFrameParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            videoFrameParams.gravity = Gravity.NO_GRAVITY;
            videoFrame.setLayoutParams(videoFrameParams);

            videoFrameParams.setMargins(0, 20, 0, 0); // Add margins for portrait
            tvTitle.setVisibility(View.VISIBLE);
            tvPurpose.setVisibility(View.VISIBLE);
            tvDescription.setVisibility(View.VISIBLE);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

            isFullscreen = false;
        }
    }

    @Override
    protected void onResume() {
        player.play();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }
}
