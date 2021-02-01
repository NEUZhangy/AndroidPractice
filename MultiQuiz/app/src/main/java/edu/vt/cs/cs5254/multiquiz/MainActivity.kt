package edu.vt.cs.cs5254.multiquiz

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.get
import edu.vt.cs.cs5254.geoquiz.Questions
import edu.vt.cs.cs5254.multiquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val DEFAULT_BUTTON_COLOR = "#6699ff"
    private val SELECTED_BUTTON_COLOR = "#40bf80"
    private val DISABLE_BUTTO_COLOR = "#800000"
    lateinit var binding: ActivityMainBinding

    // view fields (only one)
    lateinit var answerButtonList: List<Button>

    //creat list of the questions
    private val QuestionsList = listOf(
        Questions(R.string.question_australiau, R.string.australia_answer_Canberra),
        Questions(R.string.question_china, R.string.china_answer_Beijing),
        Questions(R.string.question_japan, R.string.japan_answer_Tokyo),
        Questions(R.string.question_usa,R.string.usa_answer_DC)
    )
    private var currentIndex = 0
    // model fields (only one)
    private val answerList = listOf(
            Answer(R.string.australia_answer_Canberra, true),
            Answer(R.string.china_answer_Beijing, false),
            Answer(R.string.japan_answer_Tokyo, false),
            Answer(R.string.usa_answer_DC, false)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // ------------------------------------------------------
        // Initialize answer-button list
        // ------------------------------------------------------

        answerButtonList = binding.answerButtons
                .children
                .toList()
                .filterIsInstance<Button>()

        // ------------------------------------------------------
        // Set text of views
        // ------------------------------------------------------

        binding.questionTextView.setText(R.string.question_australiau)

        answerButtonList.zip(answerList){
            a,b-> a.setText(b.textResId)
        }

        binding.hintButton.setText(R.string.hint_button)
        binding.submitButton.setText(R.string.submit_button)
        binding.submitButton.isEnabled = false
        // ------------------------------------------------------
        // Add listeners to buttons
        // ------------------------------------------------------
        answerButtonList.zip(answerList){
            a,b-> a.setOnClickListener {
            processAnswerButtonClick(b)
            }
        }
        binding.hintButton.setOnClickListener {
            processHintButtonClick()
        }
        binding.submitButton.setOnClickListener {
            procesSubmitButtonClick()
        }

        // ------------------------------------------------------
        // Refresh the view
        // ------------------------------------------------------

        refreshView()
    }
    private fun updateQuestion(){

        val questionsTextResId = QuestionsList[currentIndex].textResId
        binding.questionTextView.setText(questionsTextResId)
        val answerTextResID = QuestionsList[currentIndex].answerID
        answerList.forEach {
            it.isCorrect = it.textResId == answerTextResID
        }
    }

    private fun processAnswerButtonClick(clickedAnswer: Answer) {

        val origIsSelected = clickedAnswer.isSelected
        answerList.forEach {
            it.isSelected = false
        }
        clickedAnswer.isSelected = !origIsSelected

        refreshView()
    }

    private fun processHintButtonClick(){
        val wrongAnswer = answerList.filter { !it.isCorrect && it.isEnabled}
        val randomWrong = wrongAnswer.random();
        randomWrong.isEnabled = false;
        randomWrong.isSelected = false;
        refreshView()
    }

    private fun procesSubmitButtonClick(){

        answerList.forEach {
            it.isEnabled = true
            it.isSelected = false
        }
        refreshView()
        currentIndex = (currentIndex+1)%QuestionsList.size
        updateQuestion()
    }

    private fun refreshView() {
        binding.hintButton.isEnabled = true
        val selectedAnswers = answerList.filter {it.isSelected}
        //if questions is selected, enable the submit button
        binding.submitButton.isEnabled = !selectedAnswers.isEmpty()

        // TODO Use pairs and a zipped list instead of 0..3
        answerButtonList.zip(answerList){
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
        val disableAnswers = answerList.filter { !it.isEnabled }
        val wrongAnswers = answerList.filter { !it.isCorrect }
        if(disableAnswers.containsAll(wrongAnswers)){
            binding.hintButton.isEnabled = false
        }

    }

    private fun setButtonColor(button: Button, colorString: String) {
        button.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor(colorString))
        button.setTextColor(Color.WHITE)
        button.alpha = 1f
    }

    private  fun checkAnswer(){

    }
}
