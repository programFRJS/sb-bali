package com.baliproject.scoreboardtennis

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.baliproject.scoreboardtennis.databinding.ActivityStartEventBinding
import com.baliproject.scoreboardtennis.databinding.ActivityTimBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Calendar

class StartEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartEventBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val monthNames = arrayOf(
                        "January", "February", "March", "April", "May", "June",
                        "July", "August", "September", "October", "November", "December"
                    )
                    val selectedDate = "$dayOfMonth ${monthNames[month]} $year"
                    binding.buttonPickDate.text = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        binding.buttonSubmitEvent.setOnClickListener {
            val eventName = binding.eventNameEditText.text.toString()
            val court = binding.courtEditText.text.toString()
            val date = binding.buttonPickDate.text.toString()

            val sharedPref = getSharedPreferences("MyAppData", MODE_PRIVATE)
            with (sharedPref.edit()) {
                putString("event", eventName)
                putString("court", court)
                putString("date", date)
                apply()
            }


            val intent = Intent(this@StartEventActivity, MainActivity::class.java)

            if (isValidInput()) {
                Toast.makeText(this, "Event Added", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            } else {
                Toast.makeText(this, "Input Event Not Valid", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun isValidInput(): Boolean {
        var isValid = true

        if (isEmpty(binding.eventNameEditText)) {
            setError(binding.inputEvent, "Event Name cannot be blank")
            isValid = false
        } else {
            clearError(binding.inputEvent)
        }

        if (isEmpty(binding.courtEditText)) {
            setError(binding.inputCourt, "Court cannot be blank")
            isValid = false
        } else {
            clearError(binding.inputCourt)
        }

        val selectedDate = binding.buttonPickDate.text.toString()
        if (selectedDate == "Date" || selectedDate.isBlank()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun isEmpty(editText: TextInputEditText): Boolean {
        return editText.text.isNullOrEmpty()
    }

    private fun setError(inputLayout: TextInputLayout, errorMessage: String) {
        inputLayout.isErrorEnabled = true
        inputLayout.error = errorMessage
    }

    private fun clearError(inputLayout: TextInputLayout) {
        inputLayout.isErrorEnabled = false
    }
}