package sg.edu.np.mad.myapplication;
import sg.edu.np.mad.myapplication.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    private CheckBox Gluten, Eggs, Dairy, Fish, Shellfish, Soy, Peanut, Sesame, TreeNut;
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
        RadioButton noAllergy = findViewById(R.id.radiobutton_noAllergy);
        RadioButton ifNotSelect = findViewById(R.id.radiobutton_ifNotSpecify);
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

        noAllergy.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ifNotSelect.setChecked(false);
                //Radio Buttons and CheckBoxes
                Gluten.setVisibility(View.GONE);
                Eggs.setVisibility(View.GONE);
                Dairy.setVisibility(View.GONE);
                Fish.setVisibility(View.GONE);
                Shellfish.setVisibility(View.GONE);
                Soy.setVisibility(View.GONE);
                Peanut.setVisibility(View.GONE);
                Sesame.setVisibility(View.GONE);
                TreeNut.setVisibility(View.GONE);
                //Texts
                GlutenTxt.setVisibility(View.GONE);
                EggsTxt.setVisibility(View.GONE);
                DairyTxt.setVisibility(View.GONE);
                FishTxt.setVisibility(View.GONE);
                ShellfishTxt.setVisibility(View.GONE);
                SoyTxt.setVisibility(View.GONE);
                PeanutTxt.setVisibility(View.GONE);
                SesameTxt.setVisibility(View.GONE);
                TreeNutTxt.setVisibility(View.GONE);

            } else {
                Gluten.setVisibility(View.VISIBLE);
                Eggs.setVisibility(View.VISIBLE);
                Dairy.setVisibility(View.VISIBLE);
                Fish.setVisibility(View.VISIBLE);
                Shellfish.setVisibility(View.VISIBLE);
                Soy.setVisibility(View.VISIBLE);
                Peanut.setVisibility(View.VISIBLE);
                Sesame.setVisibility(View.VISIBLE);
                TreeNut.setVisibility(View.VISIBLE);

                GlutenTxt.setVisibility(View.VISIBLE);
                EggsTxt.setVisibility(View.VISIBLE);
                DairyTxt.setVisibility(View.VISIBLE);
                FishTxt.setVisibility(View.VISIBLE);
                ShellfishTxt.setVisibility(View.VISIBLE);
                SoyTxt.setVisibility(View.VISIBLE);
                PeanutTxt.setVisibility(View.VISIBLE);
                SesameTxt.setVisibility(View.VISIBLE);
                TreeNutTxt.setVisibility(View.VISIBLE);
            }
        });

        ifNotSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                noAllergy.setChecked(false);
            }

        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCheckboxStates();
            }
        });
        Gluten.setOnClickListener(this::onCheckboxClicked);
        Eggs.setOnClickListener(this::onCheckboxClicked);
        Dairy.setOnClickListener(this::onCheckboxClicked);
        Fish.setOnClickListener(this::onCheckboxClicked);
        Shellfish.setOnClickListener(this::onCheckboxClicked);
        Soy.setOnClickListener(this::onCheckboxClicked);
        Peanut.setOnClickListener(this::onCheckboxClicked);
        Sesame.setOnClickListener(this::onCheckboxClicked);
        TreeNut.setOnClickListener(this::onCheckboxClicked);

        submitBTN.setOnClickListener(v -> submitCheckboxStates());
    }
    public void onCheckboxClicked(View view) {
        CheckBox checkBox = (CheckBox) view;
        boolean isChecked = checkBox.isChecked();

        int id = view.getId();

        if (id == R.id.checkBox_Gluten) {
            // Handle Gluten checkbox
            Gluten.setChecked(true);
        }
        else if (id == R.id.checkBox_Eggs) {
            // Handle Eggs checkbox
            Eggs.setChecked(true);
        }
        else if (id == R.id.checkBox_Milk) {
            // Handle Dairy checkbox
            Dairy.setChecked(true);
        }
        else if (id == R.id.checkBox_Fish) {
            // Handle Fish checkbox
            Fish.setChecked(true);
        }
        else if (id == R.id.checkBox_Shellfish) {
            // Handle Shellfish checkbox
            Shellfish.setChecked(true);
        }
        else if (id == R.id.checkBox_Soy) {
            // Handle Soy checkbox
            Soy.setChecked(true);
        }
        else if (id == R.id.checkBox_Peanut) {
            // Handle Peanut checkbox
            Peanut.setChecked(true);
        }
        else if (id == R.id.checkBox_Sesame) {
            // Handle Sesame checkbox
            Sesame.setChecked(true);
        }
        else if (id == R.id.checkBox_TreeNut) {
            // Handle TreeNut checkbox
            TreeNut.setChecked(true);
        }
        else {
            // Handle default case
            Gluten.setChecked(false);
            Eggs.setChecked(false);
            Dairy.setChecked(false);
            Fish.setChecked(false);
            Shellfish.setChecked(false);
            Soy.setChecked(false);
            Peanut.setChecked(false);
            Sesame.setChecked(false);
            TreeNut.setChecked(false);
        }
    }
    private void submitCheckboxStates() {
        Map<String, Boolean> checkboxes = new HashMap<>();
        checkboxes.put("checkbox1", Gluten.isChecked());
        checkboxes.put("checkbox2", Eggs.isChecked());
        checkboxes.put("checkbox3", Dairy.isChecked());
        checkboxes.put("checkbox4", Fish.isChecked());
        checkboxes.put("checkbox5", Shellfish.isChecked());
        checkboxes.put("checkbox6", Soy.isChecked());
        checkboxes.put("checkbox7", Peanut.isChecked());
        checkboxes.put("checkbox8", Sesame.isChecked());
        checkboxes.put("checkbox9", TreeNut.isChecked());

        userRef.setValue(checkboxes)
                .addOnSuccessListener(aVoid -> Toast.makeText(MainActivity.this, "Data submitted successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Failed to submit data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}





