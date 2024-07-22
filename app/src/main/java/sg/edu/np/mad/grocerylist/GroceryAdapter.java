package sg.edu.np.mad.grocerylist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder> {

    private ArrayList<GroceryItem> groceryItems;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onEditClick(int position);
        void onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public GroceryAdapter(ArrayList<GroceryItem> groceryItems) {
        this.groceryItems = groceryItems;
    }

    @NonNull
    @Override
    public GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grocery, parent, false);
        return new GroceryViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final GroceryViewHolder holder, int position) {
        final GroceryItem item = groceryItems.get(position);
        holder.itemName.setText(item.getName());
        holder.checkBox.setChecked(item.isChecked());

        holder.itemView.setOnClickListener(v -> {
            item.setChecked(!item.isChecked());
            holder.checkBox.setChecked(item.isChecked());
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (mListener != null) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    mListener.onItemLongClick(pos);
                    return true;
                }
            }
            return false;
        });

        holder.editIcon.setOnClickListener(v -> {
            if (mListener != null) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    mListener.onEditClick(pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    public static class GroceryViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        CheckBox checkBox;
        ImageView editIcon;

        public GroceryViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            checkBox = itemView.findViewById(R.id.checkbox);
            editIcon = itemView.findViewById(R.id.edit_icon);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
