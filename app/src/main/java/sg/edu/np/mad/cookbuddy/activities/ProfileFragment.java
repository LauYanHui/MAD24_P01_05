package sg.edu.np.mad.cookbuddy.activities;


import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.models.Recipe;
import sg.edu.np.mad.cookbuddy.models.User;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sg.edu.np.mad.cookbuddy.R;

public class ProfileFragment extends Fragment {

    private final static String TAG = "ProfileFragment";
    private Button btnSignOut;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference userRef
    private  DatabaseReference recipeRef;
    private TextView tvUsername;
    private User currentUser;
    private LinearLayout bookmarkedTechniquesLayout;
    private LinearLayout favoriteRecipesLayout;
    private LinearLayout allergiesLayout;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser(); // get cached user

            // user signed out
            if (user == null) {
                Log.d(TAG, "signOut:success");
                startActivity(new Intent(getContext(), LoginActivity.class));
                Toast.makeText(getContext(), "Signed Out", Toast.LENGTH_SHORT).show();
            }
        };
    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSignOut = view.findViewById(R.id.btnSignOut);

        btnSignOut.setOnClickListener(v -> {
            mAuth.signOut();
        });
      
        // Initialize Firebase reference
        String FIREBASE_URL = "https://mad-assignment-8c5d2-default-rtdb.asia-southeast1.firebasedatabase.app/";
        userRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("Users");
        recipeRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("Recipes");

        // Initialize UI elements
        Button btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        Button btnEditAllergies = view.findViewById(R.id.btn_edit_allergies);
        tvUsername = view.findViewById(R.id.username);
  
        // bookmarkedTechniquesLayout = view.findViewById(R.id.bookmarked_techniques_container);
        favoriteRecipesLayout = view.findViewById(R.id.favorite_recipes_container);
        allergiesLayout = view.findViewById(R.id.allergies_container);

        Button btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> logOut());

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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void fetchUserData() {
        userRef.child(currentUser.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Clear existing views
                    allergiesLayout.removeAllViews();
                    favoriteRecipesLayout.removeAllViews();

                    // Fetch and display allergies
                    for (DataSnapshot allergySnapshot : snapshot.child("allergies").getChildren()) {
                        String allergy = allergySnapshot.getValue(String.class);
                        addAllergy(allergy);
                    }

                    // Fetch and display favourite recipes
                    for (DataSnapshot recipeSnapshot : snapshot.child("favourites").getChildren()) {
                        String key = recipeSnapshot.getKey();
                        recipeRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String name = snapshot.child("Name").getValue(String.class);
                                addFavoriteRecipe(name);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

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

    private void addFavoriteRecipe(String recipe) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        textView.setText(recipe);

        layout.addView(textView);

        favoriteRecipesLayout.addView(layout);
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

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
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

            // Check if the new username already exists
            userRef.child(newUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // New username already exists
                        etNewUsername.setError("Username is already taken");
                    } else {
                        // Proceed with updating username and password
                        updateUserDetails(newUsername, newPassword);
                        dialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to check username availability.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void updateUserDetails(String newUsername, String newPassword) {
        DatabaseReference oldUserRef = userRef.child(currentUser.getUsername());
        DatabaseReference newUserRef = userRef.child(newUsername);

        // Prepare updates
        Map<String, Object> updates = new HashMap<>();
        updates.put("username", newUsername);
        if (!TextUtils.isEmpty(newPassword)) {
            updates.put("password", newPassword);
        }

        // Copy existing data to new username
        oldUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, Object> userData = (Map<String, Object>) snapshot.getValue();
                    userData.put("username", newUsername); // Ensure the username is updated
                    userData.put("password", newPassword);

                    // Save data under new username
                    newUserRef.setValue(userData).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Remove old user data
                            oldUserRef.removeValue().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    // Update local user object and refresh data
                                    currentUser.setUsername(newUsername);
                                    updateUsernameTextView(newUsername);
                                    fetchUserData(); // Refresh the data
                                    Toast.makeText(getContext(), "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Failed to remove old user data.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Failed to save new user data.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Old user data not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to read old user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateUsernameTextView(String newUsername) {
        if (tvUsername != null) {
            tvUsername.setText(newUsername);
        }
    }
    private void logOut() {
        // Clear shared preferences or any stored user data
        SharedPreferences sharedPref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();

        // Redirect to LoginActivity
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish(); // Optionally finish the current activity to prevent returning to it
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

