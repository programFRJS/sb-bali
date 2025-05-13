package com.baliproject.scoreboardtennis.UI.Activity

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.baliproject.scoreboardtennis.API.ApiConfig
import com.baliproject.scoreboardtennis.API.ApiService
import com.baliproject.scoreboardtennis.API.ResetGame.ResetGameResponse
import com.baliproject.scoreboardtennis.API.ResponseData
import com.baliproject.scoreboardtennis.R
import com.baliproject.scoreboardtennis.API.RetrofitClient
import com.baliproject.scoreboardtennis.API.Score.SetScoreResponse
import com.baliproject.scoreboardtennis.API.SetAdvantage.SetAdvantageResetResponse
import com.baliproject.scoreboardtennis.API.SetAdvantage.SetAdvantageResponse
import com.baliproject.scoreboardtennis.API.SetService.SetServiceResetResponse
import com.baliproject.scoreboardtennis.API.SetService.SetServiceResponse
import com.baliproject.scoreboardtennis.API.UpdateSetScore.UpdateSetScoreResponse
import com.baliproject.scoreboardtennis.Database.AppDatabase
import com.baliproject.scoreboardtennis.Entity.MatchEntity
import com.baliproject.scoreboardtennis.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

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
    private var pressReset = 0
    private lateinit var slug: String
    private lateinit var binding: ActivityMainBinding

    private var playerA1: String? = null
    private var playerA2: String? = null
    private var playerB1: String? = null
    private var playerB2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Retrofit dengan IP dari Room



        val txtEvent = findViewById<TextView>(R.id.txt_event)
        val txtCourt = findViewById<TextView>(R.id.txt_court)
        val txtTanggal = findViewById<TextView>(R.id.txt_tanggal)

        val sharedPref = getSharedPreferences("MyAppData", MODE_PRIVATE)
        val eventName = sharedPref.getString("event", "")
        val court = sharedPref.getString("court", "")
        val date = sharedPref.getString("date", "")
        slug = intent.getStringExtra("slug") ?: ""

        scoreA = intent.getIntExtra("scoreA", 0)
        scoreB = intent.getIntExtra("scoreB", 0)
        Set = intent.getIntExtra("Set", 1)
        set1A = intent.getIntExtra("set1A", 0)
        set1B = intent.getIntExtra("set1B", 0)
        set2A = intent.getIntExtra("set2A", 0)
        set2B = intent.getIntExtra("set2B", 0)
        set3A = intent.getIntExtra("set3A", 0)
        set3B = intent.getIntExtra("set3B", 0)
        serviceA = intent.getIntExtra("serviceA", 0)
        serviceB = intent.getIntExtra("serviceB", 0)
        advantageA = intent.getIntExtra("advantageA", 0)
        advantageB = intent.getIntExtra("advantageB", 0)

        playerA1 = intent.getStringExtra("playerA1") ?: "Player A1"
        playerA2 = intent.getStringExtra("playerA2") ?: "Player A2"
        playerB1 = intent.getStringExtra("playerB1") ?: "Player B1"
        playerB2 = intent.getStringExtra("playerB2") ?: "Player B2"

        val loadEvent = intent.getStringExtra("event") ?: ""
        val loadCourt = intent.getStringExtra("court") ?: ""
        val loadDate = intent.getStringExtra("date") ?: ""
        val loadSlug = intent.getStringExtra("slug") ?: ""

        txtEvent.text = loadEvent
        txtCourt.text = loadCourt
        txtTanggal.text = loadDate
        binding.tvPlayerA1.text = playerA1
        binding.tvPlayerA2.text = playerA2
        binding.tvPlayerB1.text = playerB1
        binding.tvPlayerB2.text = playerB2
        binding.tvSkorA.text = scoreA.toString()
        binding.tvSkorB.text = scoreB.toString()
        binding.tvGamePointOneA.text = set1A.toString()
        binding.tvGamePointOneB.text = set1B.toString()
        binding. tvGamePointTwoA.text = set2A.toString()
        binding.tvGamePointTwoB.text = set2B.toString()
        binding. tvGamePointThreeA.text = set3A.toString()
        binding.tvGamePointThreeB.text = set3B.toString()

        when {
            serviceA == 1 -> {
                val drawable = ContextCompat.getDrawable(this, R.drawable.tennis_ball)
                drawable?.setBounds(0, 0, dpToPx(30), dpToPx(30)) // Ukuran 30dp

                val span = SpannableString(" ")
                drawable?.let {
                    val imageSpan = ImageSpan(it, ImageSpan.ALIGN_BOTTOM)
                    span.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }

                binding.tvServiceA.text = span
            }
            advantageA == 1 -> {
                binding.tvServiceA.text = "AD"
            }
            else -> {
                binding.tvServiceA.text = "" // Kosongkan jika tidak ada service/advantage
            }
        }



        when {
            serviceB == 1 -> {
                val drawable = ContextCompat.getDrawable(this, R.drawable.tennis_ball)
                drawable?.setBounds(0, 0, dpToPx(30), dpToPx(30)) // Ukuran 30dp

                val span = SpannableString(" ")
                drawable?.let {
                    val imageSpan = ImageSpan(it, ImageSpan.ALIGN_BOTTOM)
                    span.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }

                binding.tvServiceB.text = span
            }
            advantageB == 1 -> {
                binding.tvServiceB.text = "AD"
            }
            else -> {
                binding.tvServiceB.text = "" // Kosongkan jika tidak ada service/advantage
            }
        }

        if (Set == 1) {
            binding.tvGamePointOneA.setTextColor(ContextCompat.getColor(this, R.color.green))
            binding.tvGamePointOneB.setTextColor(ContextCompat.getColor(this, R.color.green))
        }
        if (Set == 2) {
            binding.tvGamePointTwoA.setTextColor(ContextCompat.getColor(this, R.color.green))
            binding.tvGamePointTwoB.setTextColor(ContextCompat.getColor(this, R.color.green))
        }
        if (Set == 3) {
            binding.tvGamePointThreeA.setTextColor(ContextCompat.getColor(this, R.color.green))
            binding.tvGamePointThreeB.setTextColor(ContextCompat.getColor(this, R.color.green))
        }



        txtEvent.text = "$eventName"
//        txtEvent.isSelected = true
        txtCourt.text = "Court: $court"
        txtTanggal.text = "$date"

        binding.buttonSaveMatch.setOnClickListener {
            val db = AppDatabase.getDatabase(this)
            val matchDao = db.matchDao()

// Anda harus menjalankan Room di coroutine:
            lifecycleScope.launch {
                val match = MatchEntity(
                    slug = slug,
                    playerA1 = playerA1,
                    playerA2 = playerA2,
                    playerB1 = playerB1,
                    playerB2 = playerB2,
                    eventName = eventName,
                    court = court,
                    date = date,
                    scoreA = scoreA,
                    scoreB = scoreB,
                    setNumber = Set,
                    set1A = set1A,
                    set1B = set1B,
                    set2A = set2A,
                    set2B = set2B,
                    set3A = set3A,
                    set3B = set3B,
                    serviceA = serviceA,
                    serviceB = serviceB,
                    advantageA = advantageA,
                    advantageB = advantageB
                )
                matchDao.insertMatch(match)
                Toast.makeText(this@MainActivity, "Match saved to database!", Toast.LENGTH_SHORT).show()
            }

        }




        binding.buttonSet.setOnClickListener{
            Set += 1
            reset = 0
            if (Set >= 4) {
                Set = 1
            }

            if(Set == 1) {
                binding.tvGamePointOneA.setTextColor(ContextCompat.getColor(this, R.color.green))
                binding.tvGamePointOneB.setTextColor(ContextCompat.getColor(this, R.color.green))
            } else {
                binding.tvGamePointOneA.setTextColor(ContextCompat.getColor(this, R.color.white))
                binding.tvGamePointOneB.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
            if(Set == 2){
                binding.tvGamePointTwoA.setTextColor(ContextCompat.getColor(this, R.color.green))
                binding.tvGamePointTwoB.setTextColor(ContextCompat.getColor(this, R.color.green))
            } else {
                binding.tvGamePointTwoA.setTextColor(ContextCompat.getColor(this, R.color.white))
                binding.tvGamePointTwoB.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
            if(Set == 3){
                binding.tvGamePointThreeA.setTextColor(ContextCompat.getColor(this, R.color.green))
                binding.tvGamePointThreeB.setTextColor(ContextCompat.getColor(this, R.color.green))
            }else{
                binding.tvGamePointThreeA.setTextColor(ContextCompat.getColor(this, R.color.white))
                binding.tvGamePointThreeB.setTextColor(ContextCompat.getColor(this, R.color.white))
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
                binding.tvGamePointOneB.text = set1B.toString()
                sendSetScoreToServer("b", 1, set1B)
            } else if (Set == 2) {
                set2B++
                reset = 0
                if(set2B > 9){
                    set2B = 0
                }
                binding.tvGamePointTwoB.text = set2B.toString()
                sendSetScoreToServer("b", 2, set2B)
            } else if (Set == 3) {
                set3B++
                reset = 0
                if(set3B > 9){
                    set3B = 0
                }
                binding.tvGamePointThreeB.text = set3B.toString()
                sendSetScoreToServer("b", 3, set3B)
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
                binding.tvGamePointOneB.text = set1B.toString()
                sendSetScoreToServer("b", 1, set1B)
            } else if (Set == 2) {
                set2B--
                reset = 0
                if(set2B < 0){
                    set2B = 0
                }
                binding.tvGamePointTwoB.text = set2B.toString()
                sendSetScoreToServer("b", 2, set2B)
            } else if (Set == 3) {
                set3B--
                reset = 0
                if(set3B < 0 ){
                    set3B = 0
                }
                binding.tvGamePointThreeB.text = set3B.toString()
                sendSetScoreToServer("b", 3, set3B)
            }

            sendSetToESP32()
        }

        binding.buttonBallA.setOnClickListener {
            serviceA++
            reset = 0
            serviceB = 0
            advantageA = 0
            advantageB = 0
            binding.tvServiceB.text = ""
            if (serviceA == 1) {
                val drawable = ContextCompat.getDrawable(this, R.drawable.tennis_ball)
                drawable?.setBounds(0, 0, dpToPx(30), dpToPx(30)) // Ukuran 30dp

                val span = SpannableString(" ")
                drawable?.let {
                    val imageSpan = ImageSpan(it, ImageSpan.ALIGN_BOTTOM)
                    span.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }

                binding.tvServiceA.text = span
                binding.tvServiceB.text = ""
                sendServiceToServer("a")
                resetAdvantageToServer()
            } else if (serviceA == 2) {
                serviceA = 0
                binding.tvServiceA.text = ""
                resetServiceToServer()
            }

            sendServiceToESP32()
        }


        binding.buttonBallB.setOnClickListener{
            serviceB++
            reset = 0
            serviceA = 0
            advantageA = 0
            advantageB = 0
            binding.tvServiceA.text = ""
            if (serviceB == 1) {
                val drawable = ContextCompat.getDrawable(this, R.drawable.tennis_ball)
                drawable?.setBounds(0, 0, dpToPx(30), dpToPx(30)) // Ukuran 30dp

                val span = SpannableString(" ")
                drawable?.let {
                    val imageSpan = ImageSpan(it, ImageSpan.ALIGN_BOTTOM)
                    span.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }

                binding.tvServiceB.text = span
                sendServiceToServer("b")
                resetAdvantageToServer()
            } else if (serviceB == 2) {
                serviceB = 0
                binding.tvServiceB.text = ""
                resetServiceToServer()
            }
            sendServiceToESP32()
        }

        binding.buttonADA.setOnClickListener{
            advantageA++
            reset = 0
            advantageB = 0
            serviceA = 0
            serviceB = 0
            binding.tvServiceA.text = "AD"
            binding.tvServiceB.text = ""
            if (advantageA == 2) {
                advantageA = 0
                binding.tvServiceA.text = ""
                resetAdvantageToServer()
            }
            sendServiceToESP32()
            sendAdvantageToServer("a")
            resetServiceToServer()
        }

        binding.buttonADB.setOnClickListener {
            advantageB++
            reset = 0
            advantageA = 0
            serviceA = 0
            serviceB = 0
            binding.tvServiceB.text = "AD"
            binding.tvServiceA.text = ""
            if (advantageB == 2) {
                advantageB = 0
                binding.tvServiceB.text = ""
                resetAdvantageToServer()
            }
            sendServiceToESP32()
            sendAdvantageToServer("b")
            resetServiceToServer()
        }

        binding.enterTeamButton.setOnClickListener {
            val intent = Intent(this, TimActivity::class.java)
            intent.putExtra("slug", slug) // kirim slug yang kamu terima sebelumnya
            startActivityForResult(intent, 100)
        }

        binding.buttonBrightnessUp.setOnClickListener {
            if (Set == 1) {
                set1A++
                reset = 0
                if(set1A > 9) {
                    set1A = 0
                }
                binding.tvGamePointOneA.text = set1A.toString()
                sendSetScoreToServer("a", 1, set1A)
            } else if (Set == 2) {
                set2A++
                reset = 0
                if(set2A > 9) {
                    set2A = 0
                }
                binding.tvGamePointTwoA.text = set2A.toString()
                sendSetScoreToServer("a", 2, set2A)
            } else if (Set == 3) {
                set3A++
                reset = 0
                if(set3A > 9) {
                    set3A = 0
                }
                binding.tvGamePointThreeA.text = set3A.toString()
                sendSetScoreToServer("a", 3, set3A)
            }
            sendSetToESP32()
        }

        binding.buttonBrightnessDown.setOnClickListener {
            if (Set == 1) {
                set1A--
                reset = 0
                if(set1A < 0){
                    set1A = 0
                }
                binding.tvGamePointOneA.text = set1A.toString()
                sendSetScoreToServer("a", 1, set1A)
            } else if (Set == 2) {
                set2A--
                reset = 0
                if(set2A < 0){
                    set2A = 0
                }
                binding.tvGamePointTwoA.text = set2A.toString()
                sendSetScoreToServer("a", 2, set2A)
            } else if (Set == 3) {
                set3A--
                reset = 0
                if(set3A < 0){
                    set3A = 0
                }
                binding.tvGamePointThreeA.text = set3A.toString()
                sendSetScoreToServer("a", 3, set3A)
            }

            sendSetToESP32()
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
            sendScoresToESP32()
            sendScoreToServer("a", scoreA)
        }


        binding.buttonSkorUpOneA.setOnClickListener {
            reset = 0
            scoreA++

            if (scoreA > 99) {
                scoreA = 0
            }

            binding.tvSkorA.text = scoreA.toString()


            sendScoresToESP32()
            sendScoreToServer("a", scoreA)
        }

        binding.buttonSkorDownOneA.setOnClickListener {
            reset = 0
            scoreA--

            if (scoreA <= 0) {
                scoreA = 0
            }

            binding.tvSkorA.text = scoreA.toString()


            sendScoresToESP32()
            sendScoreToServer("a", scoreA)
        }

        binding.buttonSkorUpOneB.setOnClickListener {
            reset = 0
            scoreB++

            if (scoreB > 99) {
                scoreB = 0
            }

            binding.tvSkorB.text = scoreB.toString()


            sendScoresToESP32()
            sendScoreToServer("b", scoreB)
        }

        binding.buttonSkorDownOneB.setOnClickListener {
            reset = 0
            scoreB--

            if (scoreB <= 0) {
                scoreB = 0
            }

            binding.tvSkorB.text = scoreB.toString()


            sendScoresToESP32()
            sendScoreToServer("b", scoreB)
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


            sendScoresToESP32()
            sendScoreToServer("a", scoreA)
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


            sendScoresToESP32()
            sendScoreToServer("b", scoreB)
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


            sendScoresToESP32()
            sendScoreToServer("b", scoreB)
        }

        binding.buttonReset.setOnClickListener {
            pressReset++
            if(pressReset == 3){
                pressReset = 0
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
                binding.tvSkorA.text = scoreA.toString()
                binding.tvSkorB.text = scoreB.toString()
                binding.tvServiceA.text = ""
                binding.tvServiceB.text = ""
                binding.tvGamePointOneA.setTextColor(ContextCompat.getColor(this, R.color.green))
                binding.tvGamePointOneB.setTextColor(ContextCompat.getColor(this, R.color.green))
                binding.tvGamePointTwoA.setTextColor(ContextCompat.getColor(this, R.color.white))
                binding.tvGamePointTwoB.setTextColor(ContextCompat.getColor(this, R.color.white))
                binding.tvGamePointThreeA.setTextColor(ContextCompat.getColor(this, R.color.white))
                binding.tvGamePointThreeB.setTextColor(ContextCompat.getColor(this, R.color.white))
                binding.tvGamePointOneA.text = set1A.toString()
                binding.tvGamePointTwoA.text = set2A.toString()
                binding.tvGamePointThreeA.text = set3A.toString()
                binding.tvGamePointOneB.text = set1B.toString()
                binding.tvGamePointTwoB.text = set2B.toString()
                binding.tvGamePointThreeB.text = set3B.toString()
                sendResetToESP32()
                resetToServer()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            playerA1 = data.getStringExtra("playerA1")
            playerA2 = data.getStringExtra("playerA2")
            playerB1 = data.getStringExtra("playerB1")
            playerB2 = data.getStringExtra("playerB2")

            binding.tvPlayerA1.text = formatPlayerNameA1(playerA1)
            binding.tvPlayerA2.text = formatPlayerNameA2(playerA2)
            binding.tvPlayerB1.text = formatPlayerNameB1(playerB1)
            binding.tvPlayerB2.text = formatPlayerNameB2(playerB2)
        }
    }


    private fun changeButtonOpacity(button: ImageButton) {
        button.alpha = 0.5f  // Mengatur opacity menjadi 50%
        button.postDelayed({
            button.alpha = 1f  // Mengembalikan opacity ke normal setelah 200 ms
        }, 200)  // Durasi waktu efek opacity
    }

    private fun formatPlayerNameA1(playerNameA1: String?): String {
        return if (playerNameA1 != null && playerNameA1.length > 11) {
            playerNameA1.substring(0, 11) + ".."
        } else {
            playerNameA1 ?: "PLAYER A1"
        }
    }

    private fun formatPlayerNameA2(playerNameA2: String?): String {
        return if (playerNameA2 != null && playerNameA2.length > 11) {
            playerNameA2.substring(0, 11) + ".."
        } else {
            playerNameA2 ?: "PLAYER A2"
        }
    }

    private fun formatPlayerNameB1(playerNameB1: String?): String {
        return if (playerNameB1 != null && playerNameB1.length > 11) {
            playerNameB1.substring(0, 11) + ".."
        } else {
            playerNameB1 ?: "PLAYER B1"
        }
    }

    private fun formatPlayerNameB2(playerNameB2: String?): String {
        return if (playerNameB2 != null && playerNameB2.length > 11) {
            playerNameB2.substring(0, 11) + ".."
        } else {
            playerNameB2 ?: "PLAYER B2"
        }
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
                Toast.makeText(this@MainActivity, "Not connected to scoreboard", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
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
                Toast.makeText(this@MainActivity, "Not connected to scoreboard", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this@MainActivity, "Failed to update scores: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {

                Log.e("Network Error", "Error: ${t.message}")
                Toast.makeText(this@MainActivity, "Not connected to scoreboard", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this@MainActivity, "Failed to update services: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {

                Log.e("Network Error", "Error: ${t.message}")
                Toast.makeText(this@MainActivity, "Not connected to scoreboard", Toast.LENGTH_SHORT).show()
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

    // Tambahkan fungsi untuk melakukan reset ke server
    private fun resetToServer() {
        ApiConfig.getApiService().resetGame(slug)
            .enqueue(object : Callback<ResetGameResponse> {
                override fun onResponse(
                    call: Call<ResetGameResponse>,
                    response: Response<ResetGameResponse>
                ) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        Toast.makeText(this@MainActivity, "Reset match successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Fail to reset match", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResetGameResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
    }






}
