package com.baliproject.scoreboardtennis.UI.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.baliproject.scoreboardtennis.API.ApiService
import com.baliproject.scoreboardtennis.API.ResponseData
import com.baliproject.scoreboardtennis.API.RetrofitClient
import com.baliproject.scoreboardtennis.Database.AppDatabase
import com.baliproject.scoreboardtennis.R
import com.baliproject.scoreboardtennis.databinding.FragmentDuaBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentDua : Fragment() {

    private var _binding: FragmentDuaBinding? = null
    private val binding get() = _binding!!

    private var persen = 80

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDuaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val wifi = db.wifiDao().getWifiInformation()
            val ip = db.ipAddressDao().getIpAddress()

            binding.textSsid.text = "SSID: ${wifi?.ssid ?: "-"}"
            binding.textPassword.text = "Password: ${wifi?.password ?: "-"}"
            binding.textIpAddress.text = "IP Address: ${ip?.ipAddress ?: "-"}"
        }

        binding.buttonBrightnessUpFragment.setOnClickListener {
            // Menambah nilai persen
            if (persen < 100) {
                persen += 10
            }
            sendBrightnessToESP32()
        }

        binding.buttonBrightnessDownFragment.setOnClickListener {
            // Mengurangi nilai persen
            if (persen > 0) {
                persen -= 10
            }
            sendBrightnessToESP32()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun loadWifiAndIpInfo() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val wifi = db.wifiDao().getWifiInformation()
            val ip = db.ipAddressDao().getIpAddress()

            binding.textSsid.text = "SSID: ${wifi?.ssid ?: "-"}"
            binding.textPassword.text = "Password: ${wifi?.password ?: "-"}"
            binding.textIpAddress.text = "IP Address: ${ip?.ipAddress ?: "-"}"
        }
    }

    override fun onResume() {
        super.onResume()
        loadWifiAndIpInfo()
    }



    private fun sendBrightnessToESP32() {
        val apiService = RetrofitClient.retrofit.create(ApiService::class.java)
        val call = apiService.updateBrigthness(persen)

        call.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if (response.isSuccessful) {
                    val status = response.body()?.status
                    Log.d("Retrofit", "Response: $status")
                } else {
                    Toast.makeText(requireContext(), "Failed to update brightness: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                Log.e("Network Error", "Error: ${t.message}")
                Toast.makeText(requireContext(), "Not connected to scoreboard", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

