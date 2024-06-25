package sg.edu.np.mad.cookbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.Serializable;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.models.User;

public class HomeActivity extends AppCompatActivity {

    Intent activityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("User");

        CardView techniquePage = findViewById(R.id.smallCard3);
        CardView recipePage = findViewById(R.id.recipes);
        CardView groceryList = findViewById(R.id.groceryList);
        CardView profile = findViewById(R.id.smallCard4);

        techniquePage.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                activityName = new Intent(HomeActivity.this, TechniqueActivity.class);
                startActivity(activityName);
            }
        });

        recipePage.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                activityName = new Intent(HomeActivity.this, RecipeActivity.class);
                startActivity(activityName);
            }
        });
        groceryList.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                activityName = new Intent(HomeActivity.this, GroceryListActivity.class);
                startActivity(activityName);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pass user info to next activity
                Bundle extras = new Bundle();
                extras.putSerializable("User", (Serializable) user);
                activityName = new Intent(HomeActivity.this, ProfileActivity.class);
                activityName.putExtras(extras);
                startActivity(activityName);
            }
        });
    }
}