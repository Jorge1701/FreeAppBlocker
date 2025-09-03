package com.jorgerosas.freeappblocker.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.jorgerosas.freeappblocker.R
import com.jorgerosas.freeappblocker.entity.App

class AppAdapter(
    context: Context,
    private val apps: List<App>,
) : ArrayAdapter<App>(context, 0, apps) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app, parent, false)
        val app = apps[position]
        view.findViewById<TextView>(R.id.appName).text = app.name
        view.findViewById<ImageView>(R.id.appIcon).setImageDrawable(app.icon)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.item_app, parent, false)
        val app = apps[position]
        view.findViewById<TextView>(R.id.appName).text = app.name
        view.findViewById<ImageView>(R.id.appIcon).setImageDrawable(app.icon)
        return view
    }
}
