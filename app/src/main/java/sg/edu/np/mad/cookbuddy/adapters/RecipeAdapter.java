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

import java.util.ArrayList;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.activities.RecipeDetailsActivity;
import sg.edu.np.mad.cookbuddy.models.Recipe;
import sg.edu.np.mad.cookbuddy.views.RecipeViewHolder;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
    private ArrayList<Recipe> recipeList;
    private Context context;

    public RecipeAdapter(ArrayList<Recipe> recipeList, Context context) {
        this.recipeList = recipeList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe listItem = recipeList.get(position);
        Log.i("RecipeAdapter", "onBindViewHolder called for position: " + position + ", Recipe name: " + listItem.getName());

        holder.getName().setText(listItem.getName());
        holder.getCuisine().setText(listItem.getCuisine());
        holder.getMainIngredient().setText(listItem.getMainIngredient());
        holder.getImage().setImageResource(listItem.getImageResId());

        holder.getName().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("RecipeAdapter", "Item clicked: " + listItem.getName());

                Intent intent = new Intent(v.getContext(), RecipeDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Recipe", listItem);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void updateRecipeList(ArrayList<Recipe> newRecipeList) {
        recipeList = newRecipeList;
        notifyDataSetChanged();
    }


}