package sg.edu.np.mad.cookbuddy.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.databinding.ActivityHomeBinding;


public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    public static String PACKAGE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeFragment(new RecipeFragment());

        PACKAGE_NAME = getApplicationContext().getPackageName();

        binding.navBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                FragmentManager fm = getSupportFragmentManager();
                Fragment newFragment = null;

                if (itemId == R.id.action_recipe) {
                    newFragment = fm.findFragmentById(R.id.fragment_recipe);
                } else if (itemId == R.id.action_grocery) {
                    newFragment = fm.findFragmentById(R.id.fragment_grocery);
                } else if (itemId == R.id.action_technique) {
                    newFragment = fm.findFragmentById(R.id.fragment_technique);
                }

                if (newFragment != null) {
                    changeFragment(newFragment);
                };
                return true;
            }
        });
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}