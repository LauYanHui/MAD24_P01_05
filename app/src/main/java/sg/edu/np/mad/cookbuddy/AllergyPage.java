package sg.edu.np.mad.cookbuddy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;


public class AllergyPage extends AppCompatActivity {

    private CheckBox Gluten, Eggs, Dairy, Fish, Shellfish, Soy, Peanut, Sesame, TreeNut;

    private  RadioButton noAllergy, ifNotSelect;
    private Button submitBTN;

    String FIREBASE_URL = "https://mad-assignment-8c5d2-default-rtdb.asia-southeast1.firebasedatabase.app/";
    DatabaseReference userRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("Users/");



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Radio Buttons and CheckBoxes
        submitBTN = findViewById(R.id.button_Submit);
        noAllergy = findViewById(R.id.radiobutton_noAllergy);
        ifNotSelect = findViewById(R.id.radiobutton_ifNotSpecify);
        Gluten = findViewById(R.id.checkBox_Gluten);
        Eggs = findViewById(R.id.checkBox_Eggs);
        Dairy = findViewById(R.id.checkBox_Milk);
        Fish = findViewById(R.id.checkBox_Fish);
        Shellfish = findViewById(R.id.checkBox_Shellfish);
        Soy = findViewById(R.id.checkBox_Soy);
        Peanut = findViewById(R.id.checkBox_Peanut);
        Sesame = findViewById(R.id.checkBox_Sesame);
        TreeNut = findViewById(R.id.checkBox_TreeNut);

        //Texts
        TextView ifNotSelectTxt = findViewById(R.id.textView_ifNotSelect);
        TextView GlutenTxt = findViewById(R.id.textView_Gluten);
        TextView EggsTxt = findViewById(R.id.textView_Eggs);
        TextView DairyTxt = findViewById(R.id.textView_Milk);
        TextView FishTxt = findViewById(R.id.textView_Fish);
        TextView ShellfishTxt = findViewById(R.id.textView_Shellfish);
        TextView SoyTxt = findViewById(R.id.textView_Soy);
        TextView PeanutTxt = findViewById(R.id.textView_Peanut);
        TextView SesameTxt = findViewById(R.id.textView_Sesame);
        TextView TreeNutTxt = findViewById(R.id.textView_TreeNut);

        //Set On Click For noAllergy Button
        noAllergy.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ifNotSelect.setChecked(false);
                // Hide checkboxes and texts
                setVisibilityForAllergies(View.GONE);
            } else {
                // Show checkboxes and texts
                setVisibilityForAllergies(View.VISIBLE);
            }
        });

        //Set On Click For ifNotSelect Button
        ifNotSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                noAllergy.setChecked(false);
            }
        });

        //For submit button to check checkbox states
        submitBTN.setOnClickListener(v -> submitCheckboxStates());
    }

    //Visibility to hide checkboxes if NoAllergy selected
    private void setVisibilityForAllergies(int visibility) {
        Gluten.setVisibility(visibility);
        Eggs.setVisibility(visibility);
        Dairy.setVisibility(visibility);
        Fish.setVisibility(visibility);
        Shellfish.setVisibility(visibility);
        Soy.setVisibility(visibility);
        Peanut.setVisibility(visibility);
        Sesame.setVisibility(visibility);
        TreeNut.setVisibility(visibility);

        findViewById(R.id.textView_Gluten).setVisibility(visibility);
        findViewById(R.id.textView_Eggs).setVisibility(visibility);
        findViewById(R.id.textView_Milk).setVisibility(visibility);
        findViewById(R.id.textView_Fish).setVisibility(visibility);
        findViewById(R.id.textView_Shellfish).setVisibility(visibility);
        findViewById(R.id.textView_Soy).setVisibility(visibility);
        findViewById(R.id.textView_Peanut).setVisibility(visibility);
        findViewById(R.id.textView_Sesame).setVisibility(visibility);
        findViewById(R.id.textView_TreeNut).setVisibility(visibility);
    }
    public void onCheckboxClicked(View view) {
        CheckBox checkBox = (CheckBox) view;
        boolean isChecked = checkBox.isChecked();
    }

    //Store Data as a List
    private void submitCheckboxStates() {
        List<String> allergies = new ArrayList<>();

        if (noAllergy.isChecked()) {
            allergies.add("None");
        } else {
            if (Gluten.isChecked()) {
                allergies.add("Gluten");
            }
            if (Eggs.isChecked()) {
                allergies.add("Eggs");
            }
            if (Dairy.isChecked()) {
                allergies.add("Dairy");
            }
            if (Fish.isChecked()) {
                allergies.add("Fish");
            }
            if (Shellfish.isChecked()) {
                allergies.add("Shellfish");
            }
            if (Soy.isChecked()) {
                allergies.add("Soy");
            }
            if (Peanut.isChecked()) {
                allergies.add("Peanut");
            }
            if (Sesame.isChecked()) {
                allergies.add("Sesame");
            }
            if (TreeNut.isChecked()) {
                allergies.add("TreeNut");
            }
        }

        String userId = userRef.push().getKey();

        //Push Data to Database
        if (userId != null) {
            // Set the data at the path corresponding to the new user ID
            userRef.child(userId).child("Allergies").setValue(allergies)
                    .addOnSuccessListener(aVoid -> Toast.makeText(AllergyPage.this, "Data submitted successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(AllergyPage.this, "Failed to submit data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(AllergyPage.this, "Failed to generate user ID", Toast.LENGTH_SHORT).show();
        }
    }
}







