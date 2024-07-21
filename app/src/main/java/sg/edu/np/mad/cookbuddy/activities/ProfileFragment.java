package sg.edu.np.mad.cookbuddy.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sg.edu.np.mad.cookbuddy.R;

public class ProfileFragment extends Fragment {

    private final static String TAG = "ProfileFragment";
    private Button btnSignOut;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser(); // get cached user

            // user signed out
            if (user == null) {
                Log.d(TAG, "signOut:success");
                startActivity(new Intent(getContext(), LoginActivity.class));
                Toast.makeText(getContext(), "Signed Out", Toast.LENGTH_SHORT).show();
            }
        };
    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSignOut = view.findViewById(R.id.btnSignOut);

        btnSignOut.setOnClickListener(v -> {
            mAuth.signOut();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}