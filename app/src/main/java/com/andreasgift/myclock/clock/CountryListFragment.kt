package com.andreasgift.myclock.clock

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.andreasgift.myclock.R

/**
 * Fragment to display country list timezone
 */
class CountryListFragment : DialogFragment() {
    lateinit var timezone: String

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return activity?.let {
            val builder = buildDialog(requireContext())
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    private fun buildDialog(context: Context): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose country timezone")
            .setSingleChoiceItems(
                R.array.country_list,
                -1,
                DialogInterface.OnClickListener { dialog, which ->
                    timezone = resources.getStringArray(R.array.country_list)[which]
                }
            )
            .setPositiveButton("SET",
                DialogInterface.OnClickListener { dialog, id ->
                    //Add clock here
                })
            .setNegativeButton("CANCEL",
                DialogInterface.OnClickListener { dialog, id ->
                })
        return builder
    }
}

