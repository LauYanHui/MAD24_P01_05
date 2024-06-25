package sg.edu.np.mad.cookbuddy;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class GroceryListActivity extends AppCompatActivity {

    private ListView list;
    private GroceryAdapter adapter;
    private List<GroceryItem> groceryList;
    private EditText textBox;
    private ImageView addIcon;
    private ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        list = findViewById(R.id.listView);
        textBox = findViewById(R.id.editTextGrocery);
        addIcon = findViewById(R.id.buttonAdd);
        backIcon = findViewById(R.id.backIcon);

        // replace with data from firebase
        groceryList = new ArrayList<>();
        groceryList.add(new GroceryItem("Item 1", false));
        groceryList.add(new GroceryItem("Item 2", false));
        groceryList.add(new GroceryItem("Item 3", false));
        groceryList.add(new GroceryItem("Item 4", false));
        groceryList.add(new GroceryItem("Item 5", false));

        adapter = new GroceryAdapter(this, groceryList);
        list.setAdapter(adapter);

        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(textBox.getText());
                if (name.isEmpty()) {
                    return;
                }
                GroceryItem newItem = new GroceryItem(name, false);
                // replace with firebase
                groceryList.add(newItem);
                adapter.notifyDataSetChanged();
                textBox.getText().clear();
            }
        });


        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroceryListActivity.this, HomepageActivity.class);
                startActivity(intent);
            }
        });
    }
}
