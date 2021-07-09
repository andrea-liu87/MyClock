package com.andreasgift.myclock.About

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.andreasgift.myclock.R
import com.andreasgift.myclock.databinding.DialogAboutBinding

class AboutDialog : DialogFragment() {
    private var _binding: DialogAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            _binding = DialogAboutBinding.inflate(inflater, null, false)
            val view = binding.root

            binding.donationButton.setOnClickListener {
                openDonationPage()
            }

            builder.setView(view)
                .setPositiveButton(getString(R.string.ok), null)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

    private fun openDonationPage() {
        val url = "https://ko-fi.com/snufflesrea"
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(this.requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }
}