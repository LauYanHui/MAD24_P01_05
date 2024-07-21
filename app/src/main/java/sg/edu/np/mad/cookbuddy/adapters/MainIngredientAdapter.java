package sg.edu.np.mad.cookbuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.models.Recipe;

public class MainIngredientAdapter extends RecyclerView.Adapter<MainIngredientAdapter.MainIngredientViewHolder> {

    private Context context;
    private List<String> mainIngredientList;
    private Map<String, List<Recipe>> recipeMap;

    public MainIngredientAdapter(Context context, List<String> mainIngredientList, Map<String, List<Recipe>> recipeMap) {
        this.context = context;
        this.mainIngredientList = mainIngredientList;
        this.recipeMap = recipeMap;
    }


    @NonNull
    @Override
    public MainIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main_ingredient, parent, false);
        return new MainIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainIngredientViewHolder holder, int position) {
        String mainIngredient = mainIngredientList.get(position);
        holder.mainIngredientText.setText(mainIngredient);

        List<Recipe> recipeList = recipeMap.get(mainIngredient);
        ChildRecipeAdapter childRecipeAdapter = new ChildRecipeAdapter(recipeList, context);
        holder.recipeRecyclerView.setAdapter(childRecipeAdapter);

        // Set LinearLayoutManager with horizontal orientation
        holder.recipeRecyclerView.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        );
    }

    @Override
    public int getItemCount() {
        return mainIngredientList.size();
    }

    static class MainIngredientViewHolder extends RecyclerView.ViewHolder {
        TextView mainIngredientText;
        RecyclerView recipeRecyclerView;

        MainIngredientViewHolder(View itemView) {
            super(itemView);
            mainIngredientText = itemView.findViewById(R.id.mainIngredientText);
            recipeRecyclerView = itemView.findViewById(R.id.recipeRecyclerView);
        }
    }
}
