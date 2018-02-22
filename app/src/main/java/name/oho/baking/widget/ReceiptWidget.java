package name.oho.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.parceler.Parcels;

import name.oho.baking.MainActivity;
import name.oho.baking.R;
import name.oho.baking.model.Ingredient;
import name.oho.baking.model.Receipt;
import name.oho.baking.ui.receipt.ReceiptActivity;


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link ReceiptWidgetConfigureActivity ReceiptWidgetConfigureActivity}
 */
public class ReceiptWidget extends AppWidgetProvider {


    public static final String RECEIPT_EXTRA = "RECEIPT_EXTRA";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews widget = new RemoteViews(context.getPackageName(), R.layout.receipt_widget);

        Receipt receipt = ReceiptWidgetConfigureActivity.loadReceipt(context, appWidgetId);
        if (receipt != null) {
            String widgetContent = receipt.getName() + "\n\n";
            for (Ingredient ingredient:receipt.getIngredients()) {
                widgetContent += ingredient.toString();
            }
            widget.setTextViewText(R.id.tv_widget_ingredients, widgetContent);
        }

        Intent intent = new Intent(context, ReceiptActivity.class);
        intent.putExtra(ReceiptActivity.RECEIPT_EXTRA, Parcels.wrap(receipt));

        PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        widget.setOnClickPendingIntent(R.id.tv_widget_ingredients, configPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, widget);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            ReceiptWidgetConfigureActivity.deleteReceiptPref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

