package sg.edu.np.mad.cookbuddy.activities;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.adapters.CuisineAdapter;
import sg.edu.np.mad.cookbuddy.adapters.RecipeAdapter;
import sg.edu.np.mad.cookbuddy.models.Recipe;

public class RecipeFragment extends Fragment {

    private static final String TAG = "ListActivity";
    private ArrayList<Recipe> recipeList = new ArrayList<>();
    private ArrayList<Recipe> filteredRecipeList = new ArrayList<>();
    private ArrayList<String> cuisineList = new ArrayList<>();
    private RecipeAdapter recipeAdapter;
    private CuisineAdapter cuisineAdapter;

    private StorageReference storageReference;

    public RecipeFragment() {
        // Required empty public constructor
    }

    public static RecipeFragment newInstance() {
        return new RecipeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        RecyclerView cuisineRecyclerView = view.findViewById(R.id.cuisineRecyclerView);
        RecyclerView recipeRecyclerView = view.findViewById(R.id.recyclerView);

        recipeAdapter = new RecipeAdapter(filteredRecipeList, getContext());
        cuisineAdapter = new CuisineAdapter(cuisineList, getContext(), new CuisineAdapter.OnCuisineClickListener() {
            @Override
            public void onCuisineClick(String cuisine) {
                filterByCuisine(cuisine);
            }
        });

        cuisineRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        cuisineRecyclerView.setAdapter(cuisineAdapter);

        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeRecyclerView.setNestedScrollingEnabled(true);
        recipeRecyclerView.setAdapter(recipeAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-assignment-8c5d2-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Recipes");

        //FirebaseStorage storage = FirebaseStorage.getInstance("https://console.firebase.google.com/project/mad-assignment-8c5d2/storage/mad-assignment-8c5d2.appspot.com/files");
        //storageReference = storage.getReference();

        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot != null) {
                        recipeList.clear();
                        cuisineList.clear();
                        cuisineList.add("All Recipes");

                        for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                            try {
                                Map<String, Object> recipeData = (Map<String, Object>) recipeSnapshot.getValue();
                                String id = (String) recipeData.get("ID");
                                String cuisine = (String) recipeData.get("Cuisine");
                                String mainIngredient = (String) recipeData.get("Main ingredient");
                                String name = (String) recipeData.get("Name");
                                List<String> allergies = (List<String>) recipeData.get("Allergies");
                                List<String> instructions = (List<String>) recipeData.get("Instructions");
                                List<String> ingredients = (List<String>) recipeData.get("Ingredients");

                                Map<String, String> nutritionFacts = new HashMap<>();
                                Map<String, Object> nutritionFactsData = (Map<String, Object>) recipeData.get("Nutritious facts");
                                if (nutritionFactsData != null) {
                                    for (Map.Entry<String, Object> entry : nutritionFactsData.entrySet()) {
                                        nutritionFacts.put(entry.getKey(), String.valueOf(entry.getValue()));
                                    }
                                }

                                String imageName = (String) recipeData.get("Image");
                                Log.i("Ezreal",imageName);
                                //int imageResId = getResources().getIdentifier(imageName, "drawable", HomeActivity.PACKAGE_NAME);

                                boolean favourite = false;
                                Recipe recipe = new Recipe(id, imageName, allergies, cuisine, ingredients, instructions, mainIngredient, name, nutritionFacts, favourite);
                                recipeList.add(recipe);

                                if (!cuisineList.contains(cuisine)) {
                                    cuisineList.add(cuisine);
                                }

                                Log.i(TAG, "Recipe List Size: " + recipeList.size());
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing recipe data: " + e.getMessage());
                            }
                        }

                        cuisineAdapter.notifyDataSetChanged();
                        filterByCuisine("All Recipes");
                    } else {
                        Log.e(TAG, "Data snapshot is null");
                    }
                } else {
                    Log.e(TAG, "Error getting data from Firebase: " + task.getException());
                }
            }
        });

        EditText searchInput = view.findViewById(R.id.searchInput);
        ImageView searchIcon = view.findViewById(R.id.searchIcon);

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchInput.getText().toString();
                Log.d(TAG, "Search Icon clicked, search text: " + searchText);
                filter(searchText);
                searchInput.setText("");
            }
        });

        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String searchText = searchInput.getText().toString();
                Log.d(TAG, "IME_ACTION_SEARCH triggered, search text: " + searchText);
                filter(searchText);
                InputMethodManager imm = getSystemService(requireActivity().getApplicationContext(), InputMethodManager.class);
                imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
                return true;
            }
            return false;
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "Text changed: " + charSequence.toString());
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Do nothing
            }
        });
        return view;
    }

    private void filterByCuisine(String cuisine) {
        filteredRecipeList.clear();
        if (cuisine.equals("All Recipes")) {
            filteredRecipeList.addAll(recipeList);
        } else {
            for (Recipe recipe : recipeList) {
                if (recipe.getCuisine().equalsIgnoreCase(cuisine)) {
                    filteredRecipeList.add(recipe);
                }
            }
        }
        recipeAdapter.updateRecipeList(filteredRecipeList);
        Log.d(TAG, "Filtered by cuisine: " + cuisine + ", filtered list size: " + filteredRecipeList.size());
    }

    private void filter(String text) {
        filteredRecipeList.clear();
        for (Recipe recipe : recipeList) {
            if (recipe.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredRecipeList.add(recipe);
            }
        }
        recipeAdapter.updateRecipeList(filteredRecipeList);
        Log.d(TAG, "Filter applied, filtered list size: " + filteredRecipeList.size());
    }
    private void loadImage(String imageName, ImageView imageView) {
        StorageReference imageRef = storageReference.child("Recipe Images/" + imageName);
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(this).load(uri).into(imageView);
        }).addOnFailureListener(exception -> {
            Log.e(TAG, "Error getting image URL: " + exception.getMessage());
        });
    }
}