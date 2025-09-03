package com.jorgerosas.freeappblocker.views

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
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
    companion object {
        private val ACTION_OPTIONS = listOf("Block", "Limit")
    }

    private var app: App? = null
    private var startTime: LocalTime? = null
    private var endTime: LocalTime? = null
    private var action: String? = null

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

        findViewById<Spinner>(R.id.spinnerAction).apply {
            adapter = ArrayAdapter(
                this@AppConfigurationView,
                R.layout.spinner_item,
                ACTION_OPTIONS
            ).apply {
                setDropDownViewResource(R.layout.spinner_drop_down)
            }

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    action = ACTION_OPTIONS[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val days = days.filter { it.isChecked }.map { it.text.toString() }
            trySave(app, days, startTime, endTime, action)
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

    private fun trySave(
        app: App?,
        days: List<String>,
        startTime: LocalTime?,
        endTime: LocalTime?,
        action: String?,
    ) {
        if (app == null) {
            Toast.makeText(this, "Select an application", Toast.LENGTH_SHORT).show()
            return
        }
        if (days.isEmpty()) {
            Toast.makeText(this, "Select at least one day", Toast.LENGTH_SHORT).show()
            return
        }
        if (startTime == null) {
            Toast.makeText(this, "Select a start time", Toast.LENGTH_SHORT).show()
            return
        }
        if (endTime == null) {
            Toast.makeText(this, "Select an end time", Toast.LENGTH_SHORT).show()
            return
        }
        if (action == null) {
            Toast.makeText(this, "Select an action", Toast.LENGTH_SHORT).show()
            return
        }

        when (action) {
            "Block" -> {
                Log.d(TAG, "Application: ${app.name}")
                Log.d(TAG, "Days: $days")
                Log.d(TAG, "Start: $startTime")
                Log.d(TAG, "End: $endTime")
                Log.d(TAG, "Action: $action")
            }

            else -> {
                Toast.makeText(this, "Action not implemented", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
