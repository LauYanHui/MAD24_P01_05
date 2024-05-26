package sg.edu.np.mad.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListActivity extends AppCompatActivity {
    String testing = "testing";
    ArrayList<Recipe> recipeList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Button test = findViewById(R.id.back);
//        test.setOnClickListener(new View.OnClickListener(){
//            @Override public void onClick(View v){
//                Intent activityName = new Intent(List.this,ListActivity.class);
//                startActivity(activityName);
//            }
//        });

        RecipeAdapter recipeAdapter = new RecipeAdapter(recipeList,this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recipeAdapter);


        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-assignment-8c5d2-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myref = database.getReference("Recipes");
        //DatabaseReference recipe1 = myref.child("1");
        myref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                        Map<String, Object> recipeData = (Map<String, Object>) recipeSnapshot.getValue();

                        String id = (String) recipeData.get("ID");
                        String cuisine = (String) recipeData.get("Cuisine");
                        String mainIngredient = (String) recipeData.get("Main ingredient");
                        String name = (String) recipeData.get("Name");
                        List<String> allergies = (List<String>) recipeData.get("Allergies");
                        List<String> instructions = (List<String>) recipeData.get("Instructions");
                        List<String> ingredients = (List<String>) recipeData.get("Ingredients");
                        Map<String, String> nutritionFacts = (Map<String, String>) recipeData.get("Nutritious facts");

                        // Create a Recipe object
                        Recipe recipe = new Recipe(id, allergies, cuisine, ingredients, instructions, mainIngredient, name, nutritionFacts);
                        recipeList.add(recipe);
                    }
                    Log.i(testing,"recie List Size: " + recipeList.size());
                    recipeAdapter.notifyDataSetChanged();

                    // Log the recipes
                    for (Recipe recipe : recipeList) {
                        Log.i(testing, recipe.toString());
                    }
                } else {
                    Log.i(testing, "Error fetching data");
                }
            }
        });
//        RecipeAdapter recipeAdapter = new RecipeAdapter(recipeList,this);
//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(recipeAdapter);

    }
}