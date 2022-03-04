package com.example.myapplication.Dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class AlertDialog: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage("В вашем словаре нет слов")
                .setPositiveButton("ОК", activity as DialogInterface.OnClickListener)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

