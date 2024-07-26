package sg.edu.np.mad.cookbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InformationFragment extends Fragment {
    private static final String ARG_NUTRITION_TEXT = "nutrition_text";
    private static final String ARG_INGREDIENT_TEXT = "ingredient_text";

    private String nutritionText;
    private String ingredientText;

    public InformationFragment() {
        // Required empty public constructor
    }

    public static InformationFragment newInstance(String nutritionText, String ingredientText) {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NUTRITION_TEXT, nutritionText);
        args.putString(ARG_INGREDIENT_TEXT, ingredientText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nutritionText = getArguments().getString(ARG_NUTRITION_TEXT);
            ingredientText = getArguments().getString(ARG_INGREDIENT_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_information, container, false);

        TextView nutritionTextView = rootView.findViewById(R.id.tvNutrients);
        nutritionTextView.setText(nutritionText);

        TextView ingredientTextView = rootView.findViewById(R.id.tvIngredients);
        ingredientTextView.setText(ingredientText);

        return rootView;
    }
}
