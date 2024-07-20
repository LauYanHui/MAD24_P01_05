package sg.edu.np.mad.cookbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


import sg.edu.np.mad.cookbuddy.R;

public class UsernameActivity extends AppCompatActivity {
    private final static String TAG = "UsernameActivity";
    private EditText et;
    private Button btnNext;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // shouldn't be the case
        if (user == null) {
            Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UsernameActivity.this, RegisterActivity.class));
        }

        et = findViewById(R.id.editText);
        btnNext = findViewById(R.id.btnNext);
        btnNext.setEnabled(false);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = String.valueOf(s).trim().length();

                // enable button if username is appropriately long
                btnNext.setEnabled(length >= 8 && length <= 30);
            }
        });

        btnNext.setOnClickListener(v -> {
            String username = et.getText().toString().trim();

            UserProfileChangeRequest req = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build();

            user.updateProfile(req).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "SetDisplayName:Success");
                    startActivity(new Intent(UsernameActivity.this, HomeActivity.class));
                }
            });
        });
    }
}