package name.oho.baking.ui.receipt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.widget.TextView;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import name.oho.baking.R;
import name.oho.baking.model.Receipt;
import name.oho.baking.model.Step;
import timber.log.Timber;

public class ReceiptActivity extends AppCompatActivity implements StepAdapter.StepItemClickListener {

    public static final String RECEIPT_EXTRA = "receipt_extra";

    @BindView(R.id.rv_steps)
    StepRecyclerView mStepRecyclerView;

    TextView mStepDescription;

    StepAdapter mStepAdapter;

    private Receipt mReceipt;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        ButterKnife.bind(this);

        mTwoPane = findViewById(R.id.fr_step_detail) != null;

        Intent intent = getIntent();
        if (intent.hasExtra(RECEIPT_EXTRA)) {
            mReceipt = Parcels.unwrap(intent.getParcelableExtra(RECEIPT_EXTRA));
        } else {
            onBackPressed();
            return;
        }

        setTitle(mReceipt.getName());

        mStepRecyclerView.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL, false));
        mStepAdapter = new StepAdapter(mReceipt.getSteps(), this);

        mStepRecyclerView.setAdapter(mStepAdapter);

        if (mTwoPane) {
            // show at start the first step -> Tablet
            mStepDescription = findViewById(R.id.tv_step_description);
            showStepDetail(mReceipt.getSteps().get(0));
        }
    }

    private void showStepDetail(Step step) {
        mStepDescription.setText(step.getDescription());
    }

    @Override
    public void onListItemClick(int listItemIndex) {
        Timber.d("clicked step #%d", listItemIndex);
        if (mTwoPane) {
            showStepDetail(mReceipt.getSteps().get(listItemIndex));
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(RECEIPT_EXTRA, Parcels.wrap(mReceipt));
            intent.putExtra(StepDetailActivity.STEP_EXTRA, listItemIndex);
            startActivity(intent);
        }

    }
}
