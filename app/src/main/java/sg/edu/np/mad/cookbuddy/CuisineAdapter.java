package sg.edu.np.mad.cookbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CuisineAdapter extends RecyclerView.Adapter<CuisineAdapter.CuisineViewHolder> {
    private List<String> cuisines;
    private OnCuisineClickListener listener;
    private Context context;

    public interface OnCuisineClickListener {
        void onCuisineClick(String cuisine);
    }

    public CuisineAdapter(List<String> cuisines, Context context, OnCuisineClickListener listener) {
        this.cuisines = cuisines;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public CuisineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cuisine, parent, false);
        return new CuisineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CuisineViewHolder holder, int position) {
        String cuisine = cuisines.get(position);
        holder.cuisineTextView.setText(cuisine);
        holder.itemView.setOnClickListener(v -> listener.onCuisineClick(cuisine));
    }

    @Override
    public int getItemCount() {
        return cuisines.size();
    }

    public static class CuisineViewHolder extends RecyclerView.ViewHolder {
        TextView cuisineTextView;

        public CuisineViewHolder(View itemView) {
            super(itemView);
            cuisineTextView = itemView.findViewById(R.id.cuisineName);
        }
    }
}
