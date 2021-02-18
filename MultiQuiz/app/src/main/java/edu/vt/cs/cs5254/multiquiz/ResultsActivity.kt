package edu.vt.cs.cs5254.multiquiz

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.vt.cs.cs5254.multiquiz.databinding.ActivityQuizBinding
import edu.vt.cs.cs5254.multiquiz.databinding.ActivityResultsBinding
import java.security.AccessControlContext
private const val EXTRA_WAPPER = "Result"
private const val EXTRA_TOTOAL_CORRECT = "total_correct"
private const val EXTRA_TOTOAL_QUESTIONS = "total_questions"
private const val EXTRA_TOTAL_HINT= "total_hint_used"

class ResultsActivity : AppCompatActivity() {
    lateinit var ui:ActivityResultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityResultsBinding.inflate(layoutInflater)
        val view = ui.root
        setContentView(view)

        //set total questions
        val totalQuestionsCount = intent.getIntExtra(EXTRA_TOTOAL_QUESTIONS, -1)
        val totalQuestionsText = getString(R.string.total_questions_value, totalQuestionsCount)

        ui.totalQuestionsValue.text = totalQuestionsText
        //set correct answer text and correct answer
        val correctAnswerCount = intent.getIntExtra(EXTRA_TOTOAL_CORRECT, -1)
        val correctAnswerText = getString(R.string.total_answers_correct_value, correctAnswerCount)
        ui.totalAnswersCorrectValue.text = correctAnswerText

        //set hint used value
        val totalHintUsed = intent.getIntExtra(EXTRA_TOTAL_HINT, -1)
        val totalHintUsedText = getString(R.string.total_hints_used_value, totalHintUsed)
        ui.totalHintsUsedValue.text = totalHintUsedText

    }

}