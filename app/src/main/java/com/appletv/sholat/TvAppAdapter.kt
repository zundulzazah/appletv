package com.appletv.sholat

import android.content.Context
import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TvAppAdapter(
    private val context: Context,
    private val apps: MutableList<ApplicationInfo>,
    private val onClick: (ApplicationInfo) -> Unit,
    private val onMoveTop: (ApplicationInfo) -> Unit,
    private val onHide: (ApplicationInfo) -> Unit
) : RecyclerView.Adapter<TvAppAdapter.Holder>() {

    private var style = "rect"
    private val pm = context.packageManager

    fun updateStyle(newStyle: String) { style = newStyle; notifyDataSetChanged() }

    class Holder(v: View) : RecyclerView.ViewHolder(v) {
        val icon: ImageView = v.findViewById(R.id.appIcon)
        val name: TextView = v.findViewById(R.id.appName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_app_rect, parent, false)
        return Holder(v)
    }

    override fun onBindViewHolder(h: Holder, pos: Int) {
        val app = apps[pos]
        h.name.text = pm.getApplicationLabel(app)
        try { h.icon.setImageDrawable(pm.getApplicationIcon(app)) } catch (e: Exception) {}
        h.itemView.setOnClickListener { onClick(app) }
        h.itemView.setOnLongClickListener { onMoveTop(app); true }
    }
    override fun getItemCount() = apps.size
}
