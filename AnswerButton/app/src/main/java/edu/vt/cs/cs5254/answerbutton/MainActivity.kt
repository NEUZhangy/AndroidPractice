package edu.vt.cs.cs5254.answerbutton

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.get
import edu.vt.cs.cs5254.answerbutton.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val DEFAULT_BUTTON_COLOR = "#00A2FF"
    private val SELECTED_BUTTON_COLOR = "#CB297B"

    lateinit var binding: ActivityMainBinding

    // view fields (only one)
    lateinit var answerButtonList: List<Button>

    // model fields (only one)
    private val answerList = listOf(
        Answer(R.string.australia_answer_brisbane, false),
        Answer(R.string.australia_answer_canberra, true),
        Answer(R.string.australia_answer_perth, false),
        Answer(R.string.australia_answer_sydney, false)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ------------------------------------------------------
        // Create binding and content view
        // ------------------------------------------------------

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

        binding.questionTextView.setText(R.string.australia_question)

        // TODO Use pairs and a zipped list instead of 0..3
        answerButtonList.zip(answerList){
            a,b-> a.setText(b.textResId)
        }

        binding.disableButton.setText(R.string.disable_button)
        binding.resetButton.setText(R.string.reset_button)

        // ------------------------------------------------------
        // Add listeners to buttons
        // ------------------------------------------------------

        // TODO Use pairs and a zipped list instead of 0..3
        answerButtonList.zip(answerList){
            a,b-> a.setOnClickListener {
                processAnswerButtonClick(b)
            }
        }

        binding.disableButton.setOnClickListener {
            processDisableButtonClick()
        }
        binding.resetButton.setOnClickListener {
            processResetButtonClick()
        }

        // ------------------------------------------------------
        // Refresh the view
        // ------------------------------------------------------

        refreshView()
    }

    private fun processAnswerButtonClick(clickedAnswer: Answer) {

        val origIsSelected = clickedAnswer.isSelected
        // TODO Use forEach instead of for loop
        answerList.forEach {
            it.isSelected = false
        }
        clickedAnswer.isSelected = !origIsSelected

        refreshView()
    }

    private fun processDisableButtonClick() {

        // TODO Use list functions (filter/take/forEach) instead of for loop
        var count = 2
        val wrongAnswer = answerList.filter { !it.isCorrect }
        val flipAnswer = wrongAnswer.take(count)
        flipAnswer.forEach {
            it.isEnabled =false
            it.isSelected = false
        }

        refreshView()
    }

    private fun processResetButtonClick() {

        // TODO use forEach instead of for loop
        answerList.forEach {
            it.isEnabled = true
            it.isSelected = false
        }

        refreshView()
    }

    private fun refreshView() {

        binding.disableButton.isEnabled = true

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
                a.alpha = .5f
            }
        }
        val disableAnswers = answerList.filter { !it.isEnabled }
        if(disableAnswers.any()){
            binding.disableButton.isEnabled = false
        }
    }

    private fun setButtonColor(button: Button, colorString: String) {
        button.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(colorString))
        button.setTextColor(Color.WHITE)
        button.alpha = 1f
    }
}
