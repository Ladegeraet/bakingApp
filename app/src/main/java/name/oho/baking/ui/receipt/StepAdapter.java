package name.oho.baking.ui.receipt;

import android.annotation.SuppressLint;
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
import name.oho.baking.model.Ingredient;
import name.oho.baking.model.Step;

/**
 * Created by tobi on 18.02.18.
 */

public class StepAdapter extends StepRecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int INGREDIENT = 0;
    private static final int STEP = 1;

    private int mNumberItems;
    private int mNumberIngredients;
    private List<Object> mItemList;

    private final StepItemClickListener mOnClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        switch (viewType) {
            case STEP:
                view = inflater.inflate(R.layout.step_list_item, parent, false);
                return new StepViewHolder(view);
            case INGREDIENT:
                view = inflater.inflate(R.layout.ingredient_list_item, parent, false);
                return new IngredientViewHolder(view);
            default:
                break;
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (mItemList.get(position) instanceof Ingredient) {
            return INGREDIENT;
        } else if (mItemList.get(position) instanceof Step) {
            return STEP;
        }
        return -1;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case STEP:
                StepViewHolder stepViewHolder = (StepViewHolder) holder;
                stepViewHolder.bind((Step) mItemList.get(position));
                break;
            case INGREDIENT:
                IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) holder;
                ingredientViewHolder.bind((Ingredient) mItemList.get(position));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public interface StepItemClickListener {
        void onListItemClick(int listItemIndex);
    }

    public StepAdapter(List<Step> steps, List<Ingredient> ingredients, StepItemClickListener onClickListener){
        if (ingredients == null) {
            ingredients = new ArrayList<>();
        }

        if (steps == null) {
            steps = new ArrayList<>();
        }

        mItemList = new ArrayList<>();

        mItemList.addAll(ingredients);
        mNumberIngredients = ingredients.size();

        mItemList.addAll(steps);

        mNumberItems = mItemList.size();
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
            mOnClickListener.onListItemClick(getAdapterPosition() - mNumberIngredients);
        }
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ingredient)
        TextView ingridientTextView;

        @BindView(R.id.tv_quantity)
        TextView quantityTextView;

        @BindView(R.id.tv_measure)
        TextView measureTextView;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Ingredient ingredient) {
            ingridientTextView.setText(ingredient.getIngredient());
            quantityTextView.setText(String.valueOf(ingredient.getQuantity()));
            measureTextView.setText(ingredient.getMeasure());
        }
    }
}
