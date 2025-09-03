package com.jorgerosas.freeappblocker.views

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.jorgerosas.freeappblocker.R
import com.jorgerosas.freeappblocker.entity.App
import com.jorgerosas.freeappblocker.utils.Constants.TAG
import com.jorgerosas.freeappblocker.views.adapter.AppAdapter
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale

class AppConfigurationView : AppCompatActivity() {
    private var app: App? = null
    private var startTime: LocalTime? = null
    private var endTime: LocalTime? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_configuration)

        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val apps = packageManager.queryIntentActivities(intent, 0)
            .map { resolveInfo ->
                App(
                    name = resolveInfo.loadLabel(packageManager).toString(),
                    packageName = resolveInfo.activityInfo.packageName,
                    icon = resolveInfo.loadIcon(packageManager),
                )
            }
            .sortedBy { it.name }

        findViewById<Spinner>(R.id.spinnerApps).apply {
            adapter = AppAdapter(this@AppConfigurationView, apps)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    app = apps[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        val days = listOf(
            findViewById<ToggleButton>(R.id.btnMon),
            findViewById<ToggleButton>(R.id.btnTue),
            findViewById<ToggleButton>(R.id.btnWed),
            findViewById<ToggleButton>(R.id.btnThu),
            findViewById<ToggleButton>(R.id.btnFri),
            findViewById<ToggleButton>(R.id.btnSat),
            findViewById<ToggleButton>(R.id.btnSun),
        )

        val txtStartTime = findViewById<TextView>(R.id.txtStartTime)
        txtStartTime.setOnClickListener {
            askForTime { hourOfDay, minute ->
                startTime = LocalTime.of(hourOfDay, minute)
                txtStartTime.text = String.format(Locale.ROOT, "%02d:%02d", hourOfDay, minute)
            }
        }

        val txtEndTime = findViewById<TextView>(R.id.txtEndTime)
        txtEndTime.setOnClickListener {
            askForTime { hourOfDay, minute ->
                endTime = LocalTime.of(hourOfDay, minute)
                txtEndTime.text = String.format(Locale.ROOT, "%02d:%02d", hourOfDay, minute)
            }
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            Log.d(TAG, "Application: ${app?.name}")
            val selected = days.filter { it.isChecked }.map { it.text.toString() }
            Log.d(TAG, "Days: $selected")
            Log.d(TAG, "Start: $startTime")
            Log.d(TAG, "End: $endTime")
        }
    }

    private fun askForTime(onSelection: (hour: Int, minute: Int) -> Unit) {
        val calendar = Calendar.getInstance()
        val timePicker = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                onSelection(hourOfDay, minute)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePicker.show()
    }
}
