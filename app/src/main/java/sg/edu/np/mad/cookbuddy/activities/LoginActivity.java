package sg.edu.np.mad.cookbuddy.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.models.User;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    Button btnRegister;
    EditText etUsername;
    EditText etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get widgets
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        // Go to Register Page
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerUser = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerUser);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Disable login button
                btnLogin.setEnabled(false);

                // Get value in EditText
                String username = String.valueOf(etUsername.getText());
                String password = String.valueOf(etPassword.getText());

                try {
                    if (username.isEmpty()) {
                        throw new RuntimeException("Username is empty");
                    }
                    if (password.isEmpty()) {
                        throw new RuntimeException("Password is empty");
                    }

                    // If no field is empty, get reference from Firebase
                    DatabaseReference userRef = FirebaseDatabase.getInstance(HomeActivity.FIREBASE_URL).getReference("Users/");

                    userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                ArrayList<String> allergies = new ArrayList<>();

                                String tempUsername = snapshot.child("username").getValue(String.class);
                                String tempPassword = snapshot.child("password").getValue(String.class);

                                for (DataSnapshot allergy : snapshot.child("allergies").getChildren()) {
                                    allergies.add(allergy.getValue(String.class));
                                }

                                User temp = new User(tempUsername, tempPassword, allergies);

                                // account with this username exists, check password
                                if (password.equals(temp.getPassword())) {

                                    // add user info in sharedPref so other activities can edit/retrieve info from FB
                                    SharedPreferences sharedPref = getSharedPreferences("user", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("username", temp.getUsername());
                                    editor.apply();

                                    // go to home page
                                    Intent goHome = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(goHome);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "No account found", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (RuntimeException e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {

                    // Enable login button
                    btnLogin.setEnabled(true);
                }
            }
        });
    }
}