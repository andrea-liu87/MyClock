package com.andreasgift.myclock.About

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.andreasgift.myclock.R
import kotlinx.android.synthetic.main.dialog_about.view.*

class AboutDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_about, null)

            view.donation_button.setOnClickListener {
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
        if (intent.resolveActivity(activity!!.packageManager) != null) {
            startActivity(intent)
        }
    }
}