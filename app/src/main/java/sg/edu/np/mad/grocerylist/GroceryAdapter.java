package sg.edu.np.mad.grocerylist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder> {

    private ArrayList<GroceryItem> groceryItems;

    public GroceryAdapter(ArrayList<GroceryItem> groceryItems) {
        this.groceryItems = groceryItems;
    }

    @NonNull
    @Override
    public GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grocery, parent, false);
        return new GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GroceryViewHolder holder, int position) {
        final GroceryItem item = groceryItems.get(position);
        holder.itemName.setText(item.getName());
        holder.checkBox.setChecked(item.isChecked());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> item.setChecked(isChecked));
    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    public static class GroceryViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        CheckBox checkBox;

        public GroceryViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
