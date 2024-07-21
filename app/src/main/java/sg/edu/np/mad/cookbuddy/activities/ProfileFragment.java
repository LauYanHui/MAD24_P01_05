package sg.edu.np.mad.cookbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.models.User;

public class ProfileFragment extends Fragment {

    private DatabaseReference userRef;
    private User currentUser;
    private LinearLayout bookmarkedTechniquesLayout;
    private LinearLayout favoriteRecipesLayout;
    private LinearLayout allergiesLayout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase reference
        String FIREBASE_URL = "https://mad-assignment-8c5d2-default-rtdb.asia-southeast1.firebasedatabase.app/";
        userRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("Users");

        // Initialize UI elements
        Button btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        Button btnEditAllergies = view.findViewById(R.id.btn_edit_allergies);
        TextView tvUsername = view.findViewById(R.id.username);
        bookmarkedTechniquesLayout = view.findViewById(R.id.bookmarked_techniques_container);
        favoriteRecipesLayout = view.findViewById(R.id.favorite_recipes_container);
        allergiesLayout = view.findViewById(R.id.allergies_container);

        // Get user data from arguments
        Bundle args = getArguments();
        if (args != null) {
            currentUser = (User) args.getSerializable("User");
            if (currentUser != null) {
                Log.d("ProfileFragment", "User: " + currentUser.toString());
                tvUsername.setText(currentUser.getUsername());
                fetchUserData();
            } else {
                Log.d("ProfileFragment", "User is null");
                Toast.makeText(getContext(), "User data not available", Toast.LENGTH_SHORT).show();
            }
        }

        btnEditProfile.setOnClickListener(v -> showEditDetailsDialog());
        btnEditAllergies.setOnClickListener(v -> showEditAllergiesDialog());

        return view;
    }

    private void fetchUserData() {
        userRef.child(currentUser.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Clear existing views
                    allergiesLayout.removeAllViews();
                    bookmarkedTechniquesLayout.removeAllViews();
                    favoriteRecipesLayout.removeAllViews();

                    // Fetch and display allergies
                    for (DataSnapshot allergySnapshot : snapshot.child("allergies").getChildren()) {
                        String allergy = allergySnapshot.getValue(String.class);
                        addAllergy(allergy);
                    }

                    // Fetch and display bookmarked techniques
                    for (DataSnapshot techniqueSnapshot : snapshot.child("bookmarkedTechniques").getChildren()) {
                        String technique = techniqueSnapshot.getValue(String.class);
                        addBookmarkedTechnique(technique);
                    }

                    // Fetch and display favourite recipes
                    for (DataSnapshot recipeSnapshot : snapshot.child("favoriteRecipes").getChildren()) {
                        String recipe = recipeSnapshot.getValue(String.class);
                        addFavoriteRecipe(recipe);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addAllergy(String allergy) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        textView.setText(allergy);
        textView.setId(View.generateViewId()); // Ensure each TextView has a unique ID

        // Only add the TextView for the allergy
        layout.addView(textView);

        allergiesLayout.addView(layout);
    }

    private void addBookmarkedTechnique(String technique) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        textView.setText(technique);

        Button removeButton = new Button(getContext());
        removeButton.setLayoutParams(new LinearLayout.LayoutParams(40, 40));
        removeButton.setBackgroundResource(android.R.drawable.ic_delete);
        removeButton.setOnClickListener(v -> removeBookmarkedTechnique(technique));

        layout.addView(textView);
        layout.addView(removeButton);

        bookmarkedTechniquesLayout.addView(layout);
    }

    private void removeBookmarkedTechnique(String technique) {
        userRef.child(currentUser.getUsername()).child("bookmarkedTechniques").orderByValue().equalTo(technique).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.getRef().removeValue();
                }
                // Refresh the UI
                bookmarkedTechniquesLayout.removeAllViews();
                fetchUserData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to remove technique.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addFavoriteRecipe(String recipe) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        textView.setText(recipe);

        Button removeButton = new Button(getContext());
        removeButton.setLayoutParams(new LinearLayout.LayoutParams(40, 40));
        removeButton.setBackgroundResource(android.R.drawable.ic_delete);
        removeButton.setOnClickListener(v -> removeFavoriteRecipe(recipe));

        layout.addView(textView);
        layout.addView(removeButton);

        favoriteRecipesLayout.addView(layout);
    }

    private void removeFavoriteRecipe(String recipe) {
        userRef.child(currentUser.getUsername()).child("favoriteRecipes").orderByValue().equalTo(recipe).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.getRef().removeValue();
                }
                // Refresh the UI
                favoriteRecipesLayout.removeAllViews();
                fetchUserData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to remove recipe.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditDetailsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

                if (!TextUtils.isEmpty(newPassword) && !newPassword.equals(confirmPassword)) {
                    etConfirmPassword.setError("Passwords do not match");
                    return;
                }

                userRef.child(currentUser.getUsername()).child("username").setValue(newUsername);
                if (!TextUtils.isEmpty(newPassword)) {
                    userRef.child(currentUser.getUsername()).child("password").setValue(newPassword);
                }
                currentUser.setUsername(newUsername);
                fetchUserData(); // Refresh the data
                dialog.dismiss();
            }
        });
    }

    private void showEditAllergiesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_allergies, null);
        builder.setView(dialogView);

        LinearLayout allergiesContainer = dialogView.findViewById(R.id.allergies_container);

        // Fetch all possible allergies from your database
        DatabaseReference allergiesRef = FirebaseDatabase.getInstance().getReference("Allergies");
        allergiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<CheckBox> checkBoxList = new ArrayList<>();

                // Get all allergies
                List<String> allAllergies = new ArrayList<>();
                for (DataSnapshot allergySnapshot : snapshot.getChildren()) {
                    String allergy = allergySnapshot.getValue(String.class);
                    allAllergies.add(allergy);

                    // Create a CheckBox for each allergy
                    CheckBox checkBox = new CheckBox(getContext());
                    checkBox.setText(allergy);
                    checkBoxList.add(checkBox);
                }

                // Fetch user's current allergies
                userRef.child(currentUser.getUsername()).child("allergies").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> userAllergies = new ArrayList<>();
                        for (DataSnapshot allergySnapshot : snapshot.getChildren()) {
                            String allergy = allergySnapshot.getValue(String.class);
                            userAllergies.add(allergy);
                        }

                        // Check the user's allergies
                        for (CheckBox checkBox : checkBoxList) {
                            if (userAllergies.contains(checkBox.getText().toString())) {
                                checkBox.setChecked(true);
                            }
                            allergiesContainer.addView(checkBox);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Failed to load user allergies.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load allergies.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setTitle("Edit Allergies");
        builder.setPositiveButton("Save", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            List<String> updatedAllergies = new ArrayList<>();

            // Iterate through child views of the container
            for (int i = 0; i < allergiesContainer.getChildCount(); i++) {
                View view = allergiesContainer.getChildAt(i);
                if (view instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) view;
                    if (checkBox.isChecked()) {
                        updatedAllergies.add(checkBox.getText().toString());
                    }
                }
            }

            // Update the user's allergies in the database
            userRef.child(currentUser.getUsername()).child("allergies").setValue(updatedAllergies).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    fetchUserData(); // Refresh the data
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Failed to update allergies.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
