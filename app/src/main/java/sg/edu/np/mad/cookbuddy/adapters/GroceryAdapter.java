package sg.edu.np.mad.cookbuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import sg.edu.np.mad.cookbuddy.models.GroceryItem;
import sg.edu.np.mad.cookbuddy.R;

public class GroceryAdapter extends BaseAdapter {
    private Context context;
    private List<GroceryItem> groceryList;
    private LayoutInflater inflater;

    public GroceryAdapter(Context context, List<GroceryItem> groceryList) {
        this.context = context;
        this.groceryList = groceryList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return groceryList.size();
    }

    @Override
    public Object getItem(int position) {
        return groceryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            // if no old view to reuse
            convertView = inflater.inflate(R.layout.row_grocery, parent, false);
            holder = new ViewHolder();
            holder.checkBox = convertView.findViewById(R.id.checkBox);
            holder.textView = convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        } else {
            // get old view
            holder = (ViewHolder) convertView.getTag();
        }

        final GroceryItem item = groceryList.get(position);
        holder.textView.setText(item.getName());
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(item.isChecked());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setChecked(isChecked);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        CheckBox checkBox;
        TextView textView;
    }
}