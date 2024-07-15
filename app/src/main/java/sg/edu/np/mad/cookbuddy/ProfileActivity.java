package sg.edu.np.mad.cookbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("User");
        if (user != null) {
            Log.d("ProfileActivity", "User: " + user.toString());
        } else {
            Log.d("ProfileActivity", "User is null");
            Toast.makeText(this, "User data not available", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if user data is not available
            return;
        }

        // Your existing code for setting up the views
        Button ivBack = findViewById(R.id.btn_back);
        Button btnEditProfile = findViewById(R.id.btn_edit_profile);
        TextView tvUsername = findViewById(R.id.username);
        TextView tvAllergiesList = findViewById(R.id.food_allergies_list);
        TextView tvFavoritesList = findViewById(R.id.favorite_recipes_list);

        tvUsername.setText(user.getUsername());
        tvAllergiesList.setText(formatList(user.getAllergies()));
        tvFavoritesList.setText(formatList(user.getFavorites()));

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goLogin = new Intent(ProfileActivity.this, EditProfilePage.class);
                startActivity(goLogin);
            }
        });

        // Back Button
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHome = new Intent(ProfileActivity.this, HomepageActivity.class);
                startActivity(goHome);
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
}
