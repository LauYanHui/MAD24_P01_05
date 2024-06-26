package sg.edu.np.mad.cookbuddy.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.cookbuddy.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView mainIngredient;
    TextView cuisine;
    ImageView image;

    public RecipeViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.tvRecipeName);
        mainIngredient = itemView.findViewById(R.id.mainIngredient);
        cuisine = itemView.findViewById(R.id.tvCuisine);
        image = itemView.findViewById(R.id.imageView);
    }

    public TextView getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name.setText(name);
    }
    public void setMainIngredient(String mainIngredient) {
        this.mainIngredient.setText(mainIngredient);
    }
    public void setCuisine(String cuisine) {
        this.cuisine.setText(cuisine);
    }
    public void setImage(int imgId) {
        this.image.setImageResource(imgId);
    }

}

