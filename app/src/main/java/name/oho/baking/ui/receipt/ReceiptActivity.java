package name.oho.baking.ui.receipt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

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

    SimpleExoPlayerView mExoPlayerView;

    private SimpleExoPlayer mExoPlayer;

    TextView mStepDescription;

    StepAdapter mStepAdapter;

    private Receipt mReceipt;

    private boolean mTwoPane;

    private Step mCurrentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
        mStepAdapter = new StepAdapter(mReceipt.getSteps(), mReceipt.getIngredients(), this);

        mStepRecyclerView.setAdapter(mStepAdapter);

        if (mTwoPane) {
            // show at start the first step -> Tablet
            mStepDescription = findViewById(R.id.tv_step_description);
            showStepDetail(mReceipt.getSteps().get(0));
        }
    }

    private void showStepDetail(Step step) {
        mCurrentStep = step;
        mStepDescription.setText(mCurrentStep.getDescription());
        if (mExoPlayer != null) {
            releasePlayer();
        }
        initializePlayer();
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

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer == null && mTwoPane) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    private void initializePlayer() {
        mExoPlayerView = findViewById(R.id.exoplayer_container);
        Uri uri = Uri.parse(mCurrentStep.getVideoURL());
        if (uri.toString().isEmpty()) {
            mExoPlayerView.setVisibility(View.GONE);
            return;
        }
        mExoPlayerView.setVisibility(View.VISIBLE);

        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        mExoPlayerView.setPlayer(mExoPlayer);

        // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(this, getString(R.string.app_name));
        MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                this, userAgent), new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
