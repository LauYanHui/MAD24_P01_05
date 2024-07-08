package sg.edu.np.mad.cookbuddy;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String testing = "testing";
    private boolean isStarFilled = false;
    private Recipe selectedRecipe;

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

        ImageView favouriteIcon = findViewById(R.id.favouriteIcon);
        favouriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStarFilled){
                    favouriteIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.star));
                }else{
                    favouriteIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.yellowstar));
                }
                isStarFilled = !isStarFilled;
            }
        });


        // Retrieve selected recipe from intent
        Intent intent = getIntent();
        selectedRecipe = (Recipe) intent.getSerializableExtra("Recipe");

        // Initialize views
        TextView tvname = findViewById(R.id.recipeName);
        TextView cuisine = findViewById(R.id.cuisine);
        TextView mainIngredient = findViewById(R.id.mainIngredient);
        ImageView recipeImage = findViewById(R.id.recipeImage);

        // Set data to views
        if (selectedRecipe != null) {
            tvname.setText(selectedRecipe.getName());
            cuisine.setText(selectedRecipe.getCuisine());
            mainIngredient.setText(selectedRecipe.getMainIngredient());
            recipeImage.setImageResource(selectedRecipe.getImageResId());
        }
        loadInformationFragment();

        // Back to recipe list button
        ImageView backbtn = findViewById(R.id.backtohomeBtn);
        backbtn.setOnClickListener(v -> {
            Intent backToListIntent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(backToListIntent);
        });

        // Load instruction fragment button
        Button instructionBtn = findViewById(R.id.InstructionBtn);
        instructionBtn.setOnClickListener(v -> loadInstructionFragment());

        // Load information fragment button
        Button informationBtn = findViewById(R.id.InformationBtn);
        informationBtn.setOnClickListener(v -> loadInformationFragment());
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
            Log.e(testing, "Selected recipe is null");
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
            Log.e(testing, "Selected recipe is null");
        }
    }

}
