package de.martin.gptapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        val progressbar = findViewById<ProgressBar>(R.id.progressBar)

        viewModel.gptResponseLiveData.observe(this) {
            findViewById<TextView>(R.id.answerTextView).text = it
            progressbar.visibility = View.INVISIBLE
        }
        val gptQuestionText = findViewById<EditText>(R.id.userQuestionFieldEditText)
        val buttonClick: Button = findViewById<View>(R.id.button) as Button
        buttonClick.setOnClickListener {
            val historyText = findViewById<TextView>(R.id.historyTextView)
            historyText.text = historyText.text.toString() + "\n" + viewModel.userGptQuery
            viewModel.makeGptCall()
            progressbar.visibility = View.VISIBLE
        }

        gptQuestionText.doOnTextChanged { text, _, _, _ ->
            viewModel.updateGptQueryQuestion(text.toString())
        }
    }
}