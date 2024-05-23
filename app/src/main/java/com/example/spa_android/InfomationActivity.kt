package com.example.spa_android

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.databinding.ActivityInfomationBinding

class InfomationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfomationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfomationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backBtn.setOnClickListener {
            intent.putExtra("resultData","true")
            setResult(RESULT_OK,intent)
            finish()

        }

        setUpSpinnerItem()
        setUpSpinnerHandler()

    }

    private fun setUpSpinnerItem(){
        val spinnerItem = resources.getStringArray(R.array.spinner_item)
        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,spinnerItem)
        binding.spinner.adapter =adapter
    }
    private fun setUpSpinnerHandler(){
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //item이 선택되었을 때
               // binding.tvYear.text = "select : ${binding.spinner.getItemAtPosition(position)}"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
}