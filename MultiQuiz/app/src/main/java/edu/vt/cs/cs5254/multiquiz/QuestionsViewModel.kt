package edu.vt.cs.cs5254.multiquiz

import android.util.Log
import androidx.lifecycle.ViewModel
import edu.vt.cs.cs5254.geoquiz.Questions

private const val TAG = "QuestionViewModel"
private var correctNumber = 0;
class QuestionsViewModel:ViewModel() {
    init {
        Log.d(TAG, "viewmode instance is created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "viewModel is about to be destroyed")
    }
    //private val questionsBank = listOf()
    //creat list of the questions
    var currentIndex = 0
    val QuestionsList = listOf(
        Questions(R.string.question_australiau, R.string.australia_answer_Canberra),
        Questions(R.string.question_china, R.string.china_answer_Beijing),
        Questions(R.string.question_japan, R.string.japan_answer_Tokyo),
        Questions(R.string.question_usa,R.string.usa_answer_DC)
    )

    // model fields (only one)
    val answerList = listOf(
        Answer(R.string.australia_answer_Canberra, true),
        Answer(R.string.china_answer_Beijing, false),
        Answer(R.string.japan_answer_Tokyo, false),
        Answer(R.string.usa_answer_DC, false)
    )

    val currentQuestionAnswerId: Int
        get() = QuestionsList[currentIndex].answerID
    val currentQuestionText: Int
        get() = QuestionsList[currentIndex].textResId

    fun updateAnswerList(){
        answerList.forEach {
            it.isCorrect = it.textResId == currentQuestionAnswerId
        }
    }
    fun moveToNext(){
        currentIndex += 1
    }
    fun selectAnswer(clickedAnswer: Answer){
        val origIsSelected = clickedAnswer.isSelected
        answerList.forEach {
            it.isSelected = false
        }
        clickedAnswer.isSelected = !origIsSelected
    }
    fun CountCorrectAnswer(): Int{
        if(CheckCorrectAnswer()) correctNumber++
        return correctNumber;

    }
    fun CheckCorrectAnswer(): Boolean{
        answerList.forEach {
            return it.isSelected && it.isCorrect
        }
        return false
    }
}