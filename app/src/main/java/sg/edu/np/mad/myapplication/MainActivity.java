package sg.edu.np.mad.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String testing = "testing";
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
        ImageView backbtn = findViewById(R.id.backBtn);
        //Button test = findViewById(R.id.back);
        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                Intent activityName = new Intent(MainActivity.this,ListActivity.class);
                startActivity(activityName);
            }
        });
        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("Recipe");
        TextView tvname = findViewById(R.id.recipeName);
        TextView cuisine = findViewById(R.id.cuisine);
        TextView mainIngredient = findViewById(R.id.mainIngredient);
        TextView ingredientsTv = findViewById(R.id.ingredients);
        TextView instructionsTv = findViewById(R.id.instructions);

        //text view for nutritious facts
        TextView nutritiousFactsTextView = findViewById(R.id.nutritiousFacts);
        Map<String, String> nutritiousFacts = recipe.getNutritiousFacts();
        Log.i(testing,"1");
        StringBuilder nutritionInfo = new StringBuilder();
        Log.i(testing,"2");
        for (Map.Entry<String, String> entry : nutritiousFacts.entrySet()) {
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            Log.i("testing", "Key: " + key + ", Value: " + value);
            nutritionInfo.append(key).append(": ").append(value).append("\n");
        }
        Log.i(testing,"3");
        String nutritionText = nutritionInfo.toString();
        // text view for ingredients
        List<String> ingredientList = recipe.getIngredients();
        List<String> instructionList = recipe.getInstructions();
        StringBuilder ingredientBuilder = new StringBuilder();
        for (String ingredient : ingredientList) {
            ingredientBuilder.append("- ").append(ingredient).append("\n");
        }
        String ingredientText = ingredientBuilder.toString();

        StringBuilder instructionBuilder = new StringBuilder();
        for(String instructions : instructionList){
            instructionBuilder.append("- ").append(instructions).append("\n");
        }
        String instructionText = instructionBuilder.toString();


        cuisine.setText(recipe.cuisine);
        mainIngredient.setText(recipe.mainIngredient);
        tvname.setText(recipe.name);
        nutritiousFactsTextView.setText(nutritionText);
        ingredientsTv.setText(ingredientText);
        instructionsTv.setText(instructionText);

    }
}