package com.andreasgift.myclock.clock


import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andreasgift.myclock.Helper.Constants
import com.andreasgift.myclock.clockdata.ClockViewModel
import com.andreasgift.myclock.databinding.FragmentClockBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * Fragment for Clock
 */
@AndroidEntryPoint
class ClockFragment : Fragment() {
    private var _binding: FragmentClockBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPref: SharedPreferences

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ClockRecyclerViewAdapter

    private val clockViewModel by activityViewModels<ClockViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClockBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        viewAdapter = ClockRecyclerViewAdapter(clockViewModel.isAnalog)
        recyclerView = binding.clockRv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
            adapter = viewAdapter
        }
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )


        val swipeCallback = object : SwipeCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val clock: Clock? = viewAdapter.getItem(position)
                clock?.let { clockViewModel.deleteClock(clock) }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        binding.clockSwitch.setOnCheckedChangeListener(switchCheckListener)
        binding.addClockFab.setOnClickListener(fabClickListener)

        clockViewModel.allClock.observe(this.viewLifecycleOwner, Observer {
            viewAdapter.submitData(clockViewModel.allClock.value as ArrayList<Clock>)
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clockVM = clockViewModel
    }

    //Listener for Analog or Digital Clock Switch
    val switchCheckListener: CompoundButton.OnCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { switchView, isChecked ->
            if (isChecked) {
                sharedPref.edit {
                    putBoolean(Constants.PREF_KEY_MANUAL_CLOCK, true)
                    commit()
                    viewAdapter.changeAnalog(true)
                }
            } else {
                sharedPref.edit {
                    putBoolean(Constants.PREF_KEY_MANUAL_CLOCK, false)
                    viewAdapter.changeAnalog(false)
                }
            }
            viewAdapter.notifyDataSetChanged()
        }

    //Listener for fab button
    val fabClickListener: View.OnClickListener = View.OnClickListener {
        val dialog = CountryListFragment()
        dialog.show(requireActivity().supportFragmentManager, "Country List Fragment")
    }
}
