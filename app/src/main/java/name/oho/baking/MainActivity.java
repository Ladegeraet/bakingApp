package name.oho.baking;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import name.oho.baking.model.Receipt;
import name.oho.baking.network.BakingService;
import name.oho.baking.ui.main.ReceiptOverviewAdapter;
import name.oho.baking.ui.main.ReceiptOverviewRecyclerView;
import name.oho.baking.ui.receipt.ReceiptActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements ReceiptOverviewAdapter.ListItemClickListener{

    @Inject
    BakingService mBakingService;

    BakingServiceCallback mBakingServiceCallback;

    @BindView(R.id.pg_loadingIndicator)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.rv_receipts)
    ReceiptOverviewRecyclerView mReceiptOverviewRecyclerView;

    private List<Receipt> mReceiptList;
    private ReceiptOverviewAdapter mReceiptOverviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        BakingApplication.get(this).getAppComponent().inject(this);

        mBakingServiceCallback = new BakingServiceCallback();

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE || this.getResources().getBoolean(R.bool.isLargeDevice)){
            mReceiptOverviewRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mReceiptOverviewRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        }
        mReceiptOverviewRecyclerView.setHasFixedSize(true);

        mReceiptList = new ArrayList<>();
        mReceiptOverviewAdapter = new ReceiptOverviewAdapter(mReceiptList, this);
        mReceiptOverviewRecyclerView.setAdapter(mReceiptOverviewAdapter);

        mLoadingIndicator.setVisibility(View.VISIBLE);

        mBakingService.receipts().enqueue(mBakingServiceCallback);
    }

    @Override
    public void onListItemClick(int listItemIndex) {
        Timber.d(String.valueOf(listItemIndex));
        Receipt receipt = mReceiptList.get(listItemIndex);

        Intent intent = new Intent(this, ReceiptActivity.class);
        intent.putExtra(ReceiptActivity.RECEIPT_EXTRA, Parcels.wrap(receipt));
        startActivity(intent);
    }

    class BakingServiceCallback implements Callback<List<Receipt>> {
        @Override
        public void onResponse(Call<List<Receipt>> call, Response<List<Receipt>> response) {
            mReceiptList = response.body();

            mReceiptOverviewAdapter = new ReceiptOverviewAdapter(mReceiptList, MainActivity.this);
            mReceiptOverviewRecyclerView.setAdapter(mReceiptOverviewAdapter);

            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onFailure(Call<List<Receipt>> call, Throwable t) {
            Timber.d(t);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }
}
