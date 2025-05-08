package com.baliproject.scoreboardtennis.UI.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.baliproject.scoreboardtennis.API.RetrofitClient
import com.baliproject.scoreboardtennis.API.RetrofitClientWifi
import com.baliproject.scoreboardtennis.API.WifiCredentials
import com.baliproject.scoreboardtennis.ViewModel.IpAddressViewModel
import com.baliproject.scoreboardtennis.databinding.FragmentSatuBinding
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class FragmentSatu : Fragment() {

    private var _binding: FragmentSatuBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: IpAddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSatuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[IpAddressViewModel::class.java]

        binding.buttonSaveWifi.setOnClickListener {
            val ip = binding.ipAddressEditText.text.toString()
            val ssid = binding.ssidWifiEditText.text.toString()
            val password = binding.passwordWifiEditText.text.toString()

            if (ssid.isNotEmpty() && password.isNotEmpty()) {

                if (ip.isNotEmpty()) {
                    sendWiFiCredentials(ip, ssid, password)
                } else {
                    Toast.makeText(requireContext(), "IP belum disimpan", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "WiFi/password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonSaveIpAddress.setOnClickListener {
            val ip = binding.ipAddressEditText.text.toString()
            if (ip.isNotEmpty()) {
                viewModel.saveIpAddress(ip)

                lifecycleScope.launch {
                    RetrofitClient.init(requireContext())
                    Toast.makeText(requireContext(), "IP disimpan & Retrofit diperbarui", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "IP tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendWiFiCredentials(ip: String, ssid: String, password: String) {
        val url = "http://$ip/wifi"
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("ssid", ssid)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Gagal mengirim data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "WiFi dikirim ke ESP32", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


