package com.gta.myapplication.debugDemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.gta.myapplication.R

class MyFragment : Fragment() {

    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_my, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.btnFetchUser).setOnClickListener {
            fetchUserProfile()
        }
    }

    private fun fetchUserProfile() {
        val name = user!!.name  // 故意写“!!”制造崩溃
        Log.d("MyFragment", "User name: $name")
    }
}
