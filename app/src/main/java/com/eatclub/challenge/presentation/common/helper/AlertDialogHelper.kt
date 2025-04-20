package com.eatclub.challenge.presentation.common.helper

import android.app.AlertDialog
import android.content.Context
import com.eatclub.challenge.R

class AlertDialogHelper(private val context: Context) {
    fun show(
        title: String,
        message: String,
        onPositiveButtonClick: () -> Unit,
        onNegativeButtonClick: (() -> Unit)? = null,
        positiveButtonText: String = context.getString(R.string.yes),
        negativeButtonText: String = context.getString(R.string.cancel),
    ) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ ->
                onPositiveButtonClick()
            }
            .setNegativeButton(negativeButtonText) { _, _ ->
                onNegativeButtonClick?.invoke()
            }
            .show()
    }
}
