package sg.edu.np.mad.grocerylist;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<GroceryItem> items;
    GroceryAdapter adapter;

    EditText input;
    ImageView enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        input = findViewById(R.id.input);
        enter = findViewById(R.id.add);

        items = new ArrayList<>();
        items.add(new GroceryItem("Apple"));
        items.add(new GroceryItem("Banana"));
        items.add(new GroceryItem("Orange"));

        adapter = new GroceryAdapter(items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input.getText().toString();

                if (text == null || text.length() == 0) {
                    makeToast("Please enter an item.");
                } else {
                    addItem(text);
                    input.setText("");
                    makeToast(text + " added");
                }
            }
        });

        adapter.setOnItemClickListener(new GroceryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                items.get(position).setChecked(!items.get(position).isChecked());
                adapter.notifyItemChanged(position);
            }
            @Override
            public void onEditClick(int position) {
                showEditDialog(position);
            }
            public void onItemLongClick(int position) {
                showDeleteDialog(position);
            }
        });
    }

    private void showEditDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Edit Item");

        final EditText input = new EditText(MainActivity.this);
        input.setText(items.get(position).getName());
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            items.get(position).setName(input.getText().toString());
            adapter.notifyItemChanged(position);
            makeToast("Item updated");
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showDeleteDialog(final int position) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Delete Item")
                .setMessage("Do you want to delete this item?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    items.remove(position);
                    adapter.notifyItemRemoved(position);
                    makeToast("Item removed");
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    public void addItem(String item) {
        items.add(new GroceryItem(item));
        adapter.notifyItemInserted(items.size() - 1);
    }

    Toast t;
    private void makeToast(String s) {
        if (t != null) t.cancel();
        t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }
}
