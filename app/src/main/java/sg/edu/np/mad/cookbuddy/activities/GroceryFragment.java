package sg.edu.np.mad.cookbuddy.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.adapters.GroceryAdapter;
import sg.edu.np.mad.cookbuddy.models.GroceryItem;

public class GroceryFragment extends Fragment {

    private ListView list;
    private GroceryAdapter adapter;
    private List<GroceryItem> groceryList;
    private GroceryItem groceryItem;
    private EditText textBox;
    private ImageView addIcon;
    private ImageView deleteIcon;


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

        // get widgets
        list = view.findViewById(R.id.listView);
        textBox = view.findViewById(R.id.etGrocery);
        addIcon = view.findViewById(R.id.ivAdd);
        deleteIcon = view.findViewById(R.id.ivDelete);

        // set up grocery list
        groceryList = new ArrayList<>();
        adapter = new GroceryAdapter(getContext(), groceryList);
        list.setAdapter(adapter);

        // get user info
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", null);

        // get data from firebase
        DatabaseReference groceryRef = FirebaseDatabase.getInstance(HomeActivity.FIREBASE_URL).getReference("Users/" + username + "/grocery/");
        groceryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groceryList.clear(); // clear previous items before change

                // create GroceryItem object for each item in FB
                if (snapshot.hasChildren()) {
                    for (DataSnapshot grocery : snapshot.getChildren()) {
                        String name = grocery.getKey();;
                        Boolean checked = grocery.child("checked").getValue(Boolean.class);
                        groceryItem = new GroceryItem(name, Boolean.TRUE.equals(checked));
                        groceryList.add(groceryItem);
                    }
                }

                adapter.notifyDataSetChanged(); // update ui
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "loadGrocery:onCancelled", error.toException());
            }
        });

        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(textBox.getText());

                if (name.isEmpty()) {
                    return;
                }

                groceryRef.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // check if item exists before adding
                        if (snapshot.exists()) {
                            Toast.makeText(getContext(), "Item is already in list", Toast.LENGTH_SHORT).show();
                        } else {
                            groceryRef.child(name).child("checked").setValue(false);
                        }
                        textBox.getText().clear();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("Firebase", "addGrocery:onCancelled", error.toException());
                    }
                });
            }
        });

        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // filter items that are checked
                List<GroceryItem> toDelete = groceryList.stream().filter(GroceryItem::isChecked).collect(Collectors.toList());

                // if no checked items, show toast
                if (toDelete.isEmpty()) {
                    Toast.makeText(getContext(), "No checked items", Toast.LENGTH_SHORT).show();
                    return;
                }

                // else, create alert dialog
                AlertDialog.Builder adBuilder = new AlertDialog.Builder(getContext());
                adBuilder.setTitle("Delete items?");
                adBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (GroceryItem gi : toDelete) {
                            groceryRef.child(gi.getName()).removeValue();
                        }
                    }
                });
                adBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("App", "clicked cancel");
                    }
                });
                AlertDialog alert = adBuilder.create();
                alert.show();
            }
        });

        return view;
    }
}