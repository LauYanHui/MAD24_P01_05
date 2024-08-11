package sg.edu.np.mad.cookbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import sg.edu.np.mad.cookbuddy.adapters.AllergyAdapter;
import sg.edu.np.mad.cookbuddy.models.Allergy;
import sg.edu.np.mad.cookbuddy.models.User;

public class AllergyActivity extends AppCompatActivity {

    private Button btnSubmit;
    private List<Allergy> allergyList = new ArrayList<>();
    private AllergyAdapter adapter;
    private ListView lvAllergies;
    private final String FIREBASE_URL = "https://mad-assignment-8c5d2-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private static final String TAG = "AllergyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergies);

        DatabaseReference userRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("Users");
        DatabaseReference allergyRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("Allergies");

        btnSubmit = findViewById(R.id.btnSubmit);
        lvAllergies = findViewById(R.id.lvAllergies);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

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
                ArrayList<String> checkedAllergies = new ArrayList<>();

                for (int i = 0; i < allergyList.size(); i++) {
                    Allergy allergy = allergyList.get(i);
                    if (allergy.isChecked()) {
                        checkedAllergies.add(allergy.getName());
                    }
                }

                Log.d(TAG, "Checked Allergies: " + checkedAllergies);

                User user = new User(username, password, checkedAllergies);
                Log.d(TAG, "User: " + user.getUsername() + ", Allergies: " + user.getAllergies());

                assert username != null;
                userRef.child(username).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User data updated successfully");
                            Toast.makeText(AllergyActivity.this, "User data updated successfully", Toast.LENGTH_SHORT).show();
                            Intent goLogin = new Intent(AllergyActivity.this, LoginActivity.class);
                            startActivity(goLogin);
                        } else {
                            Log.e(TAG, "Failed to update user data", task.getException());
                            Toast.makeText(AllergyActivity.this, "Failed to update user data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
