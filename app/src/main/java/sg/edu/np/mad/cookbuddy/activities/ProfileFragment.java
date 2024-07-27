package sg.edu.np.mad.cookbuddy.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.media3.extractor.text.webvtt.WebvttCssStyle;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.regex.Pattern;

import nu.aaro.gustav.passwordstrengthmeter.PasswordStrengthCalculator;
import nu.aaro.gustav.passwordstrengthmeter.PasswordStrengthMeter;
import sg.edu.np.mad.cookbuddy.R;
public class ProfileFragment extends Fragment {

    private final static String TAG = "ProfileFragment";
    private Button btnUsername;
    private Button btnPassword;
    private Button btnSignOut;
    private AlertDialog alertDialogUsername;
    private AlertDialog alertDialogPassword;
    private EditText etUsername;
    private EditText etCurrentPassword;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etFiltered;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private AuthCredential credential;
    private DatabaseReference userRef;
    private DatabaseReference recipeRef;
    private LinearLayout linearLayoutFav;
    private Boolean pwAccepted = false;
    private int primaryColor;

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
        user = mAuth.getCurrentUser();

        // should not happen
        if (user == null) {
            Log.d(TAG, "noUserFound");
            startActivity(new Intent(getContext(), LoginActivity.class));
            Toast.makeText(getContext(), "Re-login", Toast.LENGTH_SHORT).show();
        }

        mAuthListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser(); // get cached user

            // user signed out
            if (user == null) {
                Log.d(TAG, "signOut:success");
                startActivity(new Intent(getContext(), LoginActivity.class));
                Toast.makeText(getContext(), "Signed Out", Toast.LENGTH_SHORT).show();
            }
        };

        // Initialize Firebase reference
        userRef = FirebaseDatabase.getInstance(HomeActivity.FIREBASE_URL).getReference("Users");
        recipeRef = FirebaseDatabase.getInstance(HomeActivity.FIREBASE_URL).getReference("Recipes");

        // Get color resource for styling
        primaryColor = ContextCompat.getColor(requireContext(), R.color.caribbeanCurrent);

        initializeEditTexts();
        initializeAlertDialogs();
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

        // Initialize UI elements
        linearLayoutFav = view.findViewById(R.id.linearLayoutFav);
        btnSignOut = view.findViewById(R.id.btnSignOut);
        btnUsername = view.findViewById(R.id.btnUsername);
        btnPassword = view.findViewById(R.id.btnPassword);


        // Set initial value in views
        btnUsername.setText(Html.fromHtml("Username<br/><small>" + user.getDisplayName() + "</small>", Html.FROM_HTML_MODE_LEGACY));
        userRef.child(user.getUid()).child("favourites").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists() || !snapshot.hasChildren()) {
                    return;
                }
                for (DataSnapshot favourite : snapshot.getChildren()) {
                    String key = favourite.getKey();
                    recipeRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                TextView tv = new TextView(getContext());
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        400,
                                        ViewGroup.LayoutParams.MATCH_PARENT);
                                layoutParams.setMarginEnd(50);
                                tv.setLayoutParams(layoutParams);
                                tv.setPadding(20,20,20,20);
                                tv.setGravity(Gravity.CENTER);
                                tv.setText(snapshot.child("Name").getValue(String.class));
                                tv.setTextSize(16);
                                tv.setTextColor(primaryColor);
                                tv.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.style_primary_hollow));
                                linearLayoutFav.addView(tv);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.w(TAG, "GetRecipeName:error");
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "GetFavourites:error");
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // set listeners
        btnSignOut.setOnClickListener(v -> mAuth.signOut());
        btnUsername.setOnClickListener(v -> alertDialogUsername.show());
        btnPassword.setOnClickListener(v -> alertDialogPassword.show());
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

    private void initializeEditTexts() {
        etUsername = new EditText(getContext());
        etCurrentPassword = new EditText(getContext());
        etPassword = new EditText(getContext());
        etConfirmPassword = new EditText(getContext());
        etFiltered = new EditText(getContext());

        // Initialize layout params for EditTexts
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        // Set layout params
        etUsername.setLayoutParams(layoutParams);
        etCurrentPassword.setLayoutParams(layoutParams);
        etPassword.setLayoutParams(layoutParams);
        etConfirmPassword.setLayoutParams(layoutParams);
        etFiltered.setVisibility(View.GONE);

        // Set input type
        etUsername.setInputType(InputType.TYPE_CLASS_TEXT);
        etCurrentPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT );
        etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        etConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);

        // Change line color
        ColorStateList colorStateList = ColorStateList.valueOf(primaryColor);
        etUsername.setBackgroundTintList(colorStateList);
        etCurrentPassword.setBackgroundTintList(colorStateList);
        etPassword.setBackgroundTintList(colorStateList);
        etConfirmPassword.setBackgroundTintList(colorStateList);

        // Set hints
        etUsername.setHint("Username");
        etCurrentPassword.setHint("Current Password");
        etPassword.setHint("New Password");
        etConfirmPassword.setHint("Confirm New Password");

        // Add Text Watcher to remove spaces in EditTexts
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    etUsername.setText(result);
                    etUsername.setSelection(result.length());
                }
            }
        });
        etCurrentPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    etCurrentPassword.setText(result);
                    etCurrentPassword.setSelection(result.length());
                }
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    etPassword.setText(result);
                    etPassword.setSelection(result.length());
                }
                etFiltered.setText(result);
            }
        });
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    etConfirmPassword.setText(result);
                    etConfirmPassword.setSelection(result.length());
                }
            }
        });
    }

    private void initializeAlertDialogs() {
        // Initialize layout for view in alert dialog
        LinearLayout linearLayoutPassword = new LinearLayout(getContext());
        LinearLayout linearLayoutUsername = new LinearLayout(getContext());

        linearLayoutUsername.setOrientation(LinearLayout.VERTICAL);
        linearLayoutPassword.setOrientation(LinearLayout.VERTICAL);

        // Initialize PasswordStrengthMeter for changing password
        PasswordStrengthMeter meter = new PasswordStrengthMeter(getContext());
        meter.setVisibility(View.GONE);
        meter.setEditText(etFiltered);
        meter.setPasswordStrengthCalculator(new PasswordStrengthCalculator() {
            @Override
            public int calculatePasswordSecurityLevel(String s) {
                double score = 3;

                // show meter only when user starts typing
                meter.setVisibility(View.VISIBLE);

                // if length less than minimum length,
                // don't conduct any other checks
                if (s.isEmpty()) {
                    return 0; // too short
                }

                if (s.length() < 5) {
                    return 1;
                }

                if (s.length() < getMinimumLength()) {
                    return 2;
                }

                // reward longer passwords
                if (s.length() >= 12) {
                    score += 0.5;
                }

                // check for number
                if (Pattern.compile("\\d").matcher(s).find()) {
                    score += 0.5;
                }

                // check for at least 1 upper and lowercase
                if (Pattern.compile("[a-z]").matcher(s).find() &&
                        Pattern.compile("[A-Z]").matcher(s).find()) {
                    score += 0.5;
                }

                // check for symbols/special chars
                if (Pattern.compile("[~`!@#$%^&*()_\\-+={\\[}\\]|\\\\:;\"'<,>.?/]")
                        .matcher(s).find()) {
                    score += 0.5;
                }

                return (int) Math.floor(score);
            }

            @Override
            public int getMinimumLength() { return 8; }

            @Override
            public boolean passwordAccepted(int i) {
                pwAccepted = (i > 3);
                return i > 3;
            }

            @Override
            public void onPasswordAccepted(String s) { }
        });

        // Add views to layout
        linearLayoutUsername.addView(etUsername);
        linearLayoutUsername.setPadding(60, 30, 60, 30);

        linearLayoutPassword.addView(etCurrentPassword);
        linearLayoutPassword.addView(etPassword);
        linearLayoutPassword.addView(meter);
        linearLayoutPassword.addView(etConfirmPassword);
        linearLayoutPassword.addView(etFiltered);
        linearLayoutPassword.setPadding(60, 30, 60, 30);

        // TextView for custom title in AlertDialogs
        TextView titleUsername = new TextView(getContext());
        TextView titlePassword = new TextView(getContext());
        titleUsername.setText(ContextCompat.getString(requireContext(), R.string.profile_change_username));
        titlePassword.setText(ContextCompat.getString(requireContext(), R.string.profile_change_password));
        titleUsername.setTextColor(primaryColor);
        titlePassword.setTextColor(primaryColor);
        titleUsername.setTextSize(20);
        titlePassword.setTextSize(20);
        titleUsername.setTypeface(Typeface.DEFAULT_BOLD);
        titlePassword.setTypeface(Typeface.DEFAULT_BOLD);
        titleUsername.setPadding(40, 40, 40, 40);
        titlePassword.setPadding(40,40,40,40);

        // Initialize AlertDialogs for profile updates
        alertDialogUsername = new AlertDialog.Builder(getContext())
                .setCustomTitle(titleUsername)
                .setView(linearLayoutUsername)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();

        alertDialogPassword = new AlertDialog.Builder(getContext())
                .setCustomTitle(titlePassword)
                .setView(linearLayoutPassword)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();

        // Add listeners to dialogs
        alertDialogUsername.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positive = alertDialogUsername.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negative = alertDialogUsername.getButton(AlertDialog.BUTTON_NEGATIVE);

                positive.setTextColor(primaryColor);
                negative.setTextColor(primaryColor);

                positive.setOnClickListener(v -> {
                    String newDisplayName = etUsername.getText().toString();

                    if (newDisplayName.isEmpty()) {
                        Toast.makeText(getContext(), "Name can't be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    UserProfileChangeRequest req = new UserProfileChangeRequest.Builder()
                            .setDisplayName(newDisplayName)
                            .build();

                    user.updateProfile(req).addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Error updating username",  Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "UpdateUsername:error");
                        };
                        Log.d(TAG, "UpdateUsername:success");
                        user.reload().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                btnUsername.setText(Html.fromHtml("Username<br/><small>" + user.getDisplayName() + "</small>", Html.FROM_HTML_MODE_LEGACY));
                                Log.d(TAG, "LoadNewUsername:success");
                                Toast.makeText(getContext(), "Username updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.w(TAG, "LoadNewUsername:error");
                                Toast.makeText(getContext(), "Error fetching latest data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                    etUsername.getText().clear();
                    dialog.dismiss();
                });
            }
        });
        alertDialogPassword.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positive = alertDialogPassword.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negative = alertDialogPassword.getButton(AlertDialog.BUTTON_NEGATIVE);

                positive.setTextColor(primaryColor);
                negative.setTextColor(primaryColor);

                positive.setOnClickListener(v -> {
                    String currentPassword = etCurrentPassword.getText().toString();
                    String password = etPassword.getText().toString();
                    String confirmPassword = etConfirmPassword.getText().toString();

                    if (currentPassword.isEmpty()) {
                        etCurrentPassword.setError("Enter current password");
                        return;
                    }

                    if (currentPassword.equals(password)) {
                        etPassword.setError("Cannot be current password");
                        return;
                    }

                    if (!password.equals(confirmPassword)) {
                        etConfirmPassword.setError("Must match password");
                        return;
                    }

                    if (!pwAccepted) {
                        etPassword.setError("Password is too weak");
                        return;
                    }

                    credential = EmailAuthProvider.getCredential(
                            Objects.requireNonNull(user.getEmail()),
                            currentPassword);

                    // re-authenticate for security-sensitive actions
                    user.reauthenticate(credential).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Re-authenticate:success");
                            user.updatePassword(password).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Log.d(TAG, "UpdatePassword:success");
                                    user.reload().addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful()) {
                                            Log.d(TAG, "LoadNewPassword:success");
                                            Toast.makeText(getContext(), "Password updated", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Log.w(TAG, "LoadNewPassword:error");
                                            Toast.makeText(getContext(), "Error fetching latest data", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Log.w(TAG, "UpdatePassword:error");
                                    Toast.makeText(getContext(), "Error updating password", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Log.w(TAG, "Re-authenticate:error");
                            Toast.makeText(getContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    });
                    etCurrentPassword.getText().clear();
                    etPassword.getText().clear();
                    etConfirmPassword.getText().clear();
                    dialog.dismiss();
                });
            }
        });
    }
}

