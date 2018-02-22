package name.oho.baking.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import name.oho.baking.BakingApplication;
import name.oho.baking.R;
import name.oho.baking.model.Receipt;
import name.oho.baking.network.BakingService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * The configuration screen for the {@link ReceiptWidget ReceiptWidget} AppWidget.
 */
public class ReceiptWidgetConfigureActivity extends Activity {

    @Inject
    BakingService mBakingService;

    BakingServiceCallback mBakingServiceCallback;

    private static final String PREFS_NAME = "name.oho.baking.widget.ReceiptWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private List<Receipt> mReceiptList;

    @BindView(R.id.pg_loadingIndicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.rg_receipts)
    RadioGroup mReceipts;

    public ReceiptWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.receipt_widget_configure);

        ButterKnife.bind(this);
        BakingApplication.get(this).getAppComponent().inject(this);

        mBakingServiceCallback = new BakingServiceCallback();

        mBakingService.receipts().enqueue(mBakingServiceCallback);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

//        mAppWidgetText.setText(loadTitlePref(ReceiptWidgetConfigureActivity.this, mAppWidgetId));
    }

    private class BakingServiceCallback implements Callback<List<Receipt>> {
        @Override
        public void onResponse(Call<List<Receipt>> call, Response<List<Receipt>> response) {
            mReceiptList = response.body();
            if (mReceiptList == null || mReceiptList.isEmpty()) {
                finish();
            }

            for (Receipt r : mReceiptList) {
                RadioButton radioButton = new RadioButton(ReceiptWidgetConfigureActivity.this);
                radioButton.setText(r.getName());
                radioButton.setId(r.getId());
                mReceipts.addView(radioButton);
            }

            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onFailure(Call<List<Receipt>> call, Throwable t) {
            Timber.d(t);
//            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.add_button)
    public void onClick(View v) {
        final Context context = ReceiptWidgetConfigureActivity.this;

        // When the button is clicked, store the string locally
        int id = mReceipts.getCheckedRadioButtonId();
        if (id < 0) {
            return;
        }

        Receipt selectedReceipt = mReceiptList.get(id-1);

        saveReceiptIdPref(context, mAppWidgetId, selectedReceipt);

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ReceiptWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    // Write the prefix to the SharedPreferences object for this widget
    private void saveReceiptIdPref(Context context, int appWidgetId, Receipt receipt) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        Gson gson = new Gson();
        String json = gson.toJson(receipt);
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, json);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static Receipt loadReceipt(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        Gson gson = new Gson();

        return gson.fromJson(prefs.getString(PREF_PREFIX_KEY + appWidgetId, ""), Receipt.class);
    }

    static void deleteReceiptPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }
}

