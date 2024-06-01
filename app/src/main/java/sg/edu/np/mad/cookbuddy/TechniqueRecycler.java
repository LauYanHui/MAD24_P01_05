package sg.edu.np.mad.cookbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TechniqueRecycler extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_technique_recycler);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //create array list and initialize the techniques available on the app
        ArrayList<Technique> techniqueList = new ArrayList<>();
        Technique braising = new Technique(R.drawable.braising,"Braising","Tenderizes meat","Braising is a combination-cooking method" +
                " that uses both wet and dry heats. " +
                "Food is typically browned first at high temperature before being simmered" +
                " in a covered pot in cooking liquid","android.resource://" + getPackageName() + "/" +R.raw.braising);
        Technique poaching = new Technique(R.drawable.poaching, "Poaching", "Gently cooks delicate foods", "Poaching involves cooking food by submerging it in a liquid " +
                "at a low temperature, typically below boiling, which is ideal for delicate foods like eggs and fish."
                ,"android.resource://" + getPackageName() + "/" +R.raw.poaching);
        Technique deglazing = new Technique(R.drawable.deglazing,
                "Deglazing", "Enhances flavor through fond", "Deglazing involves adding liquid to a hot pan to" +
                " loosen and dissolve food particles stuck to the bottom. " +
                "This technique is commonly used to create a flavorful base for sauces by incorporating the browned bits, or 'fond,' " +
                "left after sautéing or searing.","android.resource://" + getPackageName() + "/" +R.raw.deglazing);
        Technique panFrying = new Technique(
                R.drawable.panfrying,
                "Pan Frying",
                "Quickly cooks food with minimal oil",
                "Pan frying is a dry-heat cooking method where food is cooked in a small amount of oil or fat in a hot pan. This technique is ideal for quickly cooking small or thin pieces of food to develop a crispy exterior.",
                "android.resource://" + getPackageName() + "/" + R.raw.panfrying
        );
        Technique steaming = new Technique(
                R.drawable.steaming,
                "Steaming",
                "Retains nutrients and moisture",
                "Steaming is a moist-heat cooking method where food is cooked using the steam from boiling water. This gentle cooking method helps retain nutrients and moisture, making it ideal for vegetables, fish, and dumplings.",
                "android.resource://" + getPackageName() + "/" + R.raw.steaming
        );
        //add techniques to list for recyclerView
        techniqueList.add(braising);
        techniqueList.add(poaching);
        techniqueList.add(deglazing);
        techniqueList.add(panFrying);
        techniqueList.add(steaming);

        //set up adapter with recyclerView
        TechniqueAdapter techniqueAdapter = new TechniqueAdapter(techniqueList,this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(techniqueAdapter);

        ImageView backIcon = findViewById(R.id.backIconIV);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TechniqueRecycler.this, HomepageActivity.class);
                startActivity(intent);
            }
        });
    }
}