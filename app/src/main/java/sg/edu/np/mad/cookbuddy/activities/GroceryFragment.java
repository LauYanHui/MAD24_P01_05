package sg.edu.np.mad.cookbuddy.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.adapters.GroceryAdapter;
import sg.edu.np.mad.cookbuddy.models.GroceryItem;

public class GroceryFragment extends Fragment {

    private ListView list;
    private GroceryAdapter adapter;
    private List<GroceryItem> groceryList;
    private EditText textBox;
    private ImageView addIcon;
    private String FIREBASE_URL = "https://mad-assignment-8c5d2-default-rtdb.asia-southeast1.firebasedatabase.app/";


    public GroceryFragment() {
        // Required empty public constructor
    }

    public static GroceryFragment newInstance() {
        return new GroceryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grocery, container, false);

        list = view.findViewById(R.id.listView);
        textBox = view.findViewById(R.id.etGrocery);
        addIcon = view.findViewById(R.id.ivAdd);

        // replace with data from firebase
        groceryList = new ArrayList<>();
        groceryList.add(new GroceryItem("Item 1", false));
        groceryList.add(new GroceryItem("Item 2", false));
        groceryList.add(new GroceryItem("Item 3", false));
        groceryList.add(new GroceryItem("Item 4", false));
        groceryList.add(new GroceryItem("Item 5", false));

        adapter = new GroceryAdapter(getContext(), groceryList);
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

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}