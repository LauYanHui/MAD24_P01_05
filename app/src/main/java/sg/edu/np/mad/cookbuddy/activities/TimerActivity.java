package sg.edu.np.mad.cookbuddy.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.helpers.AlarmReceiver;
import sg.edu.np.mad.cookbuddy.helpers.NotificationHelper;
import sg.edu.np.mad.cookbuddy.models.Recipe;

public class TimerActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "timer_channel";
    private static final int VIBRATE_PERMISSION_REQUEST_CODE = 1;
    private static final int SCHEDULE_EXACT_ALARM_REQUEST_CODE = 2;
    private TextView timerTextView;
    private NumberPicker minutePicker, secondPicker;
    private Button startButton, pauseButton, resumeButton, resetButton, stopAlarm;

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

        // Check for Vibrate permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.VIBRATE},
                    VIBRATE_PERMISSION_REQUEST_CODE);
        }

        // Check for SCHEDULE_EXACT_ALARM permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM},
                    SCHEDULE_EXACT_ALARM_REQUEST_CODE);
        }

        backButton = findViewById(R.id.btnBack);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            recipe = (Recipe) intent.getSerializableExtra("Recipe");
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RecipeDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Recipe", recipe);
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
        stopAlarm = findViewById(R.id.stopAlarm);

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
        stopAlarm.setOnClickListener(v -> stopAlarm());

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
        pauseButton.setEnabled(true); // Enable stop button when timer starts

        runnable = new Runnable() {
            @Override
            public void run() {
                if (seconds == 0) {
                    if (minutes == 0) {
                        // Timer finished
                        isRunning = false;
                        showNotification();
                        if (canScheduleExactAlarms()) {
                            setAlarm(); // Set the alarm
                        }
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
        handler.removeCallbacks(runnable);
        minutes = minutePicker.getValue();
        seconds = secondPicker.getValue();
        updateTimerText();
        pauseButton.setEnabled(false);
    }

    private void updateTimerText() {
        String time = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(time);
    }

    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Timer Finished")
                .setContentText("Your timer has finished.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void setAlarm() {
        long triggerTime = System.currentTimeMillis() + 1000; // Trigger immediately

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
    }

    private boolean canScheduleExactAlarms() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        return alarmManager.canScheduleExactAlarms();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == VIBRATE_PERMISSION_REQUEST_CODE) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Vibrate permission denied. Timer may not vibrate.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == SCHEDULE_EXACT_ALARM_REQUEST_CODE) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Exact alarm permission denied. Alarms may not be set correctly.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void stopAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        // Stop any ongoing notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

        Toast.makeText(this, "Alarm stopped.", Toast.LENGTH_SHORT).show();

        // Optionally, update button state
        stopAlarm.setEnabled(false);
    }
}
