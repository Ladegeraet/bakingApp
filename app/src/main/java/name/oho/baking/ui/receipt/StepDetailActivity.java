package name.oho.baking.ui.receipt;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import name.oho.baking.R;
import name.oho.baking.model.Receipt;
import name.oho.baking.model.Step;
import timber.log.Timber;

import static name.oho.baking.ui.receipt.ReceiptActivity.NOT_STARTED_POSITION;
import static name.oho.baking.ui.receipt.ReceiptActivity.RECEIPT_EXTRA;
import static name.oho.baking.ui.receipt.ReceiptActivity.VIDEO_POSITION;

public class StepDetailActivity extends AppCompatActivity {

    public static final String STEP_EXTRA = "step_extra";

    @BindView(R.id.tv_step_description)
    TextView mStepDescription;

    @BindView(R.id.exoplayer_container)
    SimpleExoPlayerView mExoPlayerView;

    @BindView(R.id.btn_prev_step)
    Button prevButton;

    @BindView(R.id.btn_next_step)
    Button nextButton;

    private SimpleExoPlayer mExoPlayer;

    private Receipt mReceipt;
    private int mStep;
    private Step mCurrentStep;

    private long mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
            mStepDescription.setVisibility(View.VISIBLE);
        }

        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getLong(VIDEO_POSITION, NOT_STARTED_POSITION);
        }

        initView();
    }

    private void initView() {

        if (mStep == 0) {
            prevButton.setVisibility(View.INVISIBLE);
            setTitle(getString(R.string.firstStep));
        } else if (mStep == mReceipt.getSteps().size() - 1) {
            nextButton.setVisibility(View.INVISIBLE);
            setTitle(getString(R.string.lastStep));
        } else {
            prevButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
            setTitle(getString(R.string.step, mStep));
        }
        mStepDescription.setText(mCurrentStep.getDescription());
        releasePlayer();
        initializePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer == null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    private void initializePlayer() {
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

        if (mPosition > NOT_STARTED_POSITION) {
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.seekTo(mPosition);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @OnClick(R.id.btn_prev_step)
    void prevStep() {
        try {
            mStep--;
            mCurrentStep = mReceipt.getSteps().get(mStep);
            initView();
        } catch (Exception e) {
            Timber.d(e);
        }

    }

    @OnClick(R.id.btn_next_step)
    void nextStep() {
        try {
            mStep++;
            mCurrentStep = mReceipt.getSteps().get(mStep);
            initView();
        } catch (Exception e) {
            Timber.d(e);
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putLong(VIDEO_POSITION, mPosition);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPosition = savedInstanceState.getLong(VIDEO_POSITION, NOT_STARTED_POSITION);
    }
}
