package com.andreasgift.myclock.Clock

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.andreasgift.myclock.ClockData.ClockViewModel
import com.andreasgift.myclock.R
import kotlinx.android.synthetic.main.city_list_dialog.view.*


/**
 * Fragment to display country list timezone
 */
class CountryListFragment : DialogFragment() {
    lateinit var timezone: String

    private lateinit var clockViewModel: ClockViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clockViewModel = ViewModelProviders.of(this).get(ClockViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return activity?.let {
            val builder = buildDialog(requireContext())
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    private fun buildDialog(context: Context): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)

        val countryList: Array<String> = java.util.TimeZone.getAvailableIDs()
        countryList.set(0, "Local")

        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.city_list_dialog, null)
        val mAdapter = ArrayAdapter<String>(
            context,
            android.R.layout.simple_list_item_single_choice,
            countryList
        )
        view.city_list.adapter = mAdapter
        view.city_list.setOnItemClickListener { adapterView, view, i, l ->
            timezone = mAdapter.getItem(i)!!
        }
        view.city_filter.addTextChangedListener {
            mAdapter.filter.filter(it.toString())
        }

        builder.setTitle("Choose country timezone")
            .setView(view)
            .setPositiveButton("SET",
                DialogInterface.OnClickListener { dialog, id ->
                    if (timezone.equals("Local")) {
                        clockViewModel.insertClock(Clock())
                    } else {
                        clockViewModel.insertClock(Clock(timezone))
                    }
                })
            .setNegativeButton("CANCEL",
                DialogInterface.OnClickListener { dialog, id ->
                })
        return builder
    }
}

