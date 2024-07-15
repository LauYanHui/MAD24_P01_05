package sg.edu.np.mad.cookbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
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

import java.util.ArrayList;

public class EditProfilePage extends AppCompatActivity {
    private CheckBox Gluten, Eggs, Dairy, Fish, Shellfish, Soy, Peanut, Sesame, TreeNut;
    private RadioButton noAllergy;
    private Button saveBTN, cancelBTN;
    private EditText newUsername, newPassword, confirmPassword;

    private User user;

    String FIREBASE_URL = "https://mad-assignment-8c5d2-default-rtdb.asia-southeast1.firebasedatabase.app/";
    DatabaseReference userRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("Users/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        newUsername = findViewById(R.id.et_new_username);
        newPassword = findViewById(R.id.et_new_password);
        confirmPassword = findViewById(R.id.et_confirm_password);
        noAllergy = findViewById(R.id.rb_no_allergies);
        Gluten = findViewById(R.id.cb_allergy_gluten);
        Eggs = findViewById(R.id.cb_allergy_egg);
        Dairy = findViewById(R.id.cb_allergy_dairy);
        Fish = findViewById(R.id.cb_allergy_fish);
        Shellfish = findViewById(R.id.cb_allergy_shellfish);
        Soy = findViewById(R.id.cb_allergy_soy);
        Peanut = findViewById(R.id.cb_allergy_nuts);
        Sesame = findViewById(R.id.cb_allergy_sesame);
        TreeNut = findViewById(R.id.cb_allergy_tree_nuts);
        saveBTN = findViewById(R.id.btn_save);
        cancelBTN = findViewById(R.id.btn_cancel);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("User");

        if (user != null) {
            loadUserData();
        }

        noAllergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noAllergy.isChecked()) {
                    setAllergyCheckboxes(false);
                }
            }
        });

        View.OnClickListener allergyCheckboxListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    noAllergy.setChecked(false);
                }
            }
        };

        Gluten.setOnClickListener(allergyCheckboxListener);
        Eggs.setOnClickListener(allergyCheckboxListener);
        Dairy.setOnClickListener(allergyCheckboxListener);
        Fish.setOnClickListener(allergyCheckboxListener);
        Shellfish.setOnClickListener(allergyCheckboxListener);
        Soy.setOnClickListener(allergyCheckboxListener);
        Peanut.setOnClickListener(allergyCheckboxListener);
        Sesame.setOnClickListener(allergyCheckboxListener);
        TreeNut.setOnClickListener(allergyCheckboxListener);

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });

        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfilePage.this, ProfileActivity.class);
                intent.putExtra("User", user); // Ensure you are passing the user object
                startActivity(intent);
            }
        });
    }

    private void loadUserData() {
        userRef.child(user.getUsername()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    User dbUser = dataSnapshot.getValue(User.class);

                    if (dbUser != null) {
                        user = dbUser;
                        updateUIWithUserData();
                    }
                } else {
                    Toast.makeText(EditProfilePage.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUIWithUserData() {
        newUsername.setHint(user.getUsername());
        newPassword.setHint("New Password");

        // Set allergies
        ArrayList<String> allergies = new ArrayList<>(user.getAllergies());
        if (allergies.isEmpty()) {
            noAllergy.setChecked(true);
        } else {
            for (String allergy : allergies) {
                switch (allergy) {
                    case "Gluten": Gluten.setChecked(true); break;
                    case "Eggs": Eggs.setChecked(true); break;
                    case "Dairy": Dairy.setChecked(true); break;
                    case "Fish": Fish.setChecked(true); break;
                    case "Shellfish": Shellfish.setChecked(true); break;
                    case "Soy": Soy.setChecked(true); break;
                    case "Peanut": Peanut.setChecked(true); break;
                    case "Sesame": Sesame.setChecked(true); break;
                    case "TreeNut": TreeNut.setChecked(true); break;
                }
            }
        }
    }

    private void saveUserData() {
        String updatedUsername = newUsername.getText().toString().trim();
        String updatedPassword = newPassword.getText().toString().trim();
        String confirmedPassword = confirmPassword.getText().toString().trim();

        // Validate input
        if (!updatedPassword.equals(confirmedPassword)) {
            Toast.makeText(EditProfilePage.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!updatedUsername.isEmpty() && !user.getUsername().equals(updatedUsername)) {
            user.setUsername(updatedUsername);
        }
        if (!updatedPassword.isEmpty()) {
            user.setPassword(updatedPassword);
        }

        ArrayList<String> updatedAllergies = new ArrayList<>();
        if (!noAllergy.isChecked()) {
            if (Gluten.isChecked()) updatedAllergies.add("Gluten");
            if (Eggs.isChecked()) updatedAllergies.add("Eggs");
            if (Dairy.isChecked()) updatedAllergies.add("Dairy");
            if (Fish.isChecked()) updatedAllergies.add("Fish");
            if (Shellfish.isChecked()) updatedAllergies.add("Shellfish");
            if (Soy.isChecked()) updatedAllergies.add("Soy");
            if (Peanut.isChecked()) updatedAllergies.add("Peanut");
            if (Sesame.isChecked()) updatedAllergies.add("Sesame");
            if (TreeNut.isChecked()) updatedAllergies.add("TreeNut");
        }
        user.setAllergies(updatedAllergies);

        // Save user data to Firebase
        userRef.child(user.getUsername()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditProfilePage.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProfilePage.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAllergyCheckboxes(boolean checked) {
        Gluten.setChecked(checked);
        Eggs.setChecked(checked);
        Dairy.setChecked(checked);
        Fish.setChecked(checked);
        Shellfish.setChecked(checked);
        Soy.setChecked(checked);
        Peanut.setChecked(checked);
        Sesame.setChecked(checked);
        TreeNut.setChecked(checked);
    }
}
