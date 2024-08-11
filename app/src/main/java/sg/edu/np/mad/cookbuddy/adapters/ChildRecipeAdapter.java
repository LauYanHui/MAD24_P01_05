package sg.edu.np.mad.cookbuddy.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.activities.RecipeDetailsActivity;
import sg.edu.np.mad.cookbuddy.models.Recipe;

public class ChildRecipeAdapter extends RecyclerView.Adapter<ChildRecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private Context context;
    private FirebaseStorage firebaseStorage;

    public ChildRecipeAdapter(List<Recipe> recipeList, Context context) {
        this.recipeList = recipeList;
        this.context = context;
        this.firebaseStorage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.recipeName.setText(recipe.getName());
        loadImageFromFirebase(recipe.getImageName(), holder.recipeImage);

        // Set up the click listener for the recipe image
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Recipe", recipe); // Pass the recipe object
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    private void loadImageFromFirebase(String imageName, ImageView imageView) {
        StorageReference imageRef = firebaseStorage.getReference().child("Recipe Images/" + imageName);
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(context).load(uri).into(imageView);
        }).addOnFailureListener(exception -> {
            // Handle any errors
        });
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName;
        ImageView recipeImage;

        RecipeViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeImage = itemView.findViewById(R.id.recipeImage);
        }
    }
}
