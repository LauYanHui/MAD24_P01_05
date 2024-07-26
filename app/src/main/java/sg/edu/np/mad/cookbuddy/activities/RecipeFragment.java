package sg.edu.np.mad.cookbuddy.activities;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.api.client.util.Lists;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.protobuf.ByteString;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.widget.NestedScrollView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.adapters.CuisineAdapter;
import sg.edu.np.mad.cookbuddy.adapters.MainIngredientAdapter;
import sg.edu.np.mad.cookbuddy.adapters.RecipeAdapter;
import sg.edu.np.mad.cookbuddy.models.Recipe;

public class RecipeFragment extends Fragment {

    private static final String TAG = "RecipeFragment";
    private ArrayList<Recipe> recipeList = new ArrayList<>();
    private ArrayList<String> mainIngredientList = new ArrayList<>();
    private ArrayList<String> cuisineList = new ArrayList<>();

    private ArrayList<Recipe> filteredList = new ArrayList<>();
    private Map<String, List<Recipe>> recipeMap = new HashMap<>();
    private RecipeAdapter recipeAdapter;

    private static final String API_KEY = "AIzaSyBXzZRrpRt3GYcxnr9FbyemHOxC2_fxyrc";

    private Uri photoURI;

    private ActivityResultLauncher<Intent> selectPictureLauncher;
    private MainIngredientAdapter mainIngredientAdapter;
    private CuisineAdapter cuisineAdapter;
    private ConstraintLayout constraintLayout;

    public RecipeFragment() {
        // Required empty public constructor
    }

    public static RecipeFragment newInstance() {
        return new RecipeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectPictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                                Log.e(TAG, "I am searching For Image");
                                detectFoodItems(bitmap);
                            } catch (IOException e) {
                                Log.e(TAG, "Error getting bitmap from selectedImageUri: " + e.getMessage());
                            }
                        }
                    }
                }
        );
        if (getActivity() != null) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        RecyclerView mainIngredientRecyclerView = view.findViewById(R.id.mainIngredientRecyclerView);
        RecyclerView cuisineRecyclerView = view.findViewById(R.id.cuisineRecyclerView);
        RecyclerView recipeRecyclerView = view.findViewById(R.id.recyclerView);
        ImageView camera_btn = view.findViewById(R.id.camera_icon);
        NestedScrollView nestedScrollView = view.findViewById(R.id.nestedScrollView);

        mainIngredientAdapter = new MainIngredientAdapter(getContext(), mainIngredientList, recipeMap);
        mainIngredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mainIngredientRecyclerView.setAdapter(mainIngredientAdapter);

        recipeAdapter = new RecipeAdapter(new ArrayList<>(), getContext());
        cuisineAdapter = new CuisineAdapter(cuisineList, getContext(), new CuisineAdapter.OnCuisineClickListener() {
            @Override
            public void onCuisineClick(String cuisine) {
                filterByCuisine(cuisine);
            }
        });

        LinearLayoutManager cuisineLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        cuisineRecyclerView.setLayoutManager(cuisineLayoutManager);
        cuisineRecyclerView.setAdapter(cuisineAdapter);

        // Add snapping to the cuisine RecyclerView
        LinearSnapHelper snapHelperCuisine = new LinearSnapHelper();
        snapHelperCuisine.attachToRecyclerView(cuisineRecyclerView);

        LinearLayoutManager recipeLayoutManager = new LinearLayoutManager(getContext());
        recipeRecyclerView.setLayoutManager(recipeLayoutManager);
        recipeRecyclerView.setNestedScrollingEnabled(true);
        recipeRecyclerView.setAdapter(recipeAdapter);

        // Add snapping to the recipe RecyclerView
        LinearSnapHelper snapHelperRecipe = new LinearSnapHelper();
        snapHelperRecipe.attachToRecyclerView(recipeRecyclerView);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-assignment-8c5d2-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Recipes");

        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot != null) {
                        recipeList.clear();
                        mainIngredientList.clear();
                        cuisineList.clear();
                        cuisineList.add("All Recipes");
                        recipeMap.clear();

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
                                boolean favourite = false;
                                Recipe recipe = new Recipe(id, imageName, allergies, cuisine, ingredients, instructions, mainIngredient, name, nutritionFacts, favourite);
                                recipeList.add(recipe);

                                if (!mainIngredientList.contains(mainIngredient)) {
                                    mainIngredientList.add(mainIngredient);
                                }

                                if (!recipeMap.containsKey(mainIngredient)) {
                                    recipeMap.put(mainIngredient, new ArrayList<>());
                                }
                                recipeMap.get(mainIngredient).add(recipe);

                                if (!cuisineList.contains(cuisine)) {
                                    cuisineList.add(cuisine);
                                }

                                Log.i(TAG, "Recipe List Size: " + recipeList.size());
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing recipe data: " + e.getMessage());
                            }
                        }

                        cuisineAdapter.notifyDataSetChanged();
                        mainIngredientAdapter.notifyDataSetChanged();
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
        constraintLayout = view.findViewById(R.id.banner_container);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuisineRecyclerView.setVisibility(View.VISIBLE);
            }
        });
//        searchInput.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mainIngredientRecyclerView.setVisibility(View.GONE);
//            }
//        });
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
                if (charSequence.toString().isEmpty()) {
                    // Show mainIngredientRecyclerView and hide recipeRecyclerView
                    mainIngredientRecyclerView.setVisibility(View.VISIBLE);
                    camera_btn.setVisibility(View.GONE);
                    recipeRecyclerView.setVisibility(View.GONE);
                    cuisineRecyclerView.setVisibility(View.GONE);
                } else {
                    // Perform filtering and show recipeRecyclerView
                    filter(charSequence.toString());
                    mainIngredientRecyclerView.setVisibility(View.GONE);
                    camera_btn.setVisibility(View.VISIBLE);
                    recipeRecyclerView.setVisibility(View.VISIBLE);
                    cuisineRecyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Do nothing
            }
        });
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileExplorer();
            }
        });

        return view;
    }

    private void filterByCuisine(String cuisine) {
        filteredList.clear();
        if (cuisine.equals("All Recipes")) {
            filteredList.addAll(recipeList);
        } else {
            for (Recipe recipe : recipeList) {
                if (recipe.getCuisine().equalsIgnoreCase(cuisine)) {
                    filteredList.add(recipe);
                }
            }
        }
        recipeAdapter.updateRecipeList(filteredList);
        Log.d(TAG, "Filtered by cuisine: " + cuisine + ", filtered list size: " + filteredList.size());
    }
    private void filterByIngredients(List<String> detectedIngredients) {
        filteredList.clear();
        for (Recipe recipe : recipeList) {
            boolean ingredientMatched = false;
            for (String ingredient : detectedIngredients) {
                for (String recipeIngredient : recipe.getIngredients()) {
                    // Normalize both detected ingredient and recipe ingredient for comparison
                    if (recipeIngredient.toLowerCase().contains(ingredient.toLowerCase())) {
                        filteredList.add(recipe);
                        ingredientMatched = true;
                        break; // Break to avoid adding the same recipe multiple times
                    }
                }
                if (ingredientMatched) {
                    break;
                }
            }
        }
        recipeAdapter.updateRecipeList(filteredList);
        Log.d(TAG, "Filtered by ingredients: " + detectedIngredients + ", filtered list size: " + filteredList.size());
    }

    private void filter(String text) {
        ArrayList<Recipe> filteredList = new ArrayList<>();
        for (Recipe recipe : recipeList) {
            if (recipe.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(recipe);
            }
        }
        recipeAdapter.updateRecipeList(filteredList);
        Log.d(TAG, "Filter applied, filtered list size: " + filteredList.size());
    }

    private void openFileExplorer() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        selectPictureLauncher.launch(intent);
    }

    private void detectFoodItems(Bitmap bitmap) {
        new DetectFoodItemsTask().execute(bitmap);
    }

    private class DetectFoodItemsTask extends AsyncTask<Bitmap, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Bitmap... bitmaps) {
            Bitmap bitmap = bitmaps[0];
            try {
                byte[] imageBytes = bitmapToByteArray(bitmap);
                String base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                JSONObject requestPayload = new JSONObject();
                JSONArray requests = new JSONArray();
                JSONObject requestObject = new JSONObject();
                JSONObject imageObject = new JSONObject();
                imageObject.put("content", base64Image);
                requestObject.put("image", imageObject);
                JSONArray features = new JSONArray();
                JSONObject feature = new JSONObject();
                feature.put("type", "LABEL_DETECTION");
                features.put(feature);
                requestObject.put("features", features);
                requests.put(requestObject);
                requestPayload.put("requests", requests);

                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestPayload.toString());
                Request request = new Request.Builder()
                        .url("https://vision.googleapis.com/v1/images:annotate?key=" + API_KEY)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    Log.e(TAG, "API request failed: " + response);
                    return new ArrayList<>();
                }

                String responseBody = response.body().string();
                JSONObject responseJson = new JSONObject(responseBody);
                JSONArray responsesArray = responseJson.getJSONArray("responses");
                List<String> detectedItems = new ArrayList<>();

                for (int i = 0; i < responsesArray.length(); i++) {
                    JSONObject responseObj = responsesArray.getJSONObject(i);
                    JSONArray labelAnnotations = responseObj.getJSONArray("labelAnnotations");

                    for (int j = 0; j < labelAnnotations.length(); j++) {
                        JSONObject annotation = labelAnnotations.getJSONObject(j);
                        String description = annotation.getString("description");
                        detectedItems.add(description);
                    }
                }
                return detectedItems;

            } catch (Exception e) {
                Log.e(TAG, "Failed to detect food items: " + e.getMessage(), e);
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(List<String> detectedItems) {
            super.onPostExecute(detectedItems);
            for (String item : detectedItems) {
                Log.i(TAG, "Detected food item: " + item);
            }
            filterByIngredients(detectedItems); // Call the new filter method
        }
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}
