package sg.edu.np.mad.cookbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class InstructionFragment extends Fragment {
    private static final String ARG_INSTRUCTION_LIST = "instructionList";

    private List<String> instructionList;

    public InstructionFragment() {
        // Required empty public constructor
    }

    public static InstructionFragment newInstance(List<String> instructionList) {
        InstructionFragment fragment = new InstructionFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_INSTRUCTION_LIST, (ArrayList<String>) instructionList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            instructionList = getArguments().getStringArrayList(ARG_INSTRUCTION_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instruction, container, false);

        ViewPager2 instructionsPager = view.findViewById(R.id.instructionsPager);
        if (instructionList != null) {
            InstructionsPagerAdapter adapter = new InstructionsPagerAdapter(instructionList);
            instructionsPager.setAdapter(adapter);
        }

        return view;
    }
}
