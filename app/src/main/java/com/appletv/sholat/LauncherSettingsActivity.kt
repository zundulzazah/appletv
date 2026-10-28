package com.appletv.sholat
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class LauncherSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        findViewById<Button>(R.id.btn_style_circle).setOnClickListener {
            AppListManager.setStyle(this, "circle"); finish()
        }
        findViewById<Button>(R.id.btn_style_rect).setOnClickListener {
            AppListManager.setStyle(this, "rectangle"); finish()
        }
    }
}