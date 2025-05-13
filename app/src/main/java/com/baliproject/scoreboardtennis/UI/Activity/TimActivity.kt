package com.baliproject.scoreboardtennis.UI.Activity

import SetPlayerRequest
import SetPlayerResponse
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.baliproject.scoreboardtennis.API.ApiConfig
import com.baliproject.scoreboardtennis.API.ApiService
import com.baliproject.scoreboardtennis.API.ResponseData
import com.baliproject.scoreboardtennis.R
import com.baliproject.scoreboardtennis.API.RetrofitClient
import com.baliproject.scoreboardtennis.Utils.AlphanumericDotCommaFilter
import com.baliproject.scoreboardtennis.databinding.ActivityTimBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TimActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: MaterialToolbar = findViewById(R.id.scan_menu_appbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setPlayerInputWatcher(binding.playerA1EditText)
        setPlayerInputWatcher(binding.playerB1EditText)
        setPlayerInputWatcher(binding.playerA2EditText)
        setPlayerInputWatcher(binding.playerB2EditText)

        val maxLengthFilter = InputFilter.LengthFilter(11)
        val charFilter = AlphanumericDotCommaFilter()
        binding.playerA1EditText.filters = arrayOf(maxLengthFilter, charFilter)
        binding.playerB1EditText.filters = arrayOf(maxLengthFilter, charFilter)
        binding.playerA2EditText.filters = arrayOf(maxLengthFilter, charFilter)
        binding.playerB2EditText.filters = arrayOf(maxLengthFilter, charFilter)

        binding.buttonSubmitTim.setOnClickListener {
            val playerA1 = binding.playerA1EditText.text.toString()
            val playerA2 = binding.playerA2EditText.text.toString()
            val playerB1 = binding.playerB1EditText.text.toString()
            val playerB2 = binding.playerB2EditText.text.toString()

            if (isValidInput()) {
                Toast.makeText(this, "Player Name Added", Toast.LENGTH_SHORT).show()
                sendPlayerToESP32(playerA1, playerA2, playerB1, playerB2)

                // Kirim ke MainActivity
                val resultIntent = Intent().apply {
                    putExtra("playerA1", playerA1)
                    putExtra("playerA2", playerA2)
                    putExtra("playerB1", playerB1)
                    putExtra("playerB2", playerB2)
                }
                setResult(RESULT_OK, resultIntent)

                // Ambil slug dari Intent
                val slug = intent.getStringExtra("slug")
                if (!slug.isNullOrEmpty()) {
                    val request = SetPlayerRequest(
                        playerA = listOf(playerA1, playerA2),
                        playerB = listOf(playerB1, playerB2)
                    )

                    val apiService = ApiConfig.getApiService()
                    apiService.setPlayerNames(slug, request)
                        .enqueue(object : Callback<SetPlayerResponse> {
                            override fun onResponse(call: Call<SetPlayerResponse>, response: Response<SetPlayerResponse>) {
                                if (response.isSuccessful && response.body()?.success == true) {
//                                    Toast.makeText(this@TimActivity, "Nama pemain berhasil disimpan", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Log.e("TimActivity", "Failed: ${response.body()?.message ?: "Unknown error"}")

                                }
                            }

                            override fun onFailure(call: Call<SetPlayerResponse>, t: Throwable) {
                                Log.e("TimActivity", "Error: ${t.message}")
                            }
                        })
                } else {
                    Log.e("TimActivity", "Slug tidak ditemukan")
                }
            } else {
                Toast.makeText(this, "Input Player cannot be blank", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun setPlayerInputWatcher(editText: TextInputEditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                charSequence?.let {
                    // Ubah semua input menjadi huruf kapital
                    val capitalizedText = it.toString().toUpperCase()
                    if (it.toString() != capitalizedText) {
                        editText.setText(capitalizedText)
                        editText.setSelection(capitalizedText.length) // Set cursor di akhir teks
                    }
                }
            }

            override fun afterTextChanged(editable: Editable?) {}
        })
    }

    private fun isValidInput(): Boolean {
        var isValid = true

        if (isEmpty(binding.playerA1EditText)) {
            setError(binding.inputPlayerA1, "Player A1 cannot be blank")
            isValid = false
        } else {
            clearError(binding.inputPlayerA1)
        }

        if (isEmpty(binding.playerB1EditText)) {
            setError(binding.inputPlayerB1, "Player B1 cannot be blank")
            isValid = false
        } else {
            clearError(binding.inputPlayerB1)
        }

        if (isEmpty(binding.playerA2EditText)) {
            setError(binding.inputPlayerA2, "Player A2 cannot be blank")
            isValid = false
        } else {
            clearError(binding.inputPlayerA2)
        }

        if (isEmpty(binding.playerB2EditText)) {
            setError(binding.inputPlayerB2, "Player B2 cannot be blank")
            isValid = false
        } else {
            clearError(binding.inputPlayerB2)
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

    private fun sendPlayerToESP32(playerA1: String, playerA2: String, playerB1: String, playerB2: String) {
        val apiService1 = RetrofitClient.retrofit.create(ApiService::class.java)
        val call1 = apiService1.enterPlayer(playerA1, playerA2, playerB1, playerB2)

        call1.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if (response.isSuccessful) {
                    val status = response.body()?.status
                    Log.d("Retrofit", "Response: $status")
                } else {
                    Log.e("TimActivity", "Failed to update player: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                Log.e("Network Error", "Error: ${t.message}")
                Toast.makeText(this@TimActivity, "Not connected to scoreboard", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button press
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}