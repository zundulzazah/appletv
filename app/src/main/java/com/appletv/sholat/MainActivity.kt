package com.appletv.sholat

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Collections

class MainActivity : AppCompatActivity() {

    private lateinit var prayerWidget: PrayerWidgetView
    private lateinit var rvApps: RecyclerView
    private lateinit var adapter: TvAppAdapter
    private val apps = mutableListOf<ApplicationInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prayerWidget = findViewById(R.id.prayer_widget)
        rvApps = findViewById(R.id.rv_apps)

        setupApps()
        loadPrayerTime()
    }

    override fun onResume() {
        super.onResume()
        // reload style kalau habis ganti di Settings
        if(::adapter.isInitialized) {
            adapter.updateStyle(AppListManager.getStyle(this))
        }
    }

    private fun setupApps() {
        val pm = packageManager
        val allApps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { pm.getLaunchIntentForPackage(it.packageName) != null }
            .filter { !AppListManager.getHidden(this).contains(it.packageName) }
            .toMutableList()

        // urutkan sesuai saved order
        val savedOrder = AppListManager.getOrder(this)
        if (savedOrder.isNotEmpty()) {
            allApps.sortBy { savedOrder.indexOf(it.packageName).takeIf { it >= 0 } ?: 999 }
        }

        apps.clear()
        apps.addAll(allApps)

        val spanCount = if(AppListManager.getStyle(this) == "circle") 5 else 3
        rvApps.layoutManager = GridLayoutManager(this, spanCount)

        adapter = TvAppAdapter(this, apps,
            onClick = { appInfo ->
                val intent = pm.getLaunchIntentForPackage(appInfo.packageName)
                startActivity(intent)
            },
            onMoveTop = { appInfo ->
                apps.remove(appInfo)
                apps.add(0, appInfo)
                adapter.notifyDataSetChanged()
                AppListManager.saveOrder(this, apps.map { it.packageName })
                Toast.makeText(this, "${pm.getApplicationLabel(appInfo)} dipindah ke depan", Toast.LENGTH_SHORT).show()
            },
            onHide = { appInfo ->
                apps.remove(appInfo)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "App disembunyikan", Toast.LENGTH_SHORT).show()
            }
        )
        rvApps.adapter = adapter

        // DRAG & DROP untuk pindah posisi
        val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, 0
        ) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val from = vh.adapterPosition
                val to = target.adapterPosition
                Collections.swap(apps, from, to)
                adapter.notifyItemMoved(from, to)
                AppListManager.saveOrder(this@MainActivity, apps.map { it.packageName })
                return true
            }
            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {}
            override fun isLongPressDragEnabled(): Boolean = true
        })
        helper.attachToRecyclerView(rvApps)
    }

    private fun loadPrayerTime() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.aladhan.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(PrayerApi::class.java)
        lifecycleScope.launch {
            try {
                val res = api.getTimings("Semarang", "Indonesia", 20)
                prayerWidget.setTimings(res.data.timings)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }
}