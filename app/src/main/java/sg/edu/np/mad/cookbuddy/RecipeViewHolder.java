package sg.edu.np.mad.cookbuddy;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView mainIngredient;
    TextView cuisine;
    ImageView image;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.recipeName);
        mainIngredient = itemView.findViewById(R.id.recipeMainIngredient);
        cuisine = itemView.findViewById(R.id.recipeCuisine);
        image = itemView.findViewById(R.id.recipeImage);
    }
}

