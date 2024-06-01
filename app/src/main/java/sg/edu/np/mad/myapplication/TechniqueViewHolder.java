package sg.edu.np.mad.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class TechniqueViewHolder extends RecyclerView.ViewHolder{
    ImageView techniqueImageView;
    TextView title;
    TextView purpose;
    public TechniqueViewHolder(View itemView){
        super(itemView);
        techniqueImageView = itemView.findViewById(R.id.techniqueImageView);
        title = itemView.findViewById(R.id.techniqueTitle);
        purpose = itemView.findViewById(R.id.techniquePurpose);
    }
}
