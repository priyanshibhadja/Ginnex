package com.example.ginnex

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ginnex.databinding.FragmentLeaveBinding
import java.util.Locale


class LeaveFragment : Fragment() {

    // assign the _binding variable initially to null and
    // also when the view is destroyed again it has to be set to null
    private var _binding: FragmentLeaveBinding? = null

    // with the backing property of the kotlin we extract
    // the non null value of the _binding
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentLeaveBinding.inflate(inflater, container, false)

        val calendarBox = Calendar.getInstance()
        val dateBox = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            calendarBox.set(Calendar.YEAR, year)
            calendarBox.set(Calendar.MONTH, month)
            calendarBox.set(Calendar.DAY_OF_MONTH, day)
            updateText(calendarBox)
        }
        val dateBox2 = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            calendarBox.set(Calendar.YEAR, year)
            calendarBox.set(Calendar.MONTH, month)
            calendarBox.set(Calendar.DAY_OF_MONTH, day)
            updateText2(calendarBox)
        }
        binding.textDate.setOnClickListener {
            DatePickerDialog(
                requireContext(), dateBox, calendarBox.get(Calendar.YEAR), calendarBox.get(Calendar.MONTH),
                calendarBox.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.textDate2.setOnClickListener {
            DatePickerDialog(
                requireContext(), dateBox2, calendarBox.get(Calendar.YEAR), calendarBox.get(Calendar.MONTH),
                calendarBox.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        binding.buttonSubmit.setOnClickListener{
            val leaveStart = binding.textDate.text.toString()
            val leaveEnd = binding.textDate2.text.toString()

            if (leaveStart.isEmpty() || leaveEnd.isEmpty()) {
                Toast.makeText(requireContext(),"Start and end date can't be empty!",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),"Leave Request Submitted",Toast.LENGTH_SHORT).show()}

        }
        return binding.root

    }
    private fun updateText(calendar: Calendar) {
        val dateFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(dateFormat, Locale.UK)
        binding.textDate.setText(sdf.format(calendar.time))
    }
    private fun updateText2(calendar: Calendar) {
        val dateFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(dateFormat, Locale.UK)
        binding.textDate2.setText(sdf.format(calendar.time))
    }


}