package sg.edu.np.mad.cookbuddy.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.cookbuddy.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView mainIngredient;
    TextView cuisine;
    ImageView image;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.tvRecipeName);
        mainIngredient = itemView.findViewById(R.id.tvMainIngredient);
        cuisine = itemView.findViewById(R.id.tvCuisine);
        image = itemView.findViewById(R.id.imageView);
    }

    public TextView getName() {
        return name;
    }

    public TextView getMainIngredient() {
        return mainIngredient;
    }

    public TextView getCuisine() {
        return cuisine;
    }

    public ImageView getImage() {
        return image;
    }
}