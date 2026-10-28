package com.appletv.sholat

import android.content.Context
import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appletv.sholat.databinding.ItemAppCircleBinding
import com.appletv.sholat.databinding.ItemAppRectBinding

class TvAppAdapter(
    private val context: Context,
    private val apps: MutableList<ApplicationInfo>,
    private val onClick: (ApplicationInfo) -> Unit,
    private val onMoveTop: (ApplicationInfo) -> Unit,
    private val onHide: (ApplicationInfo) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var style: String = AppListManager.getStyle(context)

    fun updateStyle(newStyle: String) {
        style = newStyle
        notifyDataSetChanged()
    }

    private val VIEW_CIRCLE = 1
    private val VIEW_RECT = 2

    override fun getItemViewType(position: Int): Int {
        return if (style == "circle") VIEW_CIRCLE else VIEW_RECT
    }

    inner class CircleVH(val binding: ItemAppCircleBinding) : RecyclerView.ViewHolder(binding.root)
    inner class RectVH(val binding: ItemAppRectBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_CIRCLE) {
            CircleVH(ItemAppCircleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            RectVH(ItemAppRectBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemCount(): Int = apps.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val app = apps[position]
        val pm = context.packageManager

        if (holder is CircleVH) {
            holder.binding.txtAppName.text = pm.getApplicationLabel(app)
            holder.binding.imgIcon.setImageDrawable(pm.getApplicationIcon(app))
            holder.binding.root.setOnClickListener { onClick(app) }
            holder.binding.root.setOnLongClickListener {
                EditAppDialog(context, app.packageName,
                    onHide = { onHide(app) },
                    onTop = { onMoveTop(app) }
                ).show()
                true
            }
        } else if (holder is RectVH) {
            holder.binding.txtAppName.text = pm.getApplicationLabel(app)
            holder.binding.imgIcon.setImageDrawable(pm.getApplicationIcon(app))
            holder.binding.root.setOnClickListener { onClick(app) }
            holder.binding.root.setOnLongClickListener {
                EditAppDialog(context, app.packageName,
                    onHide = { onHide(app) },
                    onTop = { onMoveTop(app) }
                ).show()
                true
            }
        }
    }
}