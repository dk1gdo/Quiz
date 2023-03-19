package com.example.quiz

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.quiz.data.Question

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val questions = mutableListOf<Question>()
    private var currentQuestion: Int = 0
    private lateinit var tvQuestionNumber: TextView
    private lateinit var tvQuestionText: TextView
    private lateinit var btnAnswer1: Button
    private lateinit var btnAnswer2: Button
    private lateinit var btnAnswer3: Button
    private lateinit var btnAnswer4: Button
    private var correctAnswerCount: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber)
        tvQuestionText = findViewById(R.id.tvQuestionText)
        btnAnswer1 = findViewById(R.id.btnAnswer1)
        btnAnswer2 = findViewById(R.id.btnAnswer2)
        btnAnswer3 = findViewById(R.id.btnAnswer3)
        btnAnswer4 = findViewById(R.id.btnAnswer4)
        fillQuestions()
        btnAnswer1.setOnClickListener(this)
        btnAnswer2.setOnClickListener(this)
        btnAnswer3.setOnClickListener(this)
        btnAnswer4.setOnClickListener(this)
    }
    private fun fillQuestions() {
        questions.run {
            this.add(Question("Поименованная область в памяти для хранения данных", "Тип данных", "Переменная", "Оператор", "Класс", 2))
            this.add(Question("Допустимое множество значений", "Тип данных", "Переменная", "Оператор", "Класс", 1))
            this.add(Question("Наименьшая автономная часть языка программирования", "Тип данных", "Переменная", "Оператор", "Класс", 3))
            this.add(Question("Модель для создания объектов определённого типа", "Тип данных", "Переменная", "Оператор", "Класс", 4))
        }
    }
    private fun updateUi(){
        tvQuestionNumber.text = getString(R.string.question_number_ui, currentQuestion + 1, questions.size)
        tvQuestionText.text = questions[currentQuestion].textQuestion
        btnAnswer1.text = questions[currentQuestion].answer1
        btnAnswer2.text = questions[currentQuestion].answer2
        btnAnswer3.text = questions[currentQuestion].answer3
        btnAnswer4.text = questions[currentQuestion].answer4
    }
    private fun checkAnswer(givenId: Int) = (givenId == questions[currentQuestion].right)
    private fun toNextQuestion() : Boolean {
        if (currentQuestion >= questions.size - 1) return false
        currentQuestion++
        updateUi()
        return true
    }
    private fun processAnswer(givenId: Int) {
        if (checkAnswer(givenId)) correctAnswerCount++
        if (!toNextQuestion()) {
            if (correctAnswerCount == questions.size) {
                val intent = Intent(this, ResultActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                askForRestart()
            }
        }
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
    private fun askForRestart() = AlertDialog.Builder(this) .run {
        setTitle(R.string.title_dialog)
        val messageText = getString(R.string.game_result, correctAnswerCount, questions.size)
        setMessage(messageText)
        setNegativeButton(android.R.string.cancel){_,_->
            finish()
        }
        setPositiveButton(android.R.string.ok){_,_ ->
            currentQuestion = 0
            correctAnswerCount = 0
            updateUi()
        }
        setCancelable(false)
        create()
    }.show()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentQuestion", currentQuestion)
        outState.putInt("correctAnswerCount", correctAnswerCount)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentQuestion = savedInstanceState.getInt("currentQuestion", 0)
        correctAnswerCount = savedInstanceState.getInt("correctAnswerCount", 0)
    }

    override fun onResume() {
        super.onResume()
        updateUi()
    }

}