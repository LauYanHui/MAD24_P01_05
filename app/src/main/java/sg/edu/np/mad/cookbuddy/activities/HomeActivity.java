package sg.edu.np.mad.cookbuddy.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
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
    public final static String FIREBASE_URL = "https://mad-assignment-8c5d2-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private boolean pressedBackOnce;

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
                    if (newFragment == null) {
                        newFragment = new RecipeFragment();
                    }
                } else if (itemId == R.id.action_grocery) {
                    newFragment = fm.findFragmentById(R.id.fragment_grocery);
                    if (newFragment == null) {
                        newFragment = new GroceryFragment();
                    }
                } else if (itemId == R.id.action_technique) {
                    newFragment = fm.findFragmentById(R.id.fragment_technique);
                    if (newFragment == null) {
                        newFragment = new TechniqueFragment();
                    }
                } else if (itemId == R.id.action_profile) {
                    newFragment = fm.findFragmentById(R.id.fragment_profile);
                    if (newFragment == null) {
                        newFragment = new ProfileFragment();
                    }
                }

                if (newFragment != null) {
                    changeFragment(newFragment);
                };
                return true;
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pressedBackOnce = false;
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack("");
        transaction.commit();
    }

    private void handleBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 1) {
            if (pressedBackOnce) {
                finish();
                return;
            }

            pressedBackOnce = true;
            Toast.makeText(getApplicationContext(), "Click back again to exit", Toast.LENGTH_SHORT).show();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                @Override
                public void run() {
                    pressedBackOnce = false;
                }
            }, 2000);
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }
}