package edu.vt.cs.cs5254.multiquiz

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import edu.vt.cs.cs5254.geoquiz.Questions
import edu.vt.cs.cs5254.multiquiz.databinding.ActivityQuizBinding

private const val TAG = "QuizActivity"
private const val KEY_INDEX = "index"
private const val EXTRA_TOTOAL_CORRECT = "total_correct"
private const val EXTRA_TOTOAL_QUESTIONS = "total_questions"
private const val EXTRA_TOTAL_HINT= "total_hint_used"

class QuizActivity : AppCompatActivity() {

    private val DEFAULT_BUTTON_COLOR = "#6699ff"
    private val SELECTED_BUTTON_COLOR = "#40bf80"
    private val DISABLE_BUTTO_COLOR = "#800000"
    lateinit var ui: ActivityQuizBinding
    private val questionsViewModel: QuestionsViewModel by lazy {
        ViewModelProvider(this).get(QuestionsViewModel::class.java)
    }
    lateinit var answerButtonList: List<Button>
    companion object{
        var totolQuestionCount = 1
        var correctAnswerCount = 0
        var totalHintUsed = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityQuizBinding.inflate(layoutInflater)
        val view = ui.root
        setContentView(view)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0)?:0
        questionsViewModel.currentIndex = currentIndex
        totolQuestionCount = savedInstanceState?.getInt(EXTRA_TOTOAL_QUESTIONS,1)?:1
        correctAnswerCount = savedInstanceState?.getInt(EXTRA_TOTOAL_CORRECT,0)?:0
        totalHintUsed = savedInstanceState?.getInt(EXTRA_TOTAL_HINT, 0)?:0

        answerButtonList = ui.answerButtons
                .children
                .toList()
                .filterIsInstance<Button>()

        ui.questionTextView.setText(questionsViewModel.currentQuestionText)
        answerButtonList.zip(questionsViewModel.answerList){
            a,b-> a.setText(b.textResId)
        }
        ui.hintButton.setText(R.string.hint_button)
        ui.submitButton.setText(R.string.submit_button)
        ui.submitButton.isEnabled = false

        answerButtonList.zip(questionsViewModel.answerList){
            a,b-> a.setOnClickListener {
            processAnswerButtonClick(b)
            }
        }
        ui.hintButton.setOnClickListener {
            processHintButtonClick()
        }
        ui.submitButton.setOnClickListener {
            procesSubmitButtonClick()
        }

        refreshView()
    }
    private fun updateQuestion(){
        val questionsTextResId = questionsViewModel.currentQuestionText
        questionsViewModel.updateAnswerList()
        ui.questionTextView.setText(questionsTextResId)
        totolQuestionCount++
    }

    private fun processAnswerButtonClick(clickedAnswer: Answer) {
        questionsViewModel.selectAnswer(clickedAnswer)
        refreshView()
    }

    private fun processHintButtonClick(){
        totalHintUsed++
        val wrongAnswer = questionsViewModel.answerList.filter { !it.isCorrect && it.isEnabled}
        val randomWrong = wrongAnswer.random();
        randomWrong.isEnabled = false;
        randomWrong.isSelected = false;
        refreshView()
    }
    private fun procesSubmitButtonClick(){
        //val selectedAnswer
        questionsViewModel.answerList.forEach {
            if(it.isSelected && it.isCorrect) {
                correctAnswerCount++
            }
        }

        if (questionsViewModel.currentIndex == questionsViewModel.QuestionsList.size-1) {
            val intent = newIntent(this, totolQuestionCount, correctAnswerCount, totalHintUsed)
            startActivity(intent)
            return
        }

        questionsViewModel.answerList.forEach {
            it.isEnabled = true
            it.isSelected = false
        }
        refreshView()
        questionsViewModel.moveToNext()
        updateQuestion()

    }

    private fun refreshView() {
        ui.hintButton.isEnabled = true
        val selectedAnswers =  questionsViewModel.answerList.filter {it.isSelected}
        //if questions is selected, enable the submit button
        ui.submitButton.isEnabled = !selectedAnswers.isEmpty()

        answerButtonList.zip(questionsViewModel.answerList){
            a,b -> a.isEnabled = b.isEnabled
            a.isSelected = b.isSelected
            if(b.isSelected){
                setButtonColor(a,SELECTED_BUTTON_COLOR)
            }else{
                setButtonColor(a,DEFAULT_BUTTON_COLOR)
            }
            if (!b.isEnabled) {
                //a.alpha = .5f
                setButtonColor(a, DISABLE_BUTTO_COLOR)
            }
        }
        //if all wrong answer selected, disable the hint button
        val disableAnswers = questionsViewModel.answerList.filter { !it.isEnabled }
        val wrongAnswers = questionsViewModel.answerList.filter { !it.isCorrect }
        if(disableAnswers.containsAll(wrongAnswers)){
            ui.hintButton.isEnabled = false
        }

    }

    private fun setButtonColor(button: Button, colorString: String) {
        button.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor(colorString))
        button.setTextColor(Color.WHITE)
        button.alpha = 1f
    }

    // check the lifecycle of the mainActivity here
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(KEY_INDEX, questionsViewModel.currentIndex)
        savedInstanceState.putInt(EXTRA_TOTOAL_CORRECT, totolQuestionCount)
        savedInstanceState.putInt(EXTRA_TOTOAL_CORRECT, correctAnswerCount)
        savedInstanceState.putInt(EXTRA_TOTAL_HINT, totalHintUsed)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun newIntent(packagesContext: Context, totalQuestion:Int, totoalCorrect:Int, totalHint: Int ):Intent{
        return Intent(packagesContext, ResultsActivity::class.java).apply {
            putExtra(EXTRA_TOTOAL_QUESTIONS, totalQuestion)
            putExtra(EXTRA_TOTOAL_CORRECT, totoalCorrect)
            putExtra(EXTRA_TOTAL_HINT, totalHint)
        }
    }
}
