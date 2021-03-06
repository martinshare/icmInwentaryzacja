package com.income.icminventory.asynctasks

import android.annotation.SuppressLint
import android.app.Activity
import android.os.AsyncTask
import com.income.icminventory.R
import com.income.icminventory.activities.MainActivity
import com.income.icminventory.views.InfoDialogFragment
import com.income.icminventory.views.ProgressDialogFragment
import timber.log.Timber

@SuppressLint("StaticFieldLeak")
class AsyncTaskWithProgress(val activity: Activity, private val doInBackground: () -> Unit, private val onPostExecute: () -> Unit) : AsyncTask<Void, Void, Boolean>() {

    private var progressDialogFragment: ProgressDialogFragment? = (activity as MainActivity).progressDialogFragment

    private val ft = (activity as MainActivity).fragmentManager
    var exc: Exception? = null

    override fun onPreExecute() {
        progressDialogFragment?.show(ft, "dialog")
        (activity as MainActivity).isDialogShowed = true
    }

    override fun doInBackground(vararg params: Void?): Boolean {
        try {
            doInBackground.invoke()
        } catch (e: Exception) {
            exc = e
        }
        return exc != null
    }

    override fun onPostExecute(result: Boolean) {
        if (result) {
            if ((activity as MainActivity).isActivityResume) {
                InfoDialogFragment({ progressDialogFragment?.dismiss() }, activity.baseContext.getString(R.string.error_ocurred) + " ${exc?.toString()}").show((activity as MainActivity).fragmentManager, "dialog")
                Timber.d("Error: ${exc?.localizedMessage?.toString()}")
            }
        } else {
            onPostExecute.invoke()
            progressDialogFragment?.dismiss()
            (activity as MainActivity).isDialogShowed = false
        }
    }
}