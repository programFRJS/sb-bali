package com.baliproject.scoreboardtennis.UI.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baliproject.scoreboardtennis.R


class FragmentDua : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dua, container, false)
        // Tambahkan informasi WiFi di sini
        return view
    }
}
