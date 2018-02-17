package name.oho.baking;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import name.oho.baking.model.Receipt;
import name.oho.baking.network.BakingService;
import name.oho.baking.ui.ReceiptAdapter;
import name.oho.baking.ui.ReceiptRecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements ReceiptAdapter.ListItemClickListener{

    @Inject
    BakingService mBakingService;

    BakingServiceCallback mBakingServiceCallback;

    @BindView(R.id.pg_loadingIndicator)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.rv_receipts)
    ReceiptRecyclerView mReceiptRecyclerView;

    private List<Receipt> mReceiptList;
    private ReceiptAdapter mReceiptAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        BakingApplication.get(this).getAppComponent().inject(this);

        mBakingServiceCallback = new BakingServiceCallback();

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE || this.getResources().getBoolean(R.bool.isLargeDevice)){
            mReceiptRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mReceiptRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        }
        mReceiptRecyclerView.setHasFixedSize(true);

        mReceiptList = new ArrayList<>();
        mReceiptAdapter = new ReceiptAdapter(mReceiptList, this);
        mReceiptRecyclerView.setAdapter(mReceiptAdapter);

        mLoadingIndicator.setVisibility(View.VISIBLE);

        mBakingService.receipts().enqueue(mBakingServiceCallback);
    }

    @Override
    public void onListItemClick(int listItemIndex) {
        Timber.d(String.valueOf(listItemIndex));
    }

    class BakingServiceCallback implements Callback<List<Receipt>> {
        @Override
        public void onResponse(Call<List<Receipt>> call, Response<List<Receipt>> response) {
            mReceiptList = response.body();

            mReceiptAdapter = new ReceiptAdapter(mReceiptList, MainActivity.this);
            mReceiptRecyclerView.setAdapter(mReceiptAdapter);

            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onFailure(Call<List<Receipt>> call, Throwable t) {
            Timber.d(t);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }
}
