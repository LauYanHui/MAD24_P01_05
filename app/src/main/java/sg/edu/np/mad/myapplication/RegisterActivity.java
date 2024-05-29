package sg.edu.np.mad.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister;
    Button btnLogin;
    EditText etUsername;
    EditText etPassword;
    EditText etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get database
        String FIREBASE_URL = "https://mad-assignment-8c5d2-default-rtdb.asia-southeast1.firebasedatabase.app/";
        DatabaseReference userRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("Users/");

        // Get widgets
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        // Switch to Login page
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginActivity);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Disable register button
                btnRegister.setEnabled(false);

                // Get value user entered in EditText
                String username = String.valueOf(etUsername.getText());
                String password = String.valueOf(etPassword.getText());
                String confirmPassword = String.valueOf(etConfirmPassword.getText());

                try {
                    // Check for invalid fields
                    if (username.isEmpty()) {
                        throw new RuntimeException("Username cannot be empty");
                    }
                    if (password.isEmpty()) {
                        throw new RuntimeException("Password cannot be empty");
                    }
                    if (!password.equals(confirmPassword)) {
                        throw new RuntimeException("Passwords do not match");
                    }
                    userRef.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Check if username already exists
                            if (task.getResult().exists()) {
                                Toast.makeText(RegisterActivity.this, "Pick a different username", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Set data in Firebase
                            userRef.child(username).setValue(new User(username, password));
                            Toast.makeText(RegisterActivity.this, "User registered.", Toast.LENGTH_SHORT).show();

                            // Reset fields
                            etUsername.getText().clear();
                            etPassword.getText().clear();
                            etConfirmPassword.getText().clear();
                        }
                    });
                } catch (RuntimeException e) {
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {
                    btnRegister.setEnabled(true);
                }
            }
        });
    }
}