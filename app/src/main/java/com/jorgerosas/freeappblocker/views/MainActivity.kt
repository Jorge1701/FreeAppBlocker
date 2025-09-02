package com.jorgerosas.freeappblocker.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.jorgerosas.freeappblocker.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        findViewById<Button>(R.id.btnAppList).setOnClickListener {
            startActivity(Intent(this, AppList::class.java))
        }

        findViewById<Button>(R.id.btnPermissions).setOnClickListener {
            startActivity(Intent(this, PermissionsView::class.java))
        }
    }
}
