package com.baliproject.scoreboardtennis.UI.Fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.baliproject.scoreboardtennis.API.RetrofitClient
import com.baliproject.scoreboardtennis.API.RetrofitClientWifi
import com.baliproject.scoreboardtennis.API.WifiCredentials
import com.baliproject.scoreboardtennis.R
import com.baliproject.scoreboardtennis.ViewModel.IpAddressViewModel
import com.baliproject.scoreboardtennis.ViewModel.WifiViewModel
import com.baliproject.scoreboardtennis.databinding.FragmentSatuBinding
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    private lateinit var viewModel2: WifiViewModel
    private var confirmSaveClicked = false
    private var confirmResetJob: Job? = null

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

        viewModel2 = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[WifiViewModel::class.java]

        binding.buttonSaveWifi.setOnClickListener {
            val ip = binding.ipAddressEditText.text.toString()
            val ssid = binding.ssidWifiEditText.text.toString()
            val password = binding.passwordWifiEditText.text.toString()

            if (ssid.isNotEmpty() && password.isNotEmpty()) {
                if (ip.isNotEmpty()) {
                    // Simpan ke database
                    lifecycleScope.launch {
                        viewModel.saveIpAddressBlocking(ip)
                        viewModel2.saveWifiBlocking(ssid, password)

                        RetrofitClient.init(requireContext())
                        sendWiFiCredentials(ip, ssid, password)

                        Toast.makeText(requireContext(), "IP & WiFi saved", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "IP not saved", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "WiFi/password cannot be blank", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonSaveIpAddress.setOnClickListener {
            val ip = binding.ipAddressEditText.text.toString()

            if (ip.isNotEmpty()) {
                lifecycleScope.launch {
                    delay(300) // delay kecil seolah menunggu "konfirmasi internal"
                    viewModel.saveIpAddressBlocking(ip)
                    RetrofitClient.init(requireContext())
                    Toast.makeText(requireContext(), "IP Address save", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "IP Address cannot be blank", Toast.LENGTH_SHORT).show()
            }
        }

        val buttonResetWifi = view.findViewById<MaterialButton>(R.id.buttonResetWifi)
        buttonResetWifi.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Reset WiFi")
                .setMessage("Sure for reset Wifi?")
                .setPositiveButton("Yes") { dialog, _ ->
                    // Panggil endpoint ESP32
                    resetWifiFromESP()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }





    }

    private fun resetWifiFromESP() {
        val ip = view?.findViewById<EditText>(R.id.ipAddressEditText)?.text.toString()
        if (ip.isEmpty()) {
            Toast.makeText(requireContext(), "IP Address cannot be blank", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "http://$ip/reset_wifi"
        val request = Request.Builder().url(url).get().build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Failed to connect: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                requireActivity().runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "WiFi reset successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
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
                    Toast.makeText(requireContext(), "Failed to send wifi", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Successfully send wifi to scoreboard", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


