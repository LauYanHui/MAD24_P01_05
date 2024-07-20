package sg.edu.np.mad.cookbuddy.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.cookbuddy.R;

public class TechniqueViewHolder extends RecyclerView.ViewHolder{
    public ImageView image;
    public TextView title;
    public TextView purpose;
    public CardView card;

    //constructor for TechniqueViewHolder
    public TechniqueViewHolder(View itemView){
        super(itemView);
        card = itemView.findViewById(R.id.card);
        image = itemView.findViewById(R.id.imageView);
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
