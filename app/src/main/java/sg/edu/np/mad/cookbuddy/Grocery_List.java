package sg.edu.np.mad.cookbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Grocery_List extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private GroceryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        dbHelper = new DatabaseHelper(this);

        EditText editTextGrocery = findViewById(R.id.editTextGrocery);
        ImageView buttonAdd = findViewById(R.id.buttonAdd);
        ListView listViewGroceries = findViewById(R.id.listViewGroceries);

        List<String> groceries = dbHelper.getAllGroceries();
        adapter = new GroceryAdapter(this, groceries, dbHelper);
        listViewGroceries.setAdapter(adapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String grocery = editTextGrocery.getText().toString();
                if (!grocery.isEmpty()) {
                    dbHelper.addGrocery(grocery);
                    adapter.add(grocery);
                    adapter.notifyDataSetChanged();
                    editTextGrocery.setText("");
                }
            }
        });
        ImageView backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Grocery_List.this, homepage.class);
                startActivity(intent);
            }
        });
    }
}
