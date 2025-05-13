package com.baliproject.scoreboardtennis.UI.Activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.baliproject.scoreboardtennis.API.ApiConfig
import com.baliproject.scoreboardtennis.API.ApiService
import com.baliproject.scoreboardtennis.API.CreateGame.CreateGameRequest
import com.baliproject.scoreboardtennis.API.CreateGame.CreateGameResponse
import com.baliproject.scoreboardtennis.API.ResetGame.ResetGameResponse
import com.baliproject.scoreboardtennis.API.ResponseData
import com.baliproject.scoreboardtennis.API.RetrofitClient
import com.baliproject.scoreboardtennis.API.Score.SetScoreResponse
import com.baliproject.scoreboardtennis.API.SetAdvantage.SetAdvantageResetResponse
import com.baliproject.scoreboardtennis.API.SetAdvantage.SetAdvantageResponse
import com.baliproject.scoreboardtennis.API.SetService.SetServiceResetResponse
import com.baliproject.scoreboardtennis.API.SetService.SetServiceResponse
import com.baliproject.scoreboardtennis.API.UpdateSetScore.UpdateSetScoreResponse
import com.baliproject.scoreboardtennis.Database.AppDatabase
import com.baliproject.scoreboardtennis.databinding.ActivityMainBinding
import com.baliproject.scoreboardtennis.databinding.ActivityStartEventBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StartEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartEventBinding

    private var scoreA = 0
    private var scoreB = 0
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
    private lateinit var slug: String

    private var playerA1: String? = null
    private var playerA2: String? = null
    private var playerB1: String? = null
    private var playerB2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartEventBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lifecycleScope.launch {
            RetrofitClient.init(this@StartEventActivity)
            val db = AppDatabase.getDatabase(applicationContext)
            val match = db.matchDao().getMatch()
            slug = match?.slug ?: ""
            Log.d("StartEventActivity", "Slug loaded from DB: $slug")
        }

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

        binding.buttonToSetting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        binding.buttonLoadMatch.setOnClickListener {
            val db = AppDatabase.getDatabase(this)
            lifecycleScope.launch {
                val match = db.matchDao().getMatch()
                if (match != null) {
                    AlertDialog.Builder(this@StartEventActivity)
                        .setTitle("Load Match")
                        .setMessage("Sure to load match?")
                        .setPositiveButton("Yes") { _, _ ->
                            // Isi dari tombol Yes
                            playerA1 = match.playerA1
                            playerA2 = match.playerA2
                            playerB1 = match.playerB1
                            playerB2 = match.playerB2
                            scoreA = match.scoreA
                            scoreB = match.scoreB
                            Set = match.setNumber
                            set1A = match.set1A
                            set1B = match.set1B
                            set2A = match.set2A
                            set2B = match.set2B
                            set3A = match.set3A
                            set3B = match.set3B
                            serviceA = match.serviceA
                            serviceB = match.serviceB
                            advantageA = match.advantageA
                            advantageB = match.advantageB
                            slug = match.slug.toString()

                            sendSetToESP32()
                            sendScoresToESP32()
                            sendServiceToESP32()
                            sendScoreToServer("a", scoreA)
                            sendScoreToServer("b", scoreB)

                            if (serviceA == 1) {
                                sendServiceToServer("a")
                                resetAdvantageToServer()
                            } else if (serviceB == 1) {
                                sendServiceToServer("b")
                                resetAdvantageToServer()
                            } else if (advantageA == 1) {
                                sendAdvantageToServer("a")
                                resetServiceToServer()
                            } else if (advantageB == 1) {
                                sendAdvantageToServer("b")
                                resetServiceToServer()
                            }

                            sendSetScoreToServer("a", 1, set1A)
                            sendSetScoreToServer("a", 2, set2A)
                            sendSetScoreToServer("a", 3, set3A)
                            sendSetScoreToServer("b", 1, set1B)
                            sendSetScoreToServer("b", 2, set2B)
                            sendSetScoreToServer("b", 3, set3B)

                            playerA1?.let { it1 -> playerA2?.let { it2 ->
                                playerB1?.let { it3 -> playerB2?.let { it4 ->
                                    sendPlayerToESP32(it1, it2, it3, it4)
                                }}
                            }}

                            val intent = Intent(this@StartEventActivity, MainActivity::class.java).apply {
                                putExtra("scoreA", scoreA)
                                putExtra("scoreB", scoreB)
                                putExtra("Set", Set)
                                putExtra("set1A", set1A)
                                putExtra("set1B", set1B)
                                putExtra("set2A", set2A)
                                putExtra("set2B", set2B)
                                putExtra("set3A", set3A)
                                putExtra("set3B", set3B)
                                putExtra("serviceA", serviceA)
                                putExtra("serviceB", serviceB)
                                putExtra("advantageA", advantageA)
                                putExtra("advantageB", advantageB)
                                putExtra("playerA1", match.playerA1)
                                putExtra("playerA2", match.playerA2)
                                putExtra("playerB1", match.playerB1)
                                putExtra("playerB2", match.playerB2)
                                putExtra("event", match.eventName)
                                putExtra("court", match.court)
                                putExtra("date", match.date)
                                putExtra("slug", slug)
                            }

                            startActivity(intent)
                        }
                        .setNegativeButton("No", null)
                        .show()
                } else {
                    Toast.makeText(this@StartEventActivity, "No saved match found", Toast.LENGTH_SHORT).show()
                }
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

    private fun sendSetToESP32(){
        val apiService = RetrofitClient.retrofit.create(ApiService::class.java)
        val call = apiService.updateSet(Set,set1A,set1B,set2A,set2B,set3A,set3B,reset)

        call.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if (response.isSuccessful) {

                    val status = response.body()?.status

                    Log.d("Retrofit", "Response: $status")
                } else {
                    Toast.makeText(this@StartEventActivity, "Failed to update set: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {

                Log.e("Network Error", "Error: ${t.message}")
                Toast.makeText(this@StartEventActivity, "Not connected to scoreboard", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendScoresToESP32() {
        val apiService1 = RetrofitClient.retrofit.create(ApiService::class.java)
        val call1 = apiService1.updateScores(scoreA,scoreB,reset)

        call1.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if (response.isSuccessful) {

                    val status = response.body()?.status

                    Log.d("Retrofit", "Response: $status")
                } else {
                    Toast.makeText(this@StartEventActivity, "Failed to update scores: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {

                Log.e("Network Error", "Error: ${t.message}")
                Toast.makeText(this@StartEventActivity, "Not connected to scoreboard", Toast.LENGTH_SHORT).show()
            }
        })



    }

    private fun sendScoreToServer(player: String, score: Int) {
        val apiService = ApiConfig.getApiService()

        apiService.setScore(slug, player, score).enqueue(object : Callback<SetScoreResponse> {
            override fun onResponse(call: Call<SetScoreResponse>, response: Response<SetScoreResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val updatedScore = response.body()?.data?.score
                    val playerUpdated = response.body()?.data?.player
                    Log.d("SetScore", "Score updated: Player $playerUpdated -> $updatedScore")
                } else {
                    Log.e("SetScore", "Failed to update score: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SetScoreResponse>, t: Throwable) {
                Log.e("SetScore", "Error: ${t.message}")
            }
        })
    }

    private fun sendServiceToServer(player: String) {
        val apiService = ApiConfig.getApiService()
        apiService.setService(slug, player).enqueue(object : Callback<SetServiceResponse> {
            override fun onResponse(call: Call<SetServiceResponse>, response: Response<SetServiceResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val updatedPlayer = response.body()?.data?.player
                    Log.d("SetService", "Service updated to player: $updatedPlayer")
                } else {
                    Log.e("SetService", "Failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SetServiceResponse>, t: Throwable) {
                Log.e("SetService", "Error: ${t.message}")
            }
        })
    }

    private fun sendAdvantageToServer(player: String) {
        val apiService = ApiConfig.getApiService()
        apiService.setAdvantage(slug, player).enqueue(object : Callback<SetAdvantageResponse> {
            override fun onResponse(call: Call<SetAdvantageResponse>, response: Response<SetAdvantageResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val updatedPlayer = response.body()?.data?.player
                    Log.d("SetService", "Service updated to player: $updatedPlayer")
                } else {
                    Log.e("SetService", "Failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SetAdvantageResponse>, t: Throwable) {
                Log.e("SetService", "Error: ${t.message}")
            }
        })
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
                    Toast.makeText(this@StartEventActivity, "Failed to update services: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {

                Log.e("Network Error", "Error: ${t.message}")
                Toast.makeText(this@StartEventActivity, "Not connected to scoreboard", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun resetServiceToServer() {
        val apiService = ApiConfig.getApiService()
        apiService.resetService(slug).enqueue(object : Callback<SetServiceResetResponse> {
            override fun onResponse(call: Call<SetServiceResetResponse>, response: Response<SetServiceResetResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    Log.d("ResetService", "Service reset successfully")
                } else {
                    Log.e("ResetService", "Failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SetServiceResetResponse>, t: Throwable) {
                Log.e("ResetService", "Error: ${t.message}")
            }
        })
    }

    private fun resetAdvantageToServer() {
        val apiService = ApiConfig.getApiService()
        apiService.resetAdvantage(slug).enqueue(object : Callback<SetAdvantageResetResponse> {
            override fun onResponse(call: Call<SetAdvantageResetResponse>, response: Response<SetAdvantageResetResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    Log.d("ResetService", "Service reset successfully")
                } else {
                    Log.e("ResetService", "Failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SetAdvantageResetResponse>, t: Throwable) {
                Log.e("ResetService", "Error: ${t.message}")
            }
        })
    }

    private fun sendSetScoreToServer(player: String, setNumber: Int, score: Int) {
        val apiService = ApiConfig.getApiService()
        apiService.updateSetScore(slug, player, setNumber, score)
            .enqueue(object : Callback<UpdateSetScoreResponse> {
                override fun onResponse(
                    call: Call<UpdateSetScoreResponse>,
                    response: Response<UpdateSetScoreResponse>
                ) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        Log.d("SetScore", "Updated: ${response.body()?.message}")
                    } else {
                        Log.e("SetScore", "Failed: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<UpdateSetScoreResponse>, t: Throwable) {
                    Log.e("SetScore", "Error: ${t.message}")
                }
            })
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
                    Toast.makeText(this@StartEventActivity, "Failed to enter players: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                Log.e("Network Error", "Error: ${t.message}")
                Toast.makeText(this@StartEventActivity, "Not connected to scoreboard", Toast.LENGTH_SHORT).show()
            }
        })
    }


}
