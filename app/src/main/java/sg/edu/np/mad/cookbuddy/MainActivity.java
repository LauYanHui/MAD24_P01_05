package sg.edu.np.mad.cookbuddy;

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

public class MainActivity extends AppCompatActivity {
    String testing = "testing";
    private int currentInstructionIndex = 0;  // Track the current instruction

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

        ImageView backbtn = findViewById(R.id.backtohomeBtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityName = new Intent(MainActivity.this, ListActivity.class);
                startActivity(activityName);
            }
        });

        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("Recipe");

        TextView tvname = findViewById(R.id.recipeName);
        TextView cuisine = findViewById(R.id.cuisine);
        TextView mainIngredient = findViewById(R.id.mainIngredient);
        TextView ingredientsTv = findViewById(R.id.ingredient);
        //TextView instructionsTv = findViewById(R.id.instructions);
        ImageView recipeImage = findViewById(R.id.recipeImage);
        TextView nutritiousFactsTextView = findViewById(R.id.nutritiousFact);
        Map<String, String> nutritiousFacts = recipe.getNutritiousFacts();
        Log.i(testing, "1");
        StringBuilder nutritionInfo = new StringBuilder();
        Log.i(testing, "2");
        for (Map.Entry<String, String> entry : nutritiousFacts.entrySet()) {
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            Log.i("testing", "Key: " + key + ", Value: " + value);
            nutritionInfo.append(key).append(": ").append(value).append("\n");
        }
        Log.i(testing, "3");
        String nutritionText = nutritionInfo.toString();

        List<String> ingredientList = recipe.getIngredients();
        List<String> instructionList = recipe.getInstructions();
        StringBuilder ingredientBuilder = new StringBuilder();
        for (String ingredient : ingredientList) {
            ingredientBuilder.append("• ").append(ingredient).append("\n");
        }
        String ingredientText = ingredientBuilder.toString();

        StringBuilder instructionBuilder = new StringBuilder();
        for (String instructions : instructionList) {
            instructionBuilder.append("- ").append(instructions).append("\n");
        }
        String instructionText = instructionBuilder.toString();

        recipeImage.setImageResource(recipe.getImageResId());
        cuisine.setText(recipe.cuisine);
        mainIngredient.setText(recipe.mainIngredient);
        tvname.setText(recipe.name);
        nutritiousFactsTextView.setText(nutritionText);
        ingredientsTv.setText(ingredientText);


        ViewPager2 instructionsPager = findViewById(R.id.instructionsPager);
        InstructionsPagerAdapter adapter = new InstructionsPagerAdapter(instructionList);
        instructionsPager.setAdapter(adapter);



    }
}
