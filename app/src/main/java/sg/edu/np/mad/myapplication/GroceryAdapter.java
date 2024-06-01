package sg.edu.np.mad.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GroceryAdapter extends ArrayAdapter<String> {

    private DatabaseHelper dbHelper;
    private List<String> groceries;

    public GroceryAdapter(Context context, List<String> groceries, DatabaseHelper dbHelper) {
        super(context, 0, groceries);
        this.dbHelper = dbHelper;
        this.groceries = groceries;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView textViewGrocery = convertView.findViewById(R.id.textViewGrocery);
        ImageView buttonDelete = convertView.findViewById(R.id.buttonDelete);

        final String grocery = getItem(position);
        textViewGrocery.setText(grocery);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteGrocery(grocery);
                groceries.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
