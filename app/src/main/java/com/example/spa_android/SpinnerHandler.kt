package com.example.spa_android

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.spa_android.databinding.ActivityInfomationBinding

object SpinnerHandler {
    fun setUpSpinnerItem(context: Context, binding: ActivityInfomationBinding) {
        val spinnerItem = context.resources.getStringArray(R.array.spinner_item)
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerItem)
        binding.spinner.adapter = adapter
    }

    fun setUpSpinnerListener(binding: ActivityInfomationBinding) {
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }
    fun setSelectedItem(binding: ActivityInfomationBinding, item: String){
        val adapter = binding.spinner.adapter as ArrayAdapter<String>
        val position = adapter.getPosition(item)
        if(position >=0){
            binding.spinner.setSelection(position)
        }
    }
}