package dalker.cmtruong.com.app.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.function.ToIntBiFunction;

import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.view.activity.DetailDalkerActivity;
import dalker.cmtruong.com.app.view.activity.MainActivity;
import dalker.cmtruong.com.app.view.fragment.FragmentDetailDalker;
import timber.log.Timber;


/**
 * @author davidetruong
 * @version 1.0
 * @since 2018 August 15
 * <p>
 * Implementation of App Widget functionality.
 */
public class DalkerWidgetProvider extends AppWidgetProvider {

    public static String ACTION_UPDATE = "dalker.cmtruong.com.app.widget.ACTION_UPDATE";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.app_name);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dalker_widget_provider);
        ComponentName localComponentName = new ComponentName(context, DalkerWidgetProvider.class);
        views.setTextViewText(R.id.appwidget_text, widgetText);


        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        // Instruct the widget manager to update the widget
        setRemoteAdapter(context, views);

        Intent mIntent = new Intent(context, DalkerWidgetProvider.class);
        mIntent.setAction(ACTION_UPDATE);
        PendingIntent pendingUpdate = PendingIntent.getBroadcast(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_update, pendingUpdate);
        Timber.d("on Update");
        appWidgetManager.updateAppWidget(localComponentName, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            Timber.d("Widget has been updated");
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_UPDATE)) {
            Timber.d("on click ok");
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            int[] widgetIds = manager.getAppWidgetIds(new ComponentName(context, getClass()));
            manager.notifyAppWidgetViewDataChanged(widgetIds, R.id.favorite_lv);
            onUpdate(context, manager, widgetIds);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Intent intent = new Intent(context, FavoriteDalkerIService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, intent);
        } else {
            context.startService(intent);
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


    private static void setRemoteAdapter(Context context, RemoteViews remoteViews) {
        Timber.d("set adapter");
        remoteViews.setRemoteAdapter(R.id.favorite_lv, new Intent(context, FavoriteDalkerIService.class));
    }
}

