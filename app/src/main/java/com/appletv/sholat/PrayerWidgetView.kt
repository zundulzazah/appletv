package com.appletv.sholat
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.cardview.widget.CardView

class PrayerWidgetView @JvmOverloads constructor(c: Context, a: AttributeSet? = null) : CardView(c, a) {
    private val v = LayoutInflater.from(c).inflate(R.layout.view_prayer_widget, this, true)
    fun setTimings(t: Timings) {
        v.findViewById<TextView>(R.id.txt_subuh).text = "Subuh ${t.Fajr}"
        v.findViewById<TextView>(R.id.txt_dzuhur).text = "Dzuhur ${t.Dhuhr}"
        v.findViewById<TextView>(R.id.txt_ashar).text = "Ashar ${t.Asr}"
        v.findViewById<TextView>(R.id.txt_maghrib).text = "Maghrib ${t.Maghrib}"
        v.findViewById<TextView>(R.id.txt_isya).text = "Isya ${t.Isha}"
        // Silent reminder - tidak ada suara
        v.findViewById<TextView>(R.id.txt_next).text = "Mode Silent - Pengingat Visual"
    }
}