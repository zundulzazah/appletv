package com.appletv.sholat
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button

class EditAppDialog(c: Context, private val pkg: String, private val onHide: ()->Unit, private val onTop: ()->Unit) : Dialog(c) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit_app)
        findViewById<Button>(R.id.btn_hide).setOnClickListener { 
            val hidden = AppListManager.getHidden(context).toMutableSet()
            hidden.add(pkg); AppListManager.setHidden(context, hidden); onHide(); dismiss()
        }
        findViewById<Button>(R.id.btn_top).setOnClickListener { onTop(); dismiss() }
    }
}