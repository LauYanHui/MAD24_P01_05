package sg.edu.np.mad.cookbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import sg.edu.np.mad.cookbuddy.R;

public class VerifyEmailActivity extends AppCompatActivity {
    private final static String TAG = "VerifyEmailActivity";
    private LottieAnimationView animation;
    private ScheduledExecutorService scheduler;
    private FirebaseAuth mAuth;

    private FirebaseUser user;
    private Button btnNext;
    private TextView tvInstructions;
    private boolean isVerified = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // shouldn't happen
        if (user == null) {
            Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(VerifyEmailActivity.this, RegisterActivity.class));
        }

        String instructions = "A verification email has been sent to "
                + user.getEmail()
                + " . To proceed, please verify your email.";

        tvInstructions = findViewById(R.id.tvInstructions);
        animation = findViewById(R.id.animation);
        btnNext = findViewById(R.id.btnNext);

        tvInstructions.setText(instructions);

        // only run first portion of animation
        animation.setMinAndMaxFrame(1, 90);

        // enable only if email is verified
        btnNext.setEnabled(false);
        btnNext.setOnClickListener(v -> {
            startActivity(new Intent(VerifyEmailActivity.this, UsernameActivity.class));
        });

        // periodically check whether email is verified
        checkEmailVerificationStatus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // ensure scheduler has no memory leakage
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }

    private void checkEmailVerificationStatus() {
        scheduler = Executors.newScheduledThreadPool(1);
        // if verified, enable button and run last section of animation once
        // stop scheduling tasks
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                user.reload().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && user != null) {
                        isVerified = user.isEmailVerified();

                        // if verified, enable button and run last section of animation once
                        if (isVerified) {
                            runOnUiThread(() -> {
                                btnNext.setEnabled(true);
                                animation.setMinAndMaxFrame(92, 211);
                                animation.setRepeatCount(0);
                                Log.d(TAG, "EmailVerificationStatus:Verified");
                            });
                        }
                    } else {
                        scheduler.shutdown(); // stop scheduling tasks
                    }
                });
            }
        }, 0, 5, TimeUnit.SECONDS); // check verification status every 5 seconds
    }
}