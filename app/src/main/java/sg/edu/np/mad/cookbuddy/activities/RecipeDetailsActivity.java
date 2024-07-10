package sg.edu.np.mad.cookbuddy.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

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
    private TextView tvIngredients;
    private TextView tvNutrients;
    private ImageView ivRecipeImage;


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

        // handle intent
        Intent intent = getIntent();
        Recipe recipe = intent.getSerializableExtra("Recipe", Recipe.class);
        Map<String, String> nutritiousFacts = recipe.getNutrients();
        List<String> ingredientList = recipe.getIngredients();
        List<String> instructionList = recipe.getInstructions();

        // get views
        tvName = findViewById(R.id.tvRecipeName);
        tvCuisine = findViewById(R.id.tvCuisine);
        tvMainIngredient = findViewById(R.id.tvMainIngredient);
        tvIngredients = findViewById(R.id.tvIngredients);
        ivRecipeImage = findViewById(R.id.ivRecipeImg);
        tvNutrients = findViewById(R.id.tvNutrients);

        // handle nutrient information
        StringBuilder nutritionInfo = new StringBuilder();
        for (Map.Entry<String, String> entry : nutritiousFacts.entrySet()) {
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            nutritionInfo.append(key).append(": ").append(value).append("\n");
        }
        String nutritionText = nutritionInfo.toString();

        // handle ingredient information
        StringBuilder ingredientBuilder = new StringBuilder();
        for (String ingredient : ingredientList) {
            ingredientBuilder.append("• ").append(ingredient).append("\n");
        }
        String ingredientText = ingredientBuilder.toString();

        // set values
        tvName.setText(recipe.getName());
        tvCuisine.setText(recipe.getCuisine());
        tvMainIngredient.setText(recipe.getMainIngredient());
        tvIngredients.setText(ingredientText);
        tvNutrients.setText(nutritionText);
        ivRecipeImage.setImageResource(recipe.getImageResId());

        // handle instructions
        ViewPager2 instructionsPager = findViewById(R.id.pagerInstructions);
        InstructionAdapter adapter = new InstructionAdapter(instructionList);
        instructionsPager.setAdapter(adapter);
    }
}