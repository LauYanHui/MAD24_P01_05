package sg.edu.np.mad.cookbuddy.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.helpers.NotificationHelper;
import sg.edu.np.mad.cookbuddy.models.Recipe;

public class TimerActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "timer_channel";
    private TextView timerTextView;
    private NumberPicker minutePicker, secondPicker;
    private Button startButton, pauseButton, resumeButton, resetButton;

    private Handler handler;
    private Runnable runnable;
    private int minutes = 25; // Default duration
    private int seconds = 0;
    private boolean isRunning = false;
    private boolean isPaused = false;
    private int remainingMinutes;
    private int remainingSeconds;
    private ImageView backButton;
    private Recipe recipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        NotificationHelper.createNotificationChannel(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        backButton = findViewById(R.id.btnBack);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            recipe = (Recipe) intent.getSerializableExtra("Recipe");
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),RecipeDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Recipe",recipe);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        timerTextView = findViewById(R.id.timerTextView);
        minutePicker = findViewById(R.id.minutePicker);
        secondPicker = findViewById(R.id.secondPicker);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        resumeButton = findViewById(R.id.resumeButton);
        resetButton = findViewById(R.id.resetButton);

        handler = new Handler();

        // Initialize NumberPickers
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setValue(25); // Default value

        secondPicker.setMinValue(0);
        secondPicker.setMaxValue(59);
        secondPicker.setValue(0); // Default value

        // Set up button listeners
        startButton.setOnClickListener(v -> {
            if (!isRunning) {
                if (isPaused) {
                    // Resume timer
                    resumeTimer();
                } else {
                    // Start new timer
                    minutes = minutePicker.getValue();
                    seconds = secondPicker.getValue();
                    startTimer();
                }
            }
        });

        pauseButton.setOnClickListener(v -> pauseTimer());

        resumeButton.setOnClickListener(v -> {
            if (isPaused) {
                resumeTimer();
            }
        });

        resetButton.setOnClickListener(v -> resetTimer());
    }

    private void startTimer() {
        isRunning = true;
        isPaused = false;

        runnable = new Runnable() {
            @Override
            public void run() {
                if (seconds == 0) {
                    if (minutes == 0) {
                        // Timer finished
                        isRunning = false;
                        showNotification();
                        return;
                    }
                    minutes--;
                    seconds = 59;
                } else {
                    seconds--;
                }

                updateTimerText();
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    private void pauseTimer() {
        if (isRunning) {
            isRunning = false;
            isPaused = true;
            handler.removeCallbacks(runnable);
            remainingMinutes = minutes;
            remainingSeconds = seconds;
        }
    }

    private void resumeTimer() {
        isRunning = true;
        isPaused = false;
        minutes = remainingMinutes;
        seconds = remainingSeconds;
        startTimer();
    }

    private void resetTimer() {
        isRunning = false;
        isPaused = false;
        minutes = minutePicker.getValue();
        seconds = secondPicker.getValue();
        updateTimerText();
        handler.removeCallbacks(runnable);
    }

    private void updateTimerText() {
        String minutesText = String.format("%02d", minutes);
        String secondsText = String.format("%02d", seconds);
        timerTextView.setText(minutesText + ":" + secondsText);
    }
    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification) // Replace with your notification icon
                .setContentTitle("Timer Finished")
                .setContentText("Your timer has completed.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
}