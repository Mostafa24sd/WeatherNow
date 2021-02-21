package com.mostafasadati.weathernow.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import com.mostafasadati.weathernow.R
import com.mostafasadati.weathernow.ui.MainActivity


class WidgetConfigActivity : AppCompatActivity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_config)

        val configIntent = intent
        val extras = configIntent.extras

        if (extras != null)
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )

        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_CANCELED, resultValue)

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
            finish()


    }

    fun widgetConfirmBtn(view: View) {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val views = RemoteViews(this.packageName, R.layout.widget_current_layout)
        views.setOnClickPendingIntent(R.id.widgetImg, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)

        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
}