package sg.edu.np.mad.cookbuddy.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.cookbuddy.R;

public class TechniqueViewHolder extends RecyclerView.ViewHolder{
    ImageView image;
    TextView title;
    TextView purpose;

    //constructor for TechniqueViewHolder
    public TechniqueViewHolder(View itemView){
        super(itemView);
        image = itemView.findViewById(R.id.ivTechnique);
        title = itemView.findViewById(R.id.tvTitle);
        purpose = itemView.findViewById(R.id.tvPurpose);
    }

    public ImageView getImage() {
        return this.image;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setPurpose(String purpose) {
        this.purpose.setText(purpose);
    }

}
