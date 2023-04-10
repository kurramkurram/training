package com.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class SecondFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            val message = bundle.getString(KEY_MESSAGE)
            view.findViewById<TextView>(R.id.text_view).text = message
        }

        view.findViewById<Button>(R.id.button).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    companion object {
        fun newInstance(message: String): SecondFragment = SecondFragment().apply {
            val bundle = Bundle()
            bundle.putString(KEY_MESSAGE, message)
            arguments = bundle
        }

        private const val KEY_MESSAGE = "key_message"
    }
}