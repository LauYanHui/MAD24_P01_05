package sg.edu.np.mad.cookbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.adapters.AllergyAdapter;
import sg.edu.np.mad.cookbuddy.models.Allergy;
import sg.edu.np.mad.cookbuddy.models.User;


public class AllergyActivity extends AppCompatActivity {
    private Button btnSubmit;
    private List<String> allergies;
    private AllergyAdapter adapter;
    private ListView lvAllergies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergies);

        btnSubmit = findViewById(R.id.btnSubmit);
        lvAllergies = findViewById(R.id.lvAllergies);
        final String FIREBASE_URL = "https://mad-assignment-8c5d2-default-rtdb.asia-southeast1.firebasedatabase.app/";
        DatabaseReference userRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("Users/");

        Intent intent = getIntent();
        String username = intent.getSerializableExtra("username", String.class);
        String password = intent.getSerializableExtra("password", String.class);

        allergies = new ArrayList<String>();
        allergies.add("Gluten");
        allergies.add("Eggs");
        allergies.add("Milk/Dairy");
        allergies.add("Fish");
        allergies.add("Shellfish");
        allergies.add("Soy");
        allergies.add("Sesame");
        allergies.add("Peanut");
        allergies.add("Tree nuts");

        List<Allergy> allergyList = allergies.stream()
                .map(i -> new Allergy(i, false))
                .collect(Collectors.toList());

        adapter = new AllergyAdapter(this, allergyList);
        lvAllergies.setAdapter(adapter);

//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Push Data to Database
//                userRef.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DataSnapshot> task) {
//                        if (!task.isSuccessful()) {
//                            Toast.makeText(AllergyActivity.this, String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        // check checkbox states
//                        ArrayList<String> allergies = submitCheckboxStates();
//                        User temp = new User(username, password, allergies);
//
//                        // Set data in Firebase
//                        userRef.child(username).setValue(temp);
//                        userRef.child(username).child("allergies").setValue(allergies);
//
//                        // Go homepage
//                        Intent goLogin = new Intent(AllergyActivity.this, LoginActivity.class);
//                        startActivity(goLogin);
//                    }
//                });
//            }
//        });
    }
}







