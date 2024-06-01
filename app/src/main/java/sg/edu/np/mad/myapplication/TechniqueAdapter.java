package sg.edu.np.mad.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TechniqueAdapter extends RecyclerView.Adapter<TechniqueViewHolder> {
    private final TechniqueRecycler activity;
    private final ArrayList<Technique> techniqueList;
    public TechniqueAdapter(ArrayList<Technique> techniqueList, TechniqueRecycler activity){
        this.activity = activity;
        this.techniqueList = techniqueList;
    }

    @NonNull
    @Override
    public TechniqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.technique_item, parent, false);
        return new TechniqueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TechniqueViewHolder holder, int position) {
        Technique listItems = techniqueList.get(position);
        holder.title.setText(listItems.getTitle());
        holder.purpose.setText(listItems.getPurpose());
        Glide.with(activity)
                .load(listItems.getImagePath())
                .into(holder.techniqueImageView);
        holder.techniqueImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TechniqueDetails = new Intent(activity, TechniqueDetails.class);
                TechniqueDetails.putExtra("title",listItems.getTitle());
                TechniqueDetails.putExtra("purpose",listItems.getPurpose());
                TechniqueDetails.putExtra("description",listItems.getDescription());
                TechniqueDetails.putExtra("videoPath",listItems.getVideoPath());
                activity.startActivity(TechniqueDetails);
            }
        });
    }

    @Override
    public int getItemCount() {return techniqueList.size();}
}
