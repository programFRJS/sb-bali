package com.baliproject.scoreboardtennis

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.baliproject.scoreboardtennis.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    // Variabel untuk menyimpan skor
    private var scoreA = 0
    private var scoreB = 0
    private var clickCountA = 0
    private var clickCountB = 0

    // Menggunakan View Binding untuk akses UI
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menghubungkan binding dengan layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSkorUpA.setOnClickListener {
            when (scoreA) {
                0 -> scoreA += 15
                15 -> scoreA += 15
                30 -> scoreA += 10
                40 -> {}
            }

            binding.tvSkorA.text = scoreA.toString()

            changeButtonOpacity(binding.buttonSkorUpA)
            sendScoresToESP32()
        }

        binding.buttonSkorDownA.setOnClickListener {
            when (scoreA) {
                40 -> scoreA -= 10
                30 -> scoreA -= 15
                15 -> scoreA -= 15
                0 -> {}
            }

            binding.tvSkorA.text = scoreA.toString()

            changeButtonOpacity(binding.buttonSkorDownA)
            sendScoresToESP32()
        }

        binding.buttonSkorUpB.setOnClickListener {
            when (scoreB) {
                0 -> scoreB += 15
                15 -> scoreB += 15
                30 -> scoreB += 10
                40 -> {}
            }

            binding.tvSkorB.text = scoreB.toString()

            changeButtonOpacity(binding.buttonSkorUpB)
            sendScoresToESP32()
        }

        binding.buttonSkorDownB.setOnClickListener {

            when (scoreB) {
                40 -> scoreB -= 10
                30 -> scoreB -= 15
                15 -> scoreB -= 15
                0 -> {}
            }

            binding.tvSkorB.text = scoreB.toString()

            changeButtonOpacity(binding.buttonSkorDownB)
            sendScoresToESP32()
        }
    }

    private fun changeButtonOpacity(button: ImageButton) {
        button.alpha = 0.5f  // Mengatur opacity menjadi 50%
        button.postDelayed({
            button.alpha = 1f  // Mengembalikan opacity ke normal setelah 200 ms
        }, 200)  // Durasi waktu efek opacity
    }

    private fun sendScoresToESP32() {
        val apiService = RetrofitClient.retrofit.create(ApiService::class.java)
        val call = apiService.updateScores(scoreA, scoreB)
        call.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if (response.isSuccessful) {

                    val status = response.body()?.status

                    Log.d("Retrofit", "Response: $status")
                } else {
                    Toast.makeText(this@MainActivity, "Failed to update scores: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {

                Log.e("Network Error", "Error: ${t.message}")
                Toast.makeText(this@MainActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}
