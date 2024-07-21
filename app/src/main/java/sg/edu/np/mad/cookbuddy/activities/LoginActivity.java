package sg.edu.np.mad.cookbuddy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sg.edu.np.mad.cookbuddy.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private boolean pressedBackOnce;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // Allow user to navigate to register activity
        SpannableString ss = getRegisterLink();
        tvRegister.setText(ss);
        tvRegister.setMovementMethod(LinkMovementMethod.getInstance());
        tvRegister.setHighlightColor(Color.TRANSPARENT);

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            // runs everytime the activity is launched if there is a cached user
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser(); // get cached user

                if (user != null) {
                    // get latest user info
                    user.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            // If user has display name, they've completed the sign up process and can login automatically
                            if (user.getDisplayName() != null) {
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            }
                        }
                    });
                }
            }
        };

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    etEmail.setText(result);
                    etEmail.setSelection(result.length());
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    etPassword.setText(result);
                    etPassword.setSelection(result.length());
                }
            }
        });

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (email.isEmpty()) {
                etEmail.setError("Empty field");
            } else if (password.isEmpty()) {
                etPassword.setError("Empty field");
            } else {
                signInUser(email, password);
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pressedBackOnce = false;
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
    private SpannableString getRegisterLink() {
        SpannableString ss = new SpannableString("Don't have an account? Register here");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
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

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                   if (!task.isSuccessful()) {
                       Log.w(TAG, "signInUser:failure", task.getException());
                       Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                       return;
                   }
                   Log.d(TAG, "signInUser:success");
                   user = mAuth.getCurrentUser();

                   if (user == null) {
                       Log.d(TAG, "fetchData:failure");
                       Toast.makeText(getApplicationContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                       return;
                   }
                   Log.d(TAG, "fetchData:success");

                   if (!user.isEmailVerified()) {
                       startActivity(new Intent(LoginActivity.this, VerifyEmailActivity.class));
                   } else if (user.getDisplayName() == null) {
                       startActivity(new Intent(LoginActivity.this, UsernameActivity.class));
                   } else {
                       startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                   }
                });
    }

    private void handleBackPressed() {
        if (pressedBackOnce) {
            finish();
            return;
        }

        pressedBackOnce = true;
        Toast.makeText(getApplicationContext(), "Click back again to exit", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                pressedBackOnce = false;
            }
        }, 2000);
    }
}