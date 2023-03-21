package com.example.quiz.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.quiz.data.Question

class MainViewModel: ViewModel() {
    private val questions = mutableListOf<Question>()
    private var currentQuestion = 0
    private var correctAnswerCount = 0
    private val tag = this.javaClass.simpleName
    init {
        Log.d(tag, "ViewModel created!")
        fillQuestions()
    }
    override fun onCleared() {
        super.onCleared()
        Log.d(tag, "ViewModel destroyed!")
    }
    private fun fillQuestions() {
        questions.run {
            this.add(Question("Поименованная область в памяти для хранения данных", "Тип данных", "Переменная", "Оператор", "Класс", 2))
            this.add(Question("Допустимое множество значений", "Тип данных", "Переменная", "Оператор", "Класс", 1))
            this.add(Question("Наименьшая автономная часть языка программирования", "Тип данных", "Переменная", "Оператор", "Класс", 3))
            this.add(Question("Модель для создания объектов определённого типа", "Тип данных", "Переменная", "Оператор", "Класс", 4))
        }
    }
    fun toNextQuestion() : Boolean {
        if (currentQuestion >= questions.size - 1) return false
        currentQuestion++
        return true
    }
    fun checkAnswer(givenId: Int): Boolean{
        if (givenId == getCurrentQuestion().right) {
            correctAnswerCount++
            return true
        }
        return false
    }
    fun reset(){
        currentQuestion = 0
        correctAnswerCount = 0
    }
    fun getQuestionSize() = questions.size
    fun getCurrentQuestion() = questions[currentQuestion]
    fun getCurrentQuestionNumber() = currentQuestion
    fun checkAllAnswerCorrect() = (correctAnswerCount == questions.size)
    fun getCorrectAnswerCount() = correctAnswerCount

}