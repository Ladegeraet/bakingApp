package name.oho.baking.ui.receipt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import name.oho.baking.R;
import name.oho.baking.model.Receipt;
import name.oho.baking.model.Step;

import static name.oho.baking.ui.receipt.ReceiptActivity.RECEIPT_EXTRA;

public class StepDetailActivity extends AppCompatActivity {

    public static final String STEP_EXTRA = "step_extra";

    @BindView(R.id.tv_step_description)
    TextView mStepDescription;

    private Receipt mReceipt;
    private int mStep;
    private Step mCurrentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.hasExtra(RECEIPT_EXTRA)) {
            mReceipt = Parcels.unwrap(intent.getParcelableExtra(RECEIPT_EXTRA));
            mStep = intent.getIntExtra(STEP_EXTRA, 0);
            mCurrentStep = mReceipt.getSteps().get(mStep);
        } else {
            onBackPressed();
            return;
        }

        mStepDescription.setText(mCurrentStep.getDescription());
    }
}
