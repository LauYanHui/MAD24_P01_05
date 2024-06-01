package sg.edu.np.mad.cookbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        CardView recipePage = findViewById(R.id.recipes);
        CardView groceryList = findViewById(R.id.groceryList);
        recipePage.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                Intent activityName = new Intent(homepage.this,ListActivity.class);
                startActivity(activityName);
            }
        });
        groceryList.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                Intent activityName = new Intent(homepage.this,Grocery_List.class);
                startActivity(activityName);
            }
        });


    }
}