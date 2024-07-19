package sg.edu.np.mad.cookbuddy.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.edu.np.mad.cookbuddy.adapters.InstructionAdapter;
import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.models.Recipe;

public class RecipeDetailsActivity extends AppCompatActivity {
    private ImageView ivBack;
    private TextView tvName;
    private TextView tvCuisine;
    private TextView tvMainIngredient;
    private ImageView ivRecipeImage;
    private ImageView ivFavourite;
    private Button btnInformation;
    private Button btnInstruction;
    private Recipe selectedRecipe;
    private String TAG = "RecipeDetailsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvName = findViewById(R.id.tvRecipeName);
        tvCuisine = findViewById(R.id.tvCuisine);
        tvMainIngredient = findViewById(R.id.tvMainIngredient);
        ivRecipeImage = findViewById(R.id.imageView);
        ivBack = findViewById(R.id.btnBack);
        ivFavourite = findViewById(R.id.btnFavourite);
        btnInstruction = findViewById(R.id.btnInstruction);
        btnInformation = findViewById(R.id.btnInfo);

        // Retrieve selected recipe from intent
        Intent intent = getIntent();
        selectedRecipe = intent.getSerializableExtra("Recipe", Recipe.class);

        // Set data to views
        if (selectedRecipe == null) {
            Toast.makeText(getBaseContext(), "Error fetching recipe", Toast.LENGTH_SHORT).show();
            return;
        }

        tvName.setText(selectedRecipe.getName());
        tvCuisine.setText(selectedRecipe.getCuisine());
        tvMainIngredient.setText(selectedRecipe.getMainIngredient());
        ivRecipeImage.setImageResource(selectedRecipe.getImageResId());

        // get user info
        SharedPreferences sharedPref = getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", null);

        // get data from firebase
        DatabaseReference favouriteRef = FirebaseDatabase.getInstance(HomeActivity.FIREBASE_URL)
                .getReference("Users/" + username + "/favourites/");



        // set favourite icon original state
        favouriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // set favourite icon
                if (snapshot.hasChild(selectedRecipe.getId())) {
                    ivFavourite.setSelected(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "getFavourite:onCancelled", error.toException());
            }
        });

        loadInformationFragment();
        btnInformation.setSelected(true);

        // Back to recipe list button
        ivBack.setOnClickListener(v -> {
            Intent backToListIntent = new Intent(RecipeDetailsActivity.this, HomeActivity.class);
            startActivity(backToListIntent);
        });

        // add/remove favourite in FB when clicked
        ivFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivFavourite.setSelected(!ivFavourite.isSelected());

                // add to FB
                if (ivFavourite.isSelected()) {
                    favouriteRef.child(selectedRecipe.getId()).setValue(true);
                } else {
                    favouriteRef.child(selectedRecipe.getId()).removeValue();
                }
            }
        });

        // Set fragment and selected button
        btnInstruction.setOnClickListener(v -> {
            btnInstruction.setSelected(true);
            btnInformation.setSelected(false);
            loadInstructionFragment();
        });

        btnInformation.setOnClickListener(v -> {
            btnInformation.setSelected(true);
            btnInstruction.setSelected(false);
            loadInformationFragment();
        });
    }

    private void loadInstructionFragment() {
        if (selectedRecipe != null) {
            // Prepare instruction list to pass to InstructionFragment
            List<String> instructionList = selectedRecipe.getInstructions();
            InstructionFragment instructionFragment = InstructionFragment.newInstance(instructionList);

            // Load InstructionFragment
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, instructionFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            Log.e(TAG, "Selected recipe is null");
        }
    }

    private void loadInformationFragment() {
        if (selectedRecipe != null) {
            // Prepare nutrition facts and ingredients to pass to InformationFragment
            StringBuilder nutritionInfo = new StringBuilder();
            for (Map.Entry<String, String> entry : selectedRecipe.getNutritiousFacts().entrySet()) {
                nutritionInfo.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            String nutritionText = nutritionInfo.toString();

            StringBuilder ingredientBuilder = new StringBuilder();
            for (String ingredient : selectedRecipe.getIngredients()) {
                ingredientBuilder.append("• ").append(ingredient).append("\n");
            }
            String ingredientText = ingredientBuilder.toString();

            // Load InformationFragment
            InformationFragment informationFragment = InformationFragment.newInstance(nutritionText, ingredientText);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, informationFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            Log.e(TAG, "Selected recipe is null");
        }
    }

}