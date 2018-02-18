package name.oho.baking.ui.receipt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import name.oho.baking.R;
import name.oho.baking.model.Step;

/**
 * Created by tobi on 18.02.18.
 */

public class StepAdapter extends StepRecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private int mNumberItems;
    private List<Step> mStepList;

    private final StepItemClickListener mOnClickListener;

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.step_list_item, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        holder.bind(mStepList.get(position));
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public interface StepItemClickListener {
        void onListItemClick(int listItemIndex);
    }

    public StepAdapter(List<Step> stepList, StepItemClickListener onClickListener){
        if (stepList == null) {
            stepList = new ArrayList<>();
        }

        mNumberItems = stepList.size();
        mStepList = stepList;
        mOnClickListener = onClickListener;
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_step_short_description)
        TextView description;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void bind(Step step) {
            description.setText(step.getShortDescription());
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onListItemClick(getAdapterPosition());
        }
    }
}
