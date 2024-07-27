package sg.edu.np.mad.cookbuddy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;


import nu.aaro.gustav.passwordstrengthmeter.PasswordStrengthCalculator;
import nu.aaro.gustav.passwordstrengthmeter.PasswordStrengthMeter;
import sg.edu.np.mad.cookbuddy.R;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private EditText etEmail;
    private EditText etPassword;
    private EditText etFiltered;
    private Button btnRegister;
    private TextView tvLogin;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private PasswordStrengthMeter meter;
    private boolean validEmail;
    private boolean validPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etFiltered = findViewById(R.id.etFiltered);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        meter = findViewById(R.id.passwordMeter);

        validEmail = false;
        validPw = false;


        // Allow user to navigate to login activity
        SpannableString ss = getLoginLink();
        tvLogin.setText(ss);
        tvLogin.setMovementMethod(LinkMovementMethod.getInstance());
        tvLogin.setHighlightColor(Color.TRANSPARENT);

        // Perform simple check on email
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
                } else {
                    validEmail = isValidEmail(s);
                    btnRegister.setEnabled(validEmail && validPw);
                    if (!validEmail) {
                        etEmail.setError("Invalid email");
                    }
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

                // prevent infinite loop
                if (!s.toString().equals(result)) {
                    etPassword.setText(result);
                    etPassword.setSelection(result.length());
                }

                etFiltered.setText(result);
            }
        });

        // Disable button until email and password is accepted
        btnRegister.setEnabled(false);

        // Check value entered before attempting to create a new user
        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            createUser(email, password);
        });

        meter.setEditText(etFiltered);
        meter.setPasswordStrengthCalculator(new PasswordStrengthCalculator() {
            @Override
            public int calculatePasswordSecurityLevel(String s) {
                double score = 3;

                // show meter only when user starts typing
                meter.setVisibility(View.VISIBLE);

                // if length less than minimum length,
                // don't conduct any other checks
                if (s.isEmpty()) {
                    return 0; // too short
                }

                if (s.length() < 5) {
                    return 1;
                }

                if (s.length() < getMinimumLength()) {
                    return 2;
                }

                // reward longer passwords
                if (s.length() >= 12) {
                    score += 0.5;
                }

                // check for number
                if (Pattern.compile("\\d").matcher(s).find()) {
                    score += 0.5;
                }

                // check for at least 1 upper and lowercase
                if (Pattern.compile("[a-z]").matcher(s).find() &&
                        Pattern.compile("[A-Z]").matcher(s).find()) {
                    score += 0.5;
                }

                // check for symbols/special chars
                if (Pattern.compile("[~`!@#$%^&*()_\\-+={\\[}\\]|\\\\:;\"'<,>.?/]")
                        .matcher(s).find()) {
                    score += 0.5;
                }

                return (int) Math.floor(score);
            }

            @Override
            public int getMinimumLength() {
                return 8;
            }

            @Override
            public boolean passwordAccepted(int i) {
                // This is a temporary workaround since there
                // is no 'onPasswordRejected' method in the calculator interface.
                // Ideally, the flag and state of the button should be changed in
                // those methods.
                validPw = i > 3;
                btnRegister.setEnabled(validPw && validEmail);
                return i > 3;
            }

            @Override
            public void onPasswordAccepted(String s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @NonNull
    private SpannableString getLoginLink() {
        SpannableString ss = new SpannableString("Already have an account? Login here");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
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

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUser:success");
                        sendVerificationEmail();
                    } else {
                        Log.w(TAG, "createUser:failure");
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public static boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private void sendVerificationEmail() {
        user = mAuth.getCurrentUser();

        // pray there is a user because that's how it should theoretically work
        // if there isn't, i'm screwed
        if (user == null) {
           Toast.makeText(getApplicationContext(), "No user", Toast.LENGTH_SHORT).show();
           return;
        }

        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "sendVerificationEmail:success");
                startActivity(new Intent(RegisterActivity.this, VerifyEmailActivity.class));
            } else {
                Log.d(TAG, "sendVerificationEmail:failure");
                Toast.makeText(getApplicationContext(), "Failed to send verification email", Toast.LENGTH_SHORT).show();
            }
        });
    }
}