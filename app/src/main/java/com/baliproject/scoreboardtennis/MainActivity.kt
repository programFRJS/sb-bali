package com.baliproject.scoreboardtennis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.baliproject.scoreboardtennis.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var scoreA = 0
    private var scoreB = 0
    private var persen = 100
    private var Set = 1
    private var set1A = 0
    private var set1B = 0
    private var set2A = 0
    private var set2B = 0
    private var set3A = 0
    private var set3B = 0
    private var serviceA = 0
    private var serviceB = 0
    private var advantageA = 0
    private var advantageB = 0
    private var reset = 0
    private var pressReset = 0

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val playerA1 = intent.getStringExtra("playerA1")
        val playerA2 = intent.getStringExtra("playerA2")
        val playerB1 = intent.getStringExtra("playerB1")
        val playerB2 = intent.getStringExtra("playerB2")

        binding.tvPlayerA1.text = formatPlayerNameA1(playerA1)
        binding.tvPlayerA2.text = formatPlayerNameA2(playerA2)
        binding.tvPlayerB1.text = formatPlayerNameB1(playerB1)
        binding.tvPlayerB2.text = formatPlayerNameB2(playerB2)

        binding.buttonSet.setOnClickListener{
            Set += 1
            reset = 0
            if (Set >= 4) {
                Set = 1
            }
            sendSetToESP32()
        }

        binding.buttonGamePointUp.setOnClickListener {
            if (Set == 1) {
                set1B++
                reset = 0
                if(set1B > 9) {
                    set1B = 0
                }
            } else if (Set == 2) {
                set2B++
                reset = 0
                if(set2B > 9){
                    set2B = 0
                }
            } else if (Set == 3) {
                set3B++
                reset = 0
                if(set3B > 9){
                    set3B = 0
                }
            }

            sendSetToESP32()
        }

        binding.buttonGamePointDown.setOnClickListener {
            if (Set == 1) {
                set1B--
                reset = 0
                if(set1B < 0) {
                    set1B = 0
                }
            } else if (Set == 2) {
                set2B--
                reset = 0
                if(set2B < 0){
                    set2B = 0
                }
            } else if (Set == 3) {
                set3B--
                reset = 0
                if(set3B < 0 ){
                    set3B = 0
                }
            }

            sendSetToESP32()
        }

        binding.buttonBallA.setOnClickListener{
            serviceA++
            reset = 0
            serviceB = 0
            advantageA = 0
            advantageB = 0
            if (serviceA == 2) {
                serviceA = 0
            }
            sendServiceToESP32()
        }

        binding.buttonBallB.setOnClickListener{
            serviceB++
            reset = 0
            serviceA = 0
            advantageA = 0
            advantageB = 0
            if (serviceB == 2) {
                serviceB = 0
            }
            sendServiceToESP32()
        }

        binding.buttonADA.setOnClickListener{
            advantageA++
            reset = 0
            advantageB = 0
            serviceA = 0
            serviceB = 0
            if (advantageA == 2) {
                advantageA = 0
            }
            sendServiceToESP32()
        }

        binding.buttonADB.setOnClickListener {
            advantageB++
            reset = 0
            advantageA = 0
            serviceA = 0
            serviceB = 0
            if (advantageB == 2) {
                advantageB = 0
            }
            sendServiceToESP32()
        }

        binding.enterTeamButton.setOnClickListener {
            val intent = Intent(this, TimActivity::class.java)
            startActivity(intent)
        }

        binding.buttonBrightnessUp.setOnClickListener {
            if (Set == 1) {
                set1A++
                reset = 0
                if(set1A > 9) {
                    set1A = 0
                }
            } else if (Set == 2) {
                set2A++
                reset = 0
                if(set2A > 9) {
                    set2A = 0
                }
            } else if (Set == 3) {
                set3A++
                reset = 0
                if(set3A > 9) {
                    set3A = 0
                }
            }
            sendSetToESP32()
//            persen += 10
//
//            if (persen > 100) {
//                persen = 100
//            }
//
//            sendBrightnessToESP32()
        }

        binding.buttonBrightnessDown.setOnClickListener {
            if (Set == 1) {
                set1A--
                reset = 0
                if(set1A < 0){
                    set1A = 0
                }
            } else if (Set == 2) {
                set2A--
                reset = 0
                if(set2A < 0){
                    set2A = 0
                }
            } else if (Set == 3) {
                set3A--
                reset = 0
                if(set3A < 0){
                    set3A = 0
                }
            }

            sendSetToESP32()
            //ini diubah
//            persen -= 10
//            if (persen > 0) {
//                persen -= 10
//            }
//
//            sendBrightnessToESP32()
        }

        binding.buttonSkorUpA.setOnClickListener {
            reset = 0
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
            reset = 0
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
            reset = 0
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
            reset = 0
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

        binding.buttonReset.setOnClickListener {
            pressReset++
            if(pressReset == 3){
                scoreA = 0
                scoreB = 0
                Set = 1
                set1A = 0
                set1B = 0
                set2A = 0
                set2B = 0
                set3A = 0
                set3B = 0
                serviceA = 0
                serviceB = 0
                advantageA = 0
                advantageB = 0
                reset = 0
                sendResetToESP32()
            }
        }
    }

    private fun changeButtonOpacity(button: ImageButton) {
        button.alpha = 0.5f  // Mengatur opacity menjadi 50%
        button.postDelayed({
            button.alpha = 1f  // Mengembalikan opacity ke normal setelah 200 ms
        }, 200)  // Durasi waktu efek opacity
    }

    private fun formatPlayerNameA1(playerNameA1: String?): String {
        return if (playerNameA1 != null && playerNameA1.length > 7) {
            playerNameA1.substring(0, 5) + ".."
        } else {
            playerNameA1 ?: "Player A1"
        }
    }

    private fun formatPlayerNameA2(playerNameA2: String?): String {
        return if (playerNameA2 != null && playerNameA2.length > 7) {
            playerNameA2.substring(0, 5) + ".."
        } else {
            playerNameA2 ?: "Player A2"
        }
    }

    private fun formatPlayerNameB1(playerNameB1: String?): String {
        return if (playerNameB1 != null && playerNameB1.length > 7) {
            playerNameB1.substring(0, 5) + ".."
        } else {
            playerNameB1 ?: "Player B1"
        }
    }

    private fun formatPlayerNameB2(playerNameB2: String?): String {
        return if (playerNameB2 != null && playerNameB2.length > 7) {
            playerNameB2.substring(0, 5) + ".."
        } else {
            playerNameB2 ?: "Player B2"
        }
    }

    private fun sendBrightnessToESP32(){
        val apiService = RetrofitClient.retrofit.create(ApiService::class.java)
        val call = apiService.updateBrigthness(persen)

        call.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if (response.isSuccessful) {

                    val status = response.body()?.status

                    Log.d("Retrofit", "Response: $status")
                } else {
                    Toast.makeText(this@MainActivity, "Failed to update brightness: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {

                Log.e("Network Error", "Error: ${t.message}")
                Toast.makeText(this@MainActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendResetToESP32(){
        val apiService = RetrofitClient.retrofit.create(ApiService::class.java)
        val call = apiService.reset(scoreA, scoreB, Set, set1A, set1B, set2A, set2B, set3A, set3B, serviceA, serviceB, advantageA, advantageB, reset)

        call.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if (response.isSuccessful) {

                    val status = response.body()?.status

                    Log.d("Retrofit", "Response: $status")
                } else {
                    Toast.makeText(this@MainActivity, "Failed to reset: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {

                Log.e("Network Error", "Error: ${t.message}")
                Toast.makeText(this@MainActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendSetToESP32(){
        val apiService = RetrofitClient.retrofit.create(ApiService::class.java)
        val call = apiService.updateSet(Set,set1A,set1B,set2A,set2B,set3A,set3B,reset)

        call.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if (response.isSuccessful) {

                    val status = response.body()?.status

                    Log.d("Retrofit", "Response: $status")
                } else {
                    Toast.makeText(this@MainActivity, "Failed to update set: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {

                Log.e("Network Error", "Error: ${t.message}")
                Toast.makeText(this@MainActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendScoresToESP32() {
        val apiService1 = RetrofitClient.retrofit.create(ApiService::class.java)
//        val apiService2 = RetrofitClient.retrofit2.create(ApiService::class.java)
        val call1 = apiService1.updateScores(scoreA,scoreB,reset)
//        val call2 = apiService2.updateScores(scoreA, scoreB)

        call1.enqueue(object : Callback<ResponseData> {
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

//        call2.enqueue(object : Callback<ResponseData> {
//            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
//                if (response.isSuccessful) {
//
//                    val status = response.body()?.status
//
//                    Log.d("Retrofit", "Response: $status")
//                } else {
//                    Toast.makeText(this@MainActivity, "Failed to update scores: ${response.message()}", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
//
//                Log.e("Network Error", "Error: ${t.message}")
//                Toast.makeText(this@MainActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })

    }

    private fun sendServiceToESP32(){
        val apiService = RetrofitClient.retrofit.create(ApiService::class.java)
        val call = apiService.updateService(serviceA,serviceB,advantageA,advantageB,reset)

        call.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if (response.isSuccessful) {

                    val status = response.body()?.status

                    Log.d("Retrofit", "Response: $status")
                } else {
                    Toast.makeText(this@MainActivity, "Failed to update services: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {

                Log.e("Network Error", "Error: ${t.message}")
                Toast.makeText(this@MainActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}
