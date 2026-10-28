package com.appletv.sholat
import android.content.Context

object AppListManager {
    private fun pref(c: Context) = c.getSharedPreferences("launcher_pref", 0)
    fun saveOrder(c: Context, list: List<String>) = pref(c).edit().putString("order", list.joinToString(",")).apply()
    fun getOrder(c: Context) = pref(c).getString("order","")!!.split(",").filter { it.isNotEmpty() }
    fun setStyle(c: Context, style: String) = pref(c).edit().putString("style", style).apply()
    fun getStyle(c: Context) = pref(c).getString("style","circle")!!
    fun setHidden(c: Context, hidden: Set<String>) = pref(c).edit().putStringSet("hidden", hidden).apply()
    fun getHidden(c: Context) = pref(c).getStringSet("hidden", emptySet())!!
}