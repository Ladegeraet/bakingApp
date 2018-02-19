package name.oho.baking.ui.receipt;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.slashrootv200.exoplayerfragment.ExoPlayerFragment;

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

    @BindView(R.id.exoplayer_container)
    FrameLayout exoplayerContainer;

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

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mStepDescription.setVisibility(View.GONE);
        } else {
            mStepDescription.setText(mCurrentStep.getDescription());
        }


        if (savedInstanceState == null) {
            Uri videoUri = Uri.parse(mCurrentStep.getVideoURL());
            if (!videoUri.toString().isEmpty()) {
                String videoTitle = mCurrentStep.getDescription();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.exoplayer_container, ExoPlayerFragment.newInstance(videoUri, videoTitle), ExoPlayerFragment.TAG)
                        .commit();
            } else {
                exoplayerContainer.setVisibility(View.GONE);
            }
        }
    }
}
