package sg.edu.np.mad.cookbuddy.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.models.Recipe;
import sg.edu.np.mad.cookbuddy.views.RecipeViewHolder;
import sg.edu.np.mad.cookbuddy.activities.RecipeDetailsActivity;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
    private ArrayList<Recipe> recipeList;
    private Context context;
    public RecipeAdapter(ArrayList<Recipe> recipeList, Context context){
        this.recipeList = recipeList;
        this.context = context;
    }
    @NonNull
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recipe, parent, false);
        return new RecipeViewHolder(view);
    }
    public void onBindViewHolder(RecipeViewHolder holder, int position){
        Recipe listItem = recipeList.get(position);
        Recipe recipe = recipeList.get(position);
        holder.setName(listItem.getName());
        holder.setCuisine(listItem.getCuisine());
        holder.setMainIngredient(listItem.getMainIngredient());
        holder.setImage(recipe.getImageResId());

        holder.getName().setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), RecipeDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Recipe", (Serializable) recipe);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }
    public int getItemCount() {
        return recipeList.size();
    }

}