package sg.edu.np.mad.cookbuddy.activities;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.adapters.TechniqueAdapter;
import sg.edu.np.mad.cookbuddy.models.Technique;
public class TechniqueFragment extends Fragment {
    public TechniqueFragment() {
        // Required empty public constructor
    }

    public static TechniqueFragment newInstance() {
        return new TechniqueFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_technique, container, false);
        //create array list and initialize the techniques available on the app
        ArrayList<Technique> techniqueList = new ArrayList<>();
        Technique braising = new Technique(R.drawable.img_braising,"Braising","Tenderizes meat","Braising is a combination-cooking method" +
                " that uses both wet and dry heats. " +
                "Food is typically browned first at high temperature before being simmered" +
                " in a covered pot in cooking liquid","android.resource://" + HomeActivity.PACKAGE_NAME + "/" +R.raw.braising);
        Technique poaching = new Technique(R.drawable.img_poaching, "Poaching", "Gently cooks delicate foods", "Poaching involves cooking food by submerging it in a liquid " +
                "at a low temperature, typically below boiling, which is ideal for delicate foods like eggs and fish."
                ,"android.resource://" + HomeActivity.PACKAGE_NAME + "/" +R.raw.poaching);
        Technique deglazing = new Technique(R.drawable.img_deglazing,
                "Deglazing", "Enhances flavor through fond", "Deglazing involves adding liquid to a hot pan to" +
                " loosen and dissolve food particles stuck to the bottom. " +
                "This technique is commonly used to create a flavorful base for sauces by incorporating the browned bits, or 'fond,' " +
                "left after sautéing or searing.","android.resource://" + HomeActivity.PACKAGE_NAME + "/" +R.raw.deglazing);
        Technique panFrying = new Technique(
                R.drawable.img_pan_frying,
                "Pan Frying",
                "Quickly cooks food with minimal oil",
                "Pan frying is a dry-heat cooking method where food is cooked in a small amount of oil or fat in a hot pan. This technique is ideal for quickly cooking small or thin pieces of food to develop a crispy exterior.",
                "android.resource://" + HomeActivity.PACKAGE_NAME + "/" + R.raw.panfrying
        );
        Technique steaming = new Technique(
                R.drawable.img_steaming,
                "Steaming",
                "Retains nutrients and moisture",
                "Steaming is a moist-heat cooking method where food is cooked using the steam from boiling water. This gentle cooking method helps retain nutrients and moisture, making it ideal for vegetables, fish, and dumplings.",
                "android.resource://" + HomeActivity.PACKAGE_NAME + "/" + R.raw.steaming
        );
        //add techniques to list for recyclerView
        techniqueList.add(braising);
        techniqueList.add(poaching);
        techniqueList.add(deglazing);
        techniqueList.add(panFrying);
        techniqueList.add(steaming);

        //set up adapter with recyclerView
        TechniqueAdapter techniqueAdapter = new TechniqueAdapter(techniqueList, getContext());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(techniqueAdapter);

        return view;
    }
}