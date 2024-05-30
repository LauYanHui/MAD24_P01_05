package sg.edu.np.mad.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Text;

import sg.edu.np.mad.myapplication.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //Radio Buttons and CheckBoxes
        Button submitBTN = findViewById(R.id.button_Submit);
        RadioButton noAllergy = findViewById(R.id.radiobutton_noAllergy);
        RadioButton ifNotSelect = findViewById(R.id.radiobutton_ifNotSpecify);
        CheckBox Gluten = findViewById(R.id.checkBox_Gluten);
        CheckBox Eggs = findViewById(R.id.checkBox_Eggs);
        CheckBox Dairy = findViewById(R.id.checkBox_Milk);
        CheckBox Fish = findViewById(R.id.checkBox_Fish);
        CheckBox Shellfish = findViewById(R.id.checkBox_Shellfish);
        CheckBox Soy = findViewById(R.id.checkBox_Soy);
        CheckBox Peanut = findViewById(R.id.checkBox_Peanut);
        CheckBox Sesame = findViewById(R.id.checkBox_Sesame);
        CheckBox TreeNut = findViewById(R.id.checkBox_TreeNut);

        //Texts
        TextView ifNotSelectTxt = findViewById(R.id.textView_ifNotSelect);
        TextView GlutenTxt = findViewById(R.id.textView_Gluten);
        TextView EggsTxt = findViewById(R.id.textView_Eggs);
        TextView DairyTxt = findViewById(R.id.textView_Milk);
        TextView FishTxt = findViewById(R.id.textView_Fish);
        TextView ShellfishTxt = findViewById(R.id.textView_Shellfish);
        TextView SoyTxt = findViewById(R.id.textView_Soy);
        TextView PeanutTxt = findViewById(R.id.textView_Peanut);
        TextView SesameTxt = findViewById(R.id.textView_Sesame);
        TextView TreeNutTxt = findViewById(R.id.textView_TreeNut);

        noAllergy.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ifNotSelect.setChecked(false);
                //Radio Buttons and CheckBoxes
                Gluten.setVisibility(View.GONE);
                Eggs.setVisibility(View.GONE);
                Dairy.setVisibility(View.GONE);
                Fish.setVisibility(View.GONE);
                Shellfish.setVisibility(View.GONE);
                Soy.setVisibility(View.GONE);
                Peanut.setVisibility(View.GONE);
                Sesame.setVisibility(View.GONE);
                TreeNut.setVisibility(View.GONE);
                //Texts
                GlutenTxt.setVisibility(View.GONE);
                EggsTxt.setVisibility(View.GONE);
                DairyTxt.setVisibility(View.GONE);
                FishTxt.setVisibility(View.GONE);
                ShellfishTxt.setVisibility(View.GONE);
                SoyTxt.setVisibility(View.GONE);
                PeanutTxt.setVisibility(View.GONE);
                SesameTxt.setVisibility(View.GONE);
                TreeNutTxt.setVisibility(View.GONE);

            } else {
                Gluten.setVisibility(View.VISIBLE);
                Eggs.setVisibility(View.VISIBLE);
                Dairy.setVisibility(View.VISIBLE);
                Fish.setVisibility(View.VISIBLE);
                Shellfish.setVisibility(View.VISIBLE);
                Soy.setVisibility(View.VISIBLE);
                Peanut.setVisibility(View.VISIBLE);
                Sesame.setVisibility(View.VISIBLE);
                TreeNut.setVisibility(View.VISIBLE);

                GlutenTxt.setVisibility(View.VISIBLE);
                EggsTxt.setVisibility(View.VISIBLE);
                DairyTxt.setVisibility(View.VISIBLE);
                FishTxt.setVisibility(View.VISIBLE);
                ShellfishTxt.setVisibility(View.VISIBLE);
                SoyTxt.setVisibility(View.VISIBLE);
                PeanutTxt.setVisibility(View.VISIBLE);
                SesameTxt.setVisibility(View.VISIBLE);
                TreeNutTxt.setVisibility(View.VISIBLE);
            }
        });
        ifNotSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                noAllergy.setChecked(false);
            } else {
                return;
            }
        });

        submitBTN.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }));


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}