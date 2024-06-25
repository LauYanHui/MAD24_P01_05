package sg.edu.np.mad.cookbuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.models.Allergy;

public class AllergyAdapter extends BaseAdapter {
    private Context context;
    private List<Allergy> allergyList;
    private LayoutInflater inflater;

    public AllergyAdapter(Context context, List<Allergy> allergyList) {
        this.context = context;
        this.allergyList = allergyList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return allergyList.size();
    }
    @Override
    public Object getItem(int position) {
        return allergyList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AllergyAdapter.ViewHolder holder;

        if (convertView == null) {
            // if no old view to reuse
            convertView = inflater.inflate(R.layout.row_allergies, parent, false);
            holder = new AllergyAdapter.ViewHolder();
            holder.checkBox = convertView.findViewById(R.id.checkBox);
            holder.textView = convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        } else {
            // get old view
            holder = (AllergyAdapter.ViewHolder) convertView.getTag();
        }

        final Allergy item = allergyList.get(position);
        holder.textView.setText(item.getName());
        holder.checkBox.setChecked(item.isChecked());
        holder.checkBox.setOnCheckedChangeListener(((buttonView, isChecked) -> item.setChecked(isChecked)));

        return convertView;
    }

    private static class ViewHolder {
        CheckBox checkBox;
        TextView textView;
    }
}
