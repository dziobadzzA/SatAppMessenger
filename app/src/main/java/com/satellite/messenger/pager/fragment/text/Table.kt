package com.satellite.messenger.pager.fragment.text

import android.content.Context
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.satellite.messenger.R

fun tableView(table: TableLayout, context: Context, text:List<String>, column: List<String>) {

    // path
    val tableRow = TableRow(context)

    for (j in column.indices) {
        val view = TextView(context, null, 0, R.style.TextViewTable)
        view.text = column[j]
        tableRow.addView(view, j)
    }

    table.addView(tableRow, 0)


    // row
    for (i in text.indices) {

        if (text[i].isNotEmpty()) {

            val tableRow = TableRow(context)
            val rowStr = text[i].split("_")

            for (j in column.indices) {
                val view = TextView(context, null, 0, R.style.TextViewTable)
                view.text = rowStr[j]
                tableRow.addView(view, j)
            }

            table.addView(tableRow, i + 1)
        }
    }
}