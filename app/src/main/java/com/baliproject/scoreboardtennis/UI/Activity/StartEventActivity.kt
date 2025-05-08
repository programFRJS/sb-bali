package com.baliproject.scoreboardtennis.UI.Activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.baliproject.scoreboardtennis.API.ApiConfig
import com.baliproject.scoreboardtennis.API.CreateGame.CreateGameRequest
import com.baliproject.scoreboardtennis.API.CreateGame.CreateGameResponse
import com.baliproject.scoreboardtennis.databinding.ActivityStartEventBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
            with(sharedPref.edit()) {
                putString("event", eventName)
                putString("court", court)
                putString("date", date)
                apply()
            }



            if (isValidInput()) {
                val name = binding.eventNameEditText.text.toString()
                val court = binding.courtEditText.text.toString()
                val rawDate = binding.buttonPickDate.text.toString()

                // Format tanggal ke yyyy-MM-dd
                val date = convertToApiDateFormat(rawDate)

                val request = CreateGameRequest(name, court, date)
                val apiService = ApiConfig.getApiService()

                apiService.createGame(request).enqueue(object : Callback<CreateGameResponse> {
                    override fun onResponse(call: Call<CreateGameResponse>, response: Response<CreateGameResponse>) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            Toast.makeText(this@StartEventActivity, "Game Created", Toast.LENGTH_SHORT).show()

                            // Simpan slug jika perlu
                            val slug = response.body()?.data?.slug
                            val intent = Intent(this@StartEventActivity, MainActivity::class.java)
                            intent.putExtra("slug", slug)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@StartEventActivity, "Gagal buat game", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<CreateGameResponse>, t: Throwable) {
                        Toast.makeText(this@StartEventActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
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

    private fun convertToApiDateFormat(date: String): String {
        // dari "3 May 2025" ke "2025-05-03"
        return try {
            val inputFormat = SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            outputFormat.format(inputFormat.parse(date)!!)
        } catch (e: Exception) {
            ""
        }
    }

}
