package com.andreasgift.myclock.clock

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.andreasgift.myclock.clockdata.ClockViewModel
import com.andreasgift.myclock.databinding.CityListDialogBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * Fragment to display country list timezone
 */
@AndroidEntryPoint
class CountryListFragment : DialogFragment() {
    private var _binding: CityListDialogBinding? = null
    private val binding get() = _binding!!

    lateinit var timezone: String

    private val clockViewModel by activityViewModels<ClockViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return activity?.let {
            Log.d("TAG", "POINTN1")
            val builder = buildDialog(requireContext())
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    private fun buildDialog(context: Context): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)

        val countryList: Array<String> = java.util.TimeZone.getAvailableIDs()
        countryList.set(0, "Local")

        val inflater = requireActivity().layoutInflater
        _binding = CityListDialogBinding.inflate(inflater, null, false)
        val view = binding.root
        Log.d("TAG", "POINTN2")
        val mAdapter = ArrayAdapter<String>(
            context,
            android.R.layout.simple_list_item_single_choice,
            countryList
        )
        binding.cityList.adapter = mAdapter
        binding.cityList.setOnItemClickListener { _, view, i, l ->
            timezone = mAdapter.getItem(i)!!
        }
        binding.cityFilter.addTextChangedListener {
            mAdapter.filter.filter(it.toString())
        }

        builder.setTitle("Choose country timezone")
            .setView(view)
            .setPositiveButton(
                "SET",
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

