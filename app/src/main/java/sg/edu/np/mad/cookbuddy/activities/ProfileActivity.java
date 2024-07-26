package sg.edu.np.mad.cookbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.widget.EditText;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.models.User;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference userRef;
    private User currentUser;
    private LinearLayout bookmarkedTechniquesLayout;
    private LinearLayout favoriteRecipesLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase reference
        String FIREBASE_URL = "https://mad-assignment-8c5d2-default-rtdb.asia-southeast1.firebasedatabase.app/";
        userRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("Users");

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("User");
        if (user != null) {
            currentUser = user;
            Log.d("ProfileActivity", "User: " + user.toString());
        } else {
            Log.d("ProfileActivity", "User is null");
            Toast.makeText(this, "User data not available", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if user data is not available
            return;
        }

        // Initialize UI elements
        Button btnEditProfile = findViewById(R.id.btn_edit_profile);
        TextView tvUsername = findViewById(R.id.username);
//        bookmarkedTechniquesLayout = findViewById(R.id.bookmarked_techniques_container);
        favoriteRecipesLayout = findViewById(R.id.favorite_recipes_container);

        tvUsername.setText(user.getUsername());

        // Fetch and display user data
        fetchUserData();

        btnEditProfile.setOnClickListener(v -> showEditDetailsDialog());
    }
    private void fetchUserData() {
        userRef.child(currentUser.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Fetch and display bookmarked techniques
                    for (DataSnapshot techniqueSnapshot : snapshot.child("bookmarkedTechniques").getChildren()) {
                        String technique = techniqueSnapshot.getValue(String.class);
                    }

                    // Fetch and display favourite recipes
                    for (DataSnapshot recipeSnapshot : snapshot.child("favorites").getChildren()) {
                        String recipe = recipeSnapshot.getKey();

                        addFavoriteRecipe(recipe);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void addFavoriteRecipe(String recipe) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        textView.setText(recipe);

        Button removeButton = new Button(this);
        removeButton.setLayoutParams(new LinearLayout.LayoutParams(40, 40));

        layout.addView(textView);
        layout.addView(removeButton);

        favoriteRecipesLayout.addView(layout);
    }



    private void showEditDetailsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_profile, null);
        builder.setView(dialogView);

        EditText etNewUsername = dialogView.findViewById(R.id.et_new_username);
        EditText etNewPassword = dialogView.findViewById(R.id.et_new_password);
        EditText etConfirmPassword = dialogView.findViewById(R.id.et_confirm_password);

        builder.setTitle("Edit Details");
        builder.setPositiveButton("Save", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = etNewUsername.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(newUsername)) {
                    etNewUsername.setError("Username cannot be empty");
                    return;
                }

                if (TextUtils.isEmpty(newPassword)) {
                    etNewPassword.setError("Password cannot be empty");
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    etConfirmPassword.setError("Passwords do not match");
                    return;
                }

                checkIfUsernameIsUnique(newUsername, newUsernameAvailable -> {
                    if (newUsernameAvailable) {
                        saveUserData(newUsername, newPassword);
                        dialog.dismiss();
                    } else {
                        etNewUsername.setError("Username is already taken");
                    }
                });
            }
        });
    }

    private void checkIfUsernameIsUnique(String username, final OnUsernameCheckListener listener) {
        userRef.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful() && !task.getResult().exists()) {
                    listener.onCheck(true);
                } else {
                    listener.onCheck(false);
                }
            }
        });
    }

    private void saveUserData(String newUsername, String newPassword) {
        currentUser.setUsername(newUsername);
        currentUser.setPassword(newPassword);

        userRef.child(currentUser.getUsername()).setValue(currentUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    recreate(); // Optionally refresh the activity to reflect the changes
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String formatList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "None";
        }
        StringBuilder formattedList = new StringBuilder();
        for (String item : list) {
            formattedList.append("• ").append(item).append("\n");
        }
        return formattedList.toString().trim();
    }

    private interface OnUsernameCheckListener {
        void onCheck(boolean isUnique);
    }
}
