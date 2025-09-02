package com.jorgerosas.freeappblocker.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jorgerosas.freeappblocker.R
import com.jorgerosas.freeappblocker.entity.App
import com.jorgerosas.freeappblocker.views.adapter.AppsAdapter

class AppConfigurationView : AppCompatActivity() {
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

        findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(this@AppConfigurationView)
            adapter = AppsAdapter(apps) { handleAppSelected(it) }
        }
    }

    private fun handleAppSelected(app: App) {
        Toast.makeText(this, "Clicked: ${app.name}", Toast.LENGTH_SHORT).show()
    }
}
