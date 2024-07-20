package sg.edu.np.mad.cookbuddy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sg.edu.np.mad.cookbuddy.R;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser(); // get cached user

                if (user != null) {

                    // get latest user info
                    user.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // if user is signed in, redirect to other activities
                            if (user != null) {
                                if (!user.isEmailVerified()) {
                                    startActivity(new Intent(RegisterActivity.this, VerifyEmailActivity.class));
                                } else if (user.getDisplayName() == null) {
                                    startActivity(new Intent(RegisterActivity.this, UsernameActivity.class));
                                } else {
                                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                }
                            }
                        }
                    });
                }
            }
        };

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        // Allow user to navigate to login activity
        SpannableString ss = getLoginLink();
        tvLogin.setText(ss);
        tvLogin.setMovementMethod(LinkMovementMethod.getInstance());
        tvLogin.setHighlightColor(Color.TRANSPARENT);

        // Check value entered before attempting to create a new user
        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (!isValidEmail(email)) {
                etEmail.setError("Invalid email");
            } else if (password.isEmpty()) {
                etPassword.setError("Password cannot be empty");
            } else if (!confirmPassword.equals(password)) {
                etConfirmPassword.setError("Confirm password must match password");
            } else {
                createUser(email, password);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @NonNull
    private SpannableString getLoginLink() {
        SpannableString ss = new SpannableString("Already have an account? Login here");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                int color = ContextCompat.getColor(getApplicationContext(), R.color.caribbeanCurrent);
                ds.setColor(color);
                ds.setUnderlineText(true);
            }
        };

        ss.setSpan(clickableSpan, ss.length() - 4, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUser:success");
                        verifyEmail();
                    } else {
                        Log.w(TAG, "createUser:failure");
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private void verifyEmail() {
        FirebaseUser user = mAuth.getCurrentUser();

        // pray there is a user because that's how it should theoretically work
        // if there isn't, i'm screwed
        if (user == null) {
           Toast.makeText(getApplicationContext(), "No user", Toast.LENGTH_SHORT).show();
           return;
        }

        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "verifyEmail:success");
                startActivity(new Intent(RegisterActivity.this, VerifyEmailActivity.class));
            } else {
                Log.d(TAG, "verifyEmail:failure");
                Toast.makeText(getApplicationContext(), "Failed to send verification email", Toast.LENGTH_SHORT).show();
            }
        });
    }
}