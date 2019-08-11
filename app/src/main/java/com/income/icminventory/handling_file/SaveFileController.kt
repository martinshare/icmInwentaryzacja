package com.income.icminventory.handling_file

import android.os.Environment
import com.income.icminventory.R
import com.income.icminventory.database.DBContext
import com.income.icminventory.fragments.abstraction.FragmentBase
import com.income.icminventory.utilities.toast
import com.income.icminventory.utilities.todayDate
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

/***********Zapis pliku CSV do telefonu (pusty plik albo z pozycjami)*****/
class SaveFileController(val fragmentBase: FragmentBase, val dbContext: DBContext) {

    fun saveItems(isEmptyFile: Boolean = false) {
        saveItemsToCSV(isEmptyFile, preparePosToSave(isEmptyFile))
    }

    private fun preparePosToSave(isEmptyFile: Boolean): String {
        val sb = StringBuilder()

        sb.append(fragmentBase.activity.baseContext.resources.getString(R.string.header_of_exported_file_for_es_systems_k) + "\n")

        if (!isEmptyFile) {
            val iterator = dbContext.items.queryList().iterator()
            while (iterator.hasNext()) {
                val item = iterator.next()
                sb.append(item.id.toString() + ";") /*id*/
                sb.append(item.code + ";")

                /*region for es systems k company*/
                sb.append(item.supplierId + ";")
                sb.append(item.orderId + ";")
                /*endregion*/

                sb.append(item.supportCode + ";")
                sb.append(item.shortName + ";")
                sb.append(item.name + ";")
                sb.append(item.oldLocation + ";")
                sb.append(item.startNumber.toString() + ";")
                sb.append(item.endNumber.toString() + ";")
                sb.append(item.itemState + ";")
                sb.append(dbContext.users.queryList().last().name + ";")
                sb.append("\n")
            }
        }
        return sb.toString()
    }

    /* Internal storageItems */
    private fun saveItemsToCSV(isEmptyFile: Boolean, content: String) {
        val date = todayDate(fragmentBase.activity.baseContext)
        val path = Environment.getExternalStorageDirectory().toString()

        val folder = File(path)
        if (!folder.exists()) {
            try {
                folder.mkdir()
            } catch (e: Exception) {
                fragmentBase.toast(e.toString())
            }
        }

        val pathNotEmptyString = path + "/Export_" + date.replace(":", "_") + ".csv"
        val pathEmptyString = path + "/Export_empty_" + date.replace(":", "_") + ".csv"

        val file = File(if (isEmptyFile) pathEmptyString else pathNotEmptyString)
        val out: FileOutputStream
        val myOutWriter: OutputStreamWriter
            out = FileOutputStream(file)
            myOutWriter = OutputStreamWriter(out)
            myOutWriter.append(content)
            myOutWriter.close()
            out.close()
    }
}