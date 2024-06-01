package sg.edu.np.mad.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RecipeViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView mainIngredient;
    TextView cuisine;
    ImageView image;
    public RecipeViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.recipeName);
        mainIngredient = itemView.findViewById(R.id.mainIngredient);
        cuisine = itemView.findViewById(R.id.cuisine);
        image = itemView.findViewById(R.id.imageView);

    }
}

