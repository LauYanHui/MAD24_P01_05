package sg.edu.np.mad.cookbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Locale;
import java.util.List;

import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.adapters.InstructionsPagerAdapter;
import sg.edu.np.mad.cookbuddy.models.Recipe;

public class InstructionFragment extends Fragment implements OnInitListener {
    private static final String ARG_INSTRUCTION_LIST = "instructionList";
    private static final String TAG = "InstructionFragment";
    private static final String ARG_RECIPE = "recipe";

    private List<String> instructionList;
    private TextToSpeech textToSpeech;
    private ViewPager2 instructionsPager;
    private Button speakButton;
    private Button timer;
    private Recipe recipe;

    public InstructionFragment() {
        // Required empty public constructor
    }

    public static InstructionFragment newInstance(List<String> instructionList, Recipe recipe) {
        InstructionFragment fragment = new InstructionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECIPE,recipe);
        args.putStringArrayList(ARG_INSTRUCTION_LIST, (ArrayList<String>) instructionList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            instructionList = getArguments().getStringArrayList(ARG_INSTRUCTION_LIST);
            recipe = (Recipe) getArguments().getSerializable(ARG_RECIPE);
        }
        // Initialize TextToSpeech
        textToSpeech = new TextToSpeech(getContext(), this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instruction, container, false);

        instructionsPager = view.findViewById(R.id.instructionsPager);
        speakButton = view.findViewById(R.id.speakButton);
        timer = view.findViewById(R.id.timerButton);

        if (instructionList != null) {
            InstructionsPagerAdapter adapter = new InstructionsPagerAdapter(instructionList);
            instructionsPager.setAdapter(adapter);
        }

        speakButton.setOnClickListener(v -> {
            int currentItem = instructionsPager.getCurrentItem();
            if (currentItem < instructionList.size()) {
                String instruction = instructionList.get(currentItem);
                speakInstruction(instruction);
            }
        });

        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TimerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Recipe",recipe);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int langResult = textToSpeech.setLanguage(Locale.US);
            if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Language is not supported or missing data");
            }
        } else {
            Log.e(TAG, "Initialization failed");
        }
    }

    private void speakInstruction(String instruction) {
        if (textToSpeech != null) {
            textToSpeech.speak(instruction, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }
}