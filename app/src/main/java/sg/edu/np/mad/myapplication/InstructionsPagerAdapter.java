package sg.edu.np.mad.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InstructionsPagerAdapter extends RecyclerView.Adapter<InstructionsPagerAdapter.InstructionsViewHolder> {
    private final List<String> instructions;

    public InstructionsPagerAdapter(List<String> instructions) {
        this.instructions = instructions;
    }

    @NonNull
    @Override
    public InstructionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instruction_item, parent, false);
        return new InstructionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionsViewHolder holder, int position) {
        holder.instructionText.setText(instructions.get(position));
    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }

    static class InstructionsViewHolder extends RecyclerView.ViewHolder {
        TextView instructionText;

        public InstructionsViewHolder(@NonNull View itemView) {
            super(itemView);
            instructionText = itemView.findViewById(R.id.instructionText);
        }
    }
}
