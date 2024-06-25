package sg.edu.np.mad.cookbuddy.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.activities.TechniqueActivity;
import sg.edu.np.mad.cookbuddy.models.Technique;
import sg.edu.np.mad.cookbuddy.activities.TechniqueDetailsActivity;
import sg.edu.np.mad.cookbuddy.views.TechniqueViewHolder;

public class TechniqueAdapter extends RecyclerView.Adapter<TechniqueViewHolder> {
    private final TechniqueActivity activity;
    private final ArrayList<Technique> techniqueList;

    //constructor for adapter
    public TechniqueAdapter(ArrayList<Technique> techniqueList, TechniqueActivity activity){
        this.activity = activity;
        this.techniqueList = techniqueList;
    }

    @NonNull
    @Override
    public TechniqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_technique, parent, false);
        return new TechniqueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TechniqueViewHolder holder, int position) {
        Technique listItems = techniqueList.get(position);
        holder.setTitle(listItems.getTitle());
        holder.setPurpose(listItems.getPurpose());
        Glide.with(activity) // Glide helps with loading pictures to each technique
                .load(listItems.getImagePath())
                .into(holder.getImage());
        holder.getImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent setup for when user clicks on a specific technique
                Intent TechniqueDetails = new Intent(activity, TechniqueDetailsActivity.class);
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
