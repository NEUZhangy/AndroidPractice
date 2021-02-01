package edu.vt.cs.cs5254.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
class QuizViewModel: ViewModel() {

    var currentIndex = 0
    private val questionBank = listOf(
        Questions(R.string.question_australia, true),
        Questions(R.string.question_oceans, true),
        Questions(R.string.question_mideast, false),
        Questions(R.string.question_africa,false),
        Questions(R.string.question_americas, true),
        Questions(R.string.question_asia, true))

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionTest: Int
        get() = questionBank[currentIndex].textResId
    fun moveToNext(){
        currentIndex = (currentIndex+1) % questionBank.size
    }
}