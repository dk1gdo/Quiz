package com.example.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.quiz.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var tvQuestionNumber: TextView
    private lateinit var tvQuestionText: TextView
    private lateinit var btnAnswer1: Button
    private lateinit var btnAnswer2: Button
    private lateinit var btnAnswer3: Button
    private lateinit var btnAnswer4: Button

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber)
        tvQuestionText = findViewById(R.id.tvQuestionText)
        btnAnswer1 = findViewById(R.id.btnAnswer1)
        btnAnswer2 = findViewById(R.id.btnAnswer2)
        btnAnswer3 = findViewById(R.id.btnAnswer3)
        btnAnswer4 = findViewById(R.id.btnAnswer4)
        btnAnswer1.setOnClickListener(this)
        btnAnswer2.setOnClickListener(this)
        btnAnswer3.setOnClickListener(this)
        btnAnswer4.setOnClickListener(this)
        updateUi()
    }
    private fun updateUi() = with(viewModel) {

        tvQuestionNumber.text = getString(R.string.question_number_ui, getCurrentQuestionNumber() + 1, getQuestionSize())
        getCurrentQuestion().run {
            tvQuestionText.text = textQuestion
            btnAnswer1.text = answer1
            btnAnswer2.text = answer2
            btnAnswer3.text = answer3
            btnAnswer4.text = answer4
        }
    }
    private fun processAnswer(givenId: Int) {
        viewModel.checkAnswer(givenId)
        if (!viewModel.toNextQuestion()) {
            if (viewModel.checkAllAnswerCorrect()) {
                val intent = Intent(this@MainActivity, ResultActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                askForRestart()
            }
        }
        updateUi()
    }
    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.btnAnswer1 -> processAnswer(1)
                R.id.btnAnswer2 -> processAnswer(2)
                R.id.btnAnswer3 -> processAnswer(3)
                R.id.btnAnswer4 -> processAnswer(4)
            }

        }
    }
    private fun askForRestart() = AlertDialog.Builder(this).run {
        setTitle(R.string.title_dialog)
        val messageText = getString(
            R.string.game_result,
            viewModel.getCorrectAnswerCount(),
            viewModel.getQuestionSize())
        setMessage(messageText)
        setNegativeButton(android.R.string.cancel){_,_->
            finish()
        }
        setPositiveButton(android.R.string.ok){_,_ ->
            viewModel.reset()
            updateUi()
        }
        setCancelable(false)
        create()
    }.show()
}