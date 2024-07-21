//package sg.edu.np.mad.cookbuddy;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import sg.edu.np.mad.cookbuddy.activities.ProfileActivity;
//
//public class HomepageActivity extends AppCompatActivity {
//
//    Intent activityName;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_homepage);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        Intent intent = getIntent();
//        User user = (User) intent.getSerializableExtra("User");
//
//        CardView techniquePage = findViewById(R.id.smallCard3);
//        CardView recipePage = findViewById(R.id.recipes);
//        CardView groceryList = findViewById(R.id.groceryList);
//        CardView profile = findViewById(R.id.smallCard4);
//
//        techniquePage.setOnClickListener(new View.OnClickListener(){
//            @Override public void onClick(View v){
//                activityName = new Intent(HomepageActivity.this, TechniqueRecycler.class);
//                startActivity(activityName);
//            }
//        });
//
//        recipePage.setOnClickListener(new View.OnClickListener(){
//            @Override public void onClick(View v){
//                activityName = new Intent(HomepageActivity.this,ListActivity.class);
//                startActivity(activityName);
//            }
//        });
//        groceryList.setOnClickListener(new View.OnClickListener(){
//            @Override public void onClick(View v){
//                activityName = new Intent(HomepageActivity.this, GroceryList.class);
//                startActivity(activityName);
//            }
//        });
//
//        profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (user != null) {
//                    Log.d("HomepageActivity", "User: " + user.toString());
//                    Bundle extras = new Bundle();
//                    extras.putSerializable("User", user);
//                    activityName = new Intent(HomepageActivity.this, ProfileActivity.class);
//                    activityName.putExtras(extras);
//                    startActivity(activityName);
//                } else {
//                    Log.d("HomepageActivity", "User is null");
//                }
//            }
//        });
//    }
//}