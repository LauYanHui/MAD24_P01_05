package sg.edu.np.mad.cookbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
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
    private List<Allergy> allergyList = new ArrayList<>();
    private AllergyAdapter adapter;
    private ListView lvAllergies;
    final String FIREBASE_URL = "https://mad-assignment-8c5d2-default-rtdb.asia-southeast1.firebasedatabase.app/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergies);

        DatabaseReference userRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("Users/");
        DatabaseReference allergyRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("Allergies");

        btnSubmit = findViewById(R.id.btnSubmit);
        lvAllergies = findViewById(R.id.lvAllergies);

        Intent intent = getIntent();
        String username = intent.getSerializableExtra("username", String.class);
        String password = intent.getSerializableExtra("password", String.class);

        allergyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot temp : snapshot.getChildren()) {
                    allergyList.add(new Allergy(temp.getValue(String.class), false));
                }

                adapter = new AllergyAdapter(getApplicationContext(), allergyList);
                lvAllergies.setAdapter(adapter);
                lvAllergies.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> checkedAllergies = new ArrayList<>();

                for (int i = 0; i < allergyList.size(); i++) {
                    Allergy allergy = allergyList.get(i);
                    if (allergy.isChecked()) {
                        checkedAllergies.add(allergy.getName());
                    }
                }

                User user = new User(username, password, checkedAllergies);
                assert username != null;
                userRef.child(username).setValue(user);

                Intent goLogin = new Intent(AllergyActivity.this, LoginActivity.class);
                startActivity(goLogin);
            }
        });
    }
}







