package sg.edu.np.mad.cookbuddy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
    private ArrayList<Recipe> recipeList;
    private Context context;
    public RecipeAdapter(ArrayList<Recipe> recipeList, Context context){
        this.recipeList = recipeList;
        this.context = context;
    }
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_activity_list, parent, false);
        RecipeViewHolder holder = new RecipeViewHolder(view);
        return holder;
    }
    public void onBindViewHolder(RecipeViewHolder holder, int position){
        Recipe List_item = recipeList.get(position);
        holder.name.setText(List_item.getName());
        holder.cuisine.setText(List_item.getCuisine());
        holder.mainIngredient.setText(List_item.getMainIngredient());
        Recipe recipe = recipeList.get(position);
        holder.name.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(),MainActivity.class);
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
